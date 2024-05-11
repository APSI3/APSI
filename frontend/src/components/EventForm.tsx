import { Field, FieldArray, Form, Formik } from "formik";
import { Helmet } from "react-helmet";
import { CreateEventRequest } from "../api/Requests";
import { Api } from "../api/Api";
import { toastDefaultError, toastInfo } from "../helpers/ToastHelpers";
import { ValidationMessage } from "../helpers/FormHelpers";
import { array, date, number, object, string } from "yup";
import DatePicker from "react-datepicker";
import TicketCard from "./TicketCard";

const initialValues: CreateEventRequest = {
    name: "",
    description: "",
    startDate: new Date(),
    endDate: new Date(),
    ticketTypes: [],
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
                .max(1000000, "Zbyt duża pula biletów"),
            price: number()
                .required("Należy podać cenę biletu")
                .max(100000, "Zbyt wysoka cena za bilet"),
        })
    ),
    location: object().shape({
        id: number()
    })
})

const EventForm: React.FC<{ onClose: () => void }> = ({ onClose }) => {
    return <>
        <Helmet>
            <title>APSI - Dodawanie wydarzenia</title>
        </Helmet>
        <Formik
            initialValues={initialValues}
            validationSchema={createEventValidationSchema}
            onSubmit={async (values, fh) => {
                await Api.CreateEvent(values).then(res => {
                    if (res.success && res.data) {
                        toastInfo("Udało się stworzyć wydarzenie " + res.data.name);
                        onClose();
                    }
                    else {
                        if (res.errors)
                            fh.setErrors(res.errors);
                        else
                            toastDefaultError()
                    }
                })
            }}
        >
            {({ isSubmitting, values, setFieldValue, errors }) => <Form className="form">
                <header className="mb-4 text-center h2">Nowe wydarzenie</header>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Nazwa</label>
                    <Field type="text" name="name" id="name" className="form-control" />
                    <ValidationMessage fieldName="name" />
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
                        selected={values.startDate}
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
                        selected={values.endDate}
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
                    <label htmlFor="ticketTypes" className="form-label">Typy biletów</label>
                    <FieldArray name="ticketTypes" 
                        render={helpers => <div className="p-1">
                            {values.ticketTypes.map((tt, idx) => <div key={idx} className="m-1" > 
                                <TicketCard ticket={{ ...tt, eventId: 0, id: idx}} skipApiCheck/>
                                <ValidationMessage fieldName={`ticketTypes.${idx}`} />
                            </div>)}
                            <button className="btn btn-primary" type="button"
                                onClick={e => helpers.push({ name: "test", price: 1, quantityAvailable: 1 })}
                            >
                                Dodaj typ biletu
                            </button>
                        </div>}
                    />
                    <ValidationMessage fieldName="ticketTypes" />
                </div>
                <div className="mb-3 text-center">
                    <button className="btn btn-primary" type="submit" disabled={isSubmitting}>Dodaj</button>
                </div>
            </Form>}
        </Formik>
    </>
}
export default EventForm;
