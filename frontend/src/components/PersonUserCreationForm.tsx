import React from 'react';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import {Api} from "../api/Api";
import {toastDefaultError, toastInfo} from "../helpers/ToastHelpers";
import {ValidationMessage} from "../helpers/FormHelpers";
import {CreateUserRequest} from "../api/Requests";

interface FormValues {
    login: string;
    email: string;
    password: string;
    repeatedPassword: string;
}

const initialValues: FormValues = {
    login: '',
    email: '',
    password: '',
    repeatedPassword: ''
};

const checkLogin = async (value: string) => {
    if (!value) return false;
    const response = await Api.GetUniqueLogin(value);
    return response.success && response.data;
}

const validationSchema = Yup.object({
    login: Yup.string()
        .required('Należy podać login'),
    email: Yup.string()
        .email('Niepoprawny format adresu email')
        .required('Należy podać email'),
    password: Yup.string()
        .required('Należy podać hasło'),
    repeatedPassword: Yup.string()
        .oneOf([Yup.ref('password')], 'Hasła muszą być identyczne')
        .required('Należy powtórzyć hasło')
});

const PersonUserCreationForm: React.FC = () => (
    <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={async (values, fh) => {
            const isUniqueLogin = await checkLogin(values.login);
            if (!isUniqueLogin) {
                fh.setFieldError('login', 'Login już zajęty');
                return;
            }

            await Api.CreateUser(values as CreateUserRequest).then(res => {
                if (res.success && res.data) {
                    toastInfo("Udało się stworzyć twoje konto. Przejdź na stronę logowania.");
                } else {
                    if (res.errors)
                        fh.setErrors(res.errors);
                    else
                        toastDefaultError()
                }
            })
        }}
    >
        {({ isSubmitting }) => (
            <Form>
                <div className="form-group row justify-content-center">
                    <label className="col-sm-1 col-form-label" htmlFor="login">Login</label>
                    <div className="col-sm-3">
                        <Field type="text" name="login" className="form-control" />
                        <ValidationMessage fieldName="login"/>
                    </div>
                </div>
                <br />
                <div className="form-group row justify-content-center">
                    <label className="col-sm-1 col-form-label" htmlFor="email">Email</label>
                    <div className="col-sm-3">
                        <Field type="email" name="email" className="form-control" />
                        <ValidationMessage fieldName="email"/>
                    </div>
                </div>
                <br />
                <div className="form-group row justify-content-center">
                    <label  className="col-sm-1 col-form-label" htmlFor="password">Hasło</label>
                    <div className="col-sm-3">
                        <Field type="password" name="password" className="form-control"/>
                        <ValidationMessage fieldName="password"/>
                    </div>
                </div>
                <br />
                <div className="form-group row justify-content-center">
                    <label  className="col-sm-1 col-form-label" htmlFor="repeatedPassword">Powtórz hasło</label>
                    <div className="col-sm-3">
                        <Field type="password" name="repeatedPassword"  className="form-control"/>
                        <ValidationMessage fieldName="repeatedPassword"/>
                    </div>
                </div>
                <div className="form-group row justify-content-center">
                    <label className="col-sm-1 col-form-label" />
                    <div className="col-sm-3">
                        <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Zarejestruj się</button>
                    </div>
                </div>
            </Form>
        )}
    </Formik>
);

export default PersonUserCreationForm;
