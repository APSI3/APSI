import {Field, FieldArray, Form, Formik, FormikHelpers} from "formik";
import { Helmet } from "react-helmet";
import { Api } from "../api/Api";
import { toastDefaultError, toastError, toastInfo } from "../helpers/ToastHelpers";
import { ValidationMessage, getLocationString } from "../helpers/FormHelpers";
import { array, date, number, object, string } from "yup";
import DatePicker from "react-datepicker";
import { Grid, Paper } from "@mui/material";
import React, { useEffect, useState } from "react";
import { LocationDTO } from "../api/DTOs";
import {CreateEventRequest, UpdateEventRequest} from "../api/Requests";

const createInitialValues: CreateEventRequest = {
    name: "",
    description: "",
    startDate: new Date(),
    endDate: new Date(),
    ticketTypes: [
        {
            name: "Nowy typ biletu",
            price: 0,
            quantityAvailable: 0,
        }
    ],
    startTime: "",
    endTime: "",
}

const timeRegex = new RegExp("^[0-9]{2}:[0-9]{2}$");

const createEventValidationSchema = object<CreateEventRequest>().shape({
    name: string()
        .max(255, "Nazwa wydarzenia jest zbyt długa")
        .required("Należy podać nazwę wydarzenia"),
    description: string()
        .max(2000, "Opis jest zbyt długi"),
    startDate: date()
        .required("Należy podać datę początkową"),
    endDate: date()
        .required("Należy podać datę końcową"),
    startTime: string()
        .matches(timeRegex),
    endTime: string()
        .matches(timeRegex),
    ticketTypes: array().of(
        object().shape({
            name: string()
                .max(100, "Zbyt długa nazwa typu biletu")
                .required("Należy podać nazwę typu biletu"),
            quantityAvailable: number()
                .max(1000000, "Zbyt duża pula biletów")
                .min(1, "Musi być dostępny przynajmniej 1 bilet"),
            price: number()
                .required("Należy podać cenę biletu")
                .min(0, "Cena nie może być ujemna")
                .max(100000, "Zbyt wysoka cena za bilet"),
        })
    ).min(1, "Należy dodać przynajmniej jeden typ biletów"),
    location: object().shape({
        id: number()
    }).nullable()
})

const isUpdateRequest = (values: Object): values is UpdateEventRequest => {
    return (values as UpdateEventRequest).id !== undefined;
};

const EventForm: React.FC<{ onClose: () => void, initialValues?: Object }> = ({ onClose, initialValues = createInitialValues }) => {
    const [locations, setLocations] = useState<LocationDTO[]>([])
    const isUpdate = isUpdateRequest(initialValues);
    console.log(initialValues);

    useEffect(() => {
        Api.GetLocations().then(res => {
            if (res.success && res.data)
                setLocations(res.data)
            else
                toastError("Nie udało się pobrać dostępnych lokalizacji")
        })
    }, [])

    const options = [
        { label: "Nie wybrano", value: 0},
        ...locations.map(l => ({ label: getLocationString(l), value: l.id }))
    ]

    const handleCreateEvent = async (newValues: CreateEventRequest, fh: FormikHelpers<any>) => {
        await Api.CreateEvent(newValues).then(res => {
            if (res.success && res.data) {
                toastInfo("Udało się stworzyć wydarzenie " + res.data.name);
                onClose();
            } else {
                if (res.errors)
                    fh.setErrors(res.errors);
                else
                    toastDefaultError()
            }
        })
    }

    const handleUpdateEvent = async (newValues: UpdateEventRequest, fh: FormikHelpers<any>) => {
        let hasErrors = false;

        for (let idx = 0; idx < newValues.ticketTypes.length; idx++) {
            const ticket = newValues.ticketTypes[idx];
            const oldTicket = (initialValues as UpdateEventRequest).ticketTypes[idx];

            if (oldTicket?.quantityAvailable !== ticket?.quantityAvailable) {
                await Api.GetSoldTicketsCount(ticket.id).then(res => {
                    if (res.data && ticket.quantityAvailable - res.data < 0) {
                        fh.setFieldError(`ticketTypes.${idx}.quantityAvailable`, 'Nie można zmienić liczby biletów poniżej dostępnej wartości');
                        hasErrors = true;
                    }
                });
            }
        }

        if (hasErrors) {
            return;
        }
        await Api.UpdateEvent({...newValues, id : (initialValues as UpdateEventRequest).id }).then(res => {
            if (res.success && res.data) {
                toastInfo("Udało się zaktualizować wydarzenie" + (initialValues as UpdateEventRequest).name);
                onClose();
            } else {
                if (res.errors)
                    fh.setErrors(res.errors);
                else
                    toastDefaultError()
            }
        })
    }

    return <>
        <Helmet>
            <title>APSI - Dodawanie wydarzenia</title>
        </Helmet>
        <Formik
            initialValues={initialValues}
            validationSchema={createEventValidationSchema}
            onSubmit={async (values, fh) => {
                let newValues = values;
                if ((values as UpdateEventRequest).location?.id == 0) // location id can come to us as string - soft compare
                    newValues = { ...newValues, location: undefined}

                isUpdate ? handleUpdateEvent((newValues as UpdateEventRequest), fh) : handleCreateEvent((newValues as CreateEventRequest), fh)
            }}
        >
            {({ isSubmitting, values, setFieldValue, setFieldError }) => <Form className="form">
                <header className="mb-4 mt-3 text-center h2">{isUpdate ? 'Edytuj wydarzenie' : 'Nowe wydarzenie'}</header>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Nazwa</label>
                    <Field type="text" name="name" id="name" className="form-control" />
                    <ValidationMessage fieldName="name" />
                    <ValidationMessage fieldName="id" />
                </div>
                <div className="mb-3">
                    <label htmlFor="description" className="form-label">Opis</label>
                    <Field style={{ minHeight: '100px' }} type="text" as="textarea"
                        name="description" id="description" className="form-control"
                    />
                    <ValidationMessage fieldName="description" />
                </div>
                <div className="mb-3">
                    <label htmlFor="startDate" className="form-label">Od</label><br />
                    <DatePicker className="form-control"
                        dateFormat={"dd/MM/yyyy"}
                        selected={(values as CreateEventRequest).startDate}
                        onChange={e => setFieldValue("startDate", e ?? new Date())}
                        id="startDate"
                    />
                    <ValidationMessage fieldName="startDate" />
                </div>
                <div className="mb-3">
                    <label htmlFor="startTime" className="form-label">Godzina początkowa</label>
                    <Field type="time" name="startTime" id="startTime" className="form-control" />
                    <ValidationMessage fieldName="startTime" />
                </div>
                <div className="mb-3">
                    <label htmlFor="endDate" className="form-label">Do</label><br/>
                    <DatePicker className="form-control"
                        dateFormat={"dd/MM/yyyy"}
                        selected={(values as CreateEventRequest).endDate}
                        onChange={e => setFieldValue("endDate", e ?? new Date())}
                        id="endDate"
                    />
                    <ValidationMessage fieldName="endDate" />
                </div>
                <div className="mb-3">
                    <label htmlFor="endTime" className="form-label">Godzina końcowa</label>
                    <Field type="time" name="endTime" id="endTime" className="form-control" />
                    <ValidationMessage fieldName="endTime" />
                </div>
                <div className="mb-3">
                    <label htmlFor="location.id" className="form-label">Lokacja</label>
                    <Field as="select" name="location.id" id="location.id" className="form-control">
                        {options.map(o => <option key={o.value} value={o.value}>{o.label}</option>)} 
                    </Field>
                    <ValidationMessage fieldName="location.id" />
                    <ValidationMessage fieldName="location" />
                </div>
                <div className="mb-3">
                    <label htmlFor="image" className="form-label">Obraz</label>
                    {/* couldn't get the initial image to load to the loader */}
                    {(initialValues as CreateEventRequest)?.image && <span> (zastąpi istniejący obraz)</span>}
                    <input className="form-control" type="file" accept="image/*" id="image" name="image" onChange={e => {
                        const reader = new FileReader();
                        reader.onload = () => {
                            if (reader.readyState === 2) 
                                setFieldValue("image", reader.result)
                            else 
                                setFieldError("image", "Nie udało się wczytać obrazu")
                        }

                        if (!!e.target.files) {
                            if (e.target.files[0]?.size > 500_000)
                                setFieldError("image", "Maksymalna wielkość pliku to 500 KB")
                            else
                                reader.readAsArrayBuffer(e.target.files[0])
                        }
                    }}/>
                    <ValidationMessage fieldName="image" />
                </div>
                <div className="mb-3">
                    <label htmlFor="ticketTypes" className="form-label">Typy biletów</label>
                    <FieldArray name="ticketTypes" 
                        render={helpers => <div className="p-1">
                            {(values as CreateEventRequest).ticketTypes.map((tt, idx) => {
                                const name = `ticketTypes.${idx}`;
                                return <Paper key={idx} className="m-1" style={{ padding: 20 }}>
                                    <Grid container spacing={1} alignItems="center">
                                        <Grid item xs={12} sm={6} md={4} lg={3}>
                                            <label htmlFor={name + ".name"} className="form-label">Nazwa</label>
                                        </Grid>
                                        <Grid item xs={12} sm={6} md={8} lg={9}>
                                            <Field type="string" name={name + ".name"} id={name + ".name"} className="form-control" />
                                            <ValidationMessage fieldName={name + ".name"} />
                                        </Grid>

                                        <Grid item xs={12} sm={6} md={4} lg={3}>
                                            <label htmlFor={name + ".price"} className="form-label">Cena</label>
                                        </Grid>
                                        <Grid item xs={12} sm={6} md={8} lg={9}>
                                            <Field type="number" name={name + ".price"} id={name + ".price"} className="form-control" />
                                            <ValidationMessage fieldName={name + ".price"} />
                                        </Grid>

                                        <Grid item xs={12} sm={6} md={4} lg={3}>
                                            <label htmlFor={name + ".quantityAvailable"} className="form-label">Dostępna ilość</label>
                                        </Grid>
                                        <Grid item xs={12} sm={6} md={8} lg={9}>
                                            <Field type="number" name={name + ".quantityAvailable"} id={name + ".quantityAvailable"} className="form-control" />
                                            <ValidationMessage fieldName={name + ".quantityAvailable"} />
                                        </Grid>
                                    </Grid>
                                    <button className="btn btn-danger" type="button" onClick={() => helpers.remove(idx)}>
                                        Usuń
                                    </button>
                                </Paper>}
                            )}
                            <button className="btn btn-primary" type="button"
                                onClick={e => helpers.push({ name: "", price: 0, quantityAvailable: 0 })}
                            >
                                Dodaj typ biletu
                            </button>
                        </div>}
                    />
                    <ValidationMessage fieldName="tickets" />
                    <ValidationMessage fieldName="ticketTypes" />
                </div>
                <div className="mb-3 text-center">
                    <button className="btn btn-primary" type="submit" disabled={isSubmitting}>{isUpdate ? 'Aktualizuj' : 'Dodaj'}</button>
                </div>
            </Form>}
        </Formik>
    </>
}
export default EventForm;