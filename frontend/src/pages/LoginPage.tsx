import { Field, Form, Formik } from "formik";
import { Api } from "../api/Api";
import { useNavigate } from "react-router-dom";
import { Paths } from "../App";
import { object, string } from "yup";
import { LoginRequest } from "../api/Requests";
import { AuthHelpers } from "../helpers/AuthHelpers";
import { ValidationMessage } from "../helpers/FormHelpers";
import { Helmet } from "react-helmet";

const initialValues: LoginRequest = {
    login: "",
    password: ""
}

const loginValidationSchema = object<LoginRequest>().shape({
    login: string()
        .max(64, "Zbyt długi login")
        .min(4, "Zbyt krótki login")
        .required("Należy podać login"),
    password: string()
        .max(128, "Zbyt długie hasło")
        .required("Należy podać hasło")
})

export default function LoginPage() {
    const nav = useNavigate();

    function toastDefaultError() {
        throw new Error("Function not implemented.");
    }

    return <>
        <Helmet>
            <title>APSI - logowanie</title>
        </Helmet>
        <div className="login">
            <Formik
                validationSchema={loginValidationSchema}
                initialValues={initialValues}
                onSubmit={async (values, fh) => {
                    await Api.Login(values).then(res => {
                        if (res.success && res.data) {
                            nav(Paths.main)
                            AuthHelpers.StoreUserData(res.data.user);
                        }
                        else {
                            fh.setFieldValue('password', '', false);
                            if (res.errors)
                                fh.setErrors(res.errors);
                            else
                                toastDefaultError()
                        }
                    })
                }}
            >
                {({ isSubmitting }) => <Form className="form">
                    <div className="form-group row justify-content-center">
                        <label className="col-sm-1 col-form-label">Login</label>
                        <div className="col-sm-3">
                            <Field type="text" name="login" className="form-control" />
                            <ValidationMessage fieldName="login" />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row justify-content-center">
                        <label className="col-sm-1 col-form-label">Hasło</label>
                        <div className="col-sm-3">
                            <Field type="password" name="password" className="form-control" />
                            <ValidationMessage fieldName="password" />
                        </div>
                    </div>
                    <br />
                    <div className="form-group row justify-content-center">
                        <label className="col-sm-1 col-form-label" />
                        <div className="col-sm-3">
                            <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Zaloguj</button>
                        </div>
                    </div>
                </Form>}
            </Formik>
        </div>
    </>
}