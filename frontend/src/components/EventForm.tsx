import { Field, Form, Formik } from "formik";
import { Helmet } from "react-helmet";
import { CreateEventRequest } from "../api/Requests";
import { Api } from "../api/Api";
import { toastDefaultError, toastInfo } from "../helpers/ToastHelpers";
import { ValidationMessage } from "../helpers/FormHelpers";

const initialValues: CreateEventRequest = {
    name: "",
    description: "",
    startDate: new Date(),
    endDate: new Date(),
}

export default function EventForm() {
    // TODO: JIRA APSI-44
    // TODO: dodać walidację z yupa
    return <>
        <Helmet>
            <title>APSI - Dodawanie wydarzenia</title>
        </Helmet>
        <Formik
            initialValues={initialValues}
            onSubmit={async (values, fh) => {
                await Api.CreateEvent(values).then(res => {
                    if (res.success && res.data) {
                        toastInfo("Udało się stworzyć wydarzenie " + res.data.event.name);
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
            {({ isSubmitting }) => <Form className="form">
                <header className="mb-4 text-center h2">Nowe wydarzenie</header>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Nazwa</label>
                    <Field type="text" name="name" id="name" className="form-control" />
                    <ValidationMessage fieldName="name" />
                </div>
                <div className="mb-3">
                    <label htmlFor="description" className="form-label">Opis</label>
                    <Field type="text" name="description" id="description" className="form-control" />
                    <ValidationMessage fieldName="description" />
                </div>
                <div className="mb-3">
                    <label htmlFor="startDate" className="form-label">Od</label>
                    <Field type="date" name="startDate" id="startDate" className="form-control" />
                    <ValidationMessage fieldName="startDate" />
                </div>
                <div className="mb-3">
                    <label htmlFor="endDate" className="form-label">Do</label>
                    <Field type="date" name="endDate" id="endDate" className="form-control" />
                    <ValidationMessage fieldName="endDate" />
                </div>
                <div className="mb-3 text-center">
                    <button className="btn btn-primary" type="submit" disabled={isSubmitting}>Dodaj</button>
                </div>
            </Form>}
        </Formik>
    </>
}