import { object, string } from "yup";
import { CreateTicketRequest } from "../api/Requests";
import { Field, Form, Formik } from "formik";
import { Api } from "../api/Api";
import { toastInfo, toastDefaultError, toastError } from "../helpers/ToastHelpers";
import { useNavigate } from "react-router-dom";
import { ValidationMessage } from "../helpers/FormHelpers";
import { EventDTO, TicketTypeDTO } from "../api/DTOs";

const validationSchema = object<CreateTicketRequest>().shape({
    holderFirstName: string()
        .required("Należy podać imię")
        .max(255, "Maksymalna długość imienia to 255"),
    holderLastName: string()
        .required("Należy podać nazwisko")
        .max(255, "Maksymalna długość nazwiska to 255"),
})

export function BuyTicketForm({ ticketType, event }: { ticketType: TicketTypeDTO, event: EventDTO }){
    const nav = useNavigate();

    const initialValues: CreateTicketRequest = {
        holderFirstName: "",
        holderLastName: "",
        ticketType,
        event,
    }

    const toDate = new Date();
    toDate.setDate(toDate.getDate() + 7);

    return <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={(values, fh) => {
            Api.CreateTicket(values).then(res => {
                if (res.success && res.data) {
                    toastInfo("Zakupiono bilet");
                    nav(`/ticketSummary/${res.data.id}`, { state: res.data });
                }
                else if (res.errors) {
                    toastError(res.errors[0])
                    fh.setErrors(res.errors);
                }
                else toastDefaultError();
            })
        }}
    >
        {({ isSubmitting }) => <Form className="form">
            <header className="mb-2 text-center h2">Kup bilet</header>
            <div className="info">
                <div className="alert alert-primary">
                    Czy na pewno chcesz kupić bilet na wydarzenie {"\"" + event.name + "\""}?<br/>
                    Kupując bilet zobowiązujesz się wpłacić {ticketType.price.toFixed(2)} zł na numer konta PL32109024025256332731736334
                    do dnia <strong>{toDate.toLocaleDateString()}</strong>.
                </div>
            </div>
            <div className="form-group row justify-content-center mb-2">
                <label className="col-sm-6 col-form-label">Imię posiadacza biletu</label>
                <div className="col-sm-6">
                    <Field type="string" name="holderFirstName" className="form-control" />
                    <ValidationMessage fieldName="holderFirstName" />
                </div>
            </div>
            <div className="form-group row justify-content-center mb-2">
                <label className="col-sm-6 col-form-label">Nazwisko posiadacza biletu</label>
                <div className="col-sm-6">
                    <Field type="string" name="holderLastName" className="form-control"/>
                    <ValidationMessage fieldName="holderLastName" />
                </div>
            </div>
            <div className="form-group row justify-content-center mb-2 text-center" >
                <div className="col-sm-6">
                    <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Kup</button>
                </div>
            </div>
        </Form>}
    </Formik>
}