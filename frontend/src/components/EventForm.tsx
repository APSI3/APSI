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
    // TODO: poprawić wygląd
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
                        toastInfo("Udało się stworzyć wydarzenie " + res.data.name);
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
                <label>Nazwa</label>
                <div>
                    <Field type="text" name="name" className="form-control" />
                    <ValidationMessage fieldName="name" />
                </div>
                <label>Opis</label>
                <div>
                    <Field type="text" name="description" className="form-control" />
                    <ValidationMessage fieldName="description" />
                </div>
                <label>Od</label>
                <div>
                    <Field type="date" name="startDate" className="form-control" />
                    <ValidationMessage fieldName="startDate" />
                </div>
                <label>Do</label>
                <div>
                    <Field type="date" name="endDate" className="form-control" />
                    <ValidationMessage fieldName="endDate" />
                </div>
                <div>
                    <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Dodaj</button>
                </div>
            </Form>}
        </Formik>
    </>
}