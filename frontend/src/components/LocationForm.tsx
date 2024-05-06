import { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import { Field, Formik, Form } from "formik";
import { object, string, number } from "yup";
import { Api } from "../api/Api";
import { CreateLocationRequest } from "../api/Requests";
import { ValidationMessage } from "../helpers/FormHelpers";
import { CountryDTO } from "../api/DTOs";
import { CountriesSearchResult } from "./CountriesSearchResult";
import { toastDefaultError, toastInfo } from "../helpers/ToastHelpers";
import "../style/forms.css"

const initialValues: CreateLocationRequest = {
    country_id: 1,
    capacity: '',
    description: '',
    city: '',
    street: '',
    building_nr: '',
    apartment_nr: '',
    zip_code: '',
}

const num_of_countries_hints = 3;

const createLocationValidationSchema = object<CreateLocationRequest>().shape({
    country_id: number()
        .required("Należy wybrać kraj")
        .min(1, "ID kraju musi być dodatnie"),
    capacity: number()
        .max(8000000000, "Cała populacja ziemi to trochę przesada")
        .min(1, "Pojemność musi być większa od zera")
        .required("Należy podać pojemność"),
    city: string()
        .required("Należy podać miejscowość"),
    street: string()
        .required("Należy podać ulicę"),
    building_nr: string()
        .required("Należy podać number budynku"),
    zip_code: string()
        .required("Należy podać kod pocztowy")
})


const LocationForm: React.FC = () => {
    const [ countries, setCountries ] = useState<CountryDTO[]>([]);
    const [ matchingCountries, setMatchingCountries ] = useState<CountryDTO[]>([]);
    const [ typedCountryName, setTypedCountryName ] = useState<string>("");

    useEffect(() => {
        setTypedCountryName("Polska");
        getCountries();
    }, [])

    const getCountries = async () => {
        await Api.GetCountries().then(res => {
            if (res.success && res.data) {
                setCountries(res.data);
            }
        })
    };

    const searchCountry = (value: string, setValue: any) => {
        setTypedCountryName(value);
        setValue("country_id", null);
        let localMatchingCountries: CountryDTO[] = [];
        for (const country of countries) {
            if (country.full_name.toLowerCase().includes(value.trim().toLowerCase())) {
                localMatchingCountries = [...localMatchingCountries, country];
            }
            if (localMatchingCountries.length >= num_of_countries_hints) {
                break;
            }
        }
        setMatchingCountries(localMatchingCountries);
    };

    return (
        <>
        <Helmet>
            <title>APSI - Dodawanie lokalizacji</title>
        </Helmet>
        <Formik
            validationSchema={createLocationValidationSchema}
            initialValues={initialValues}
            onSubmit={async (values, fh) => {
                await Api.CreateLocation(values).then(res => {
                    if (res.success && res.data) {
                        toastInfo("Pomyślnie dodano nową lokalizację");
                    }
                    else {
                        toastDefaultError();
                    }
                })
            }}
        >
            {({ isSubmitting, setFieldValue, values }) => <Form className="form">
                <header className="mb-2 text-center h2">Nowa lokalizacja</header>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Opis</label>
                    <div className="col-sm-6">
                            <Field as="textarea" name="description" className="form-control" style={{ minHeight: '100px' }} />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Kraj</label>
                    <div className="col-sm-6" style={{ position: 'relative' }}>
                        <Field 
                            type="string" 
                            name="country_name"
                            className="form-control"
                            value={typedCountryName}
                            autoComplete="off"
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => searchCountry(e.target.value, setFieldValue)}
                        />
                        <div className="search-results-container">
                        {
                            typedCountryName.length > 0 && values.country_id == null && matchingCountries.map((matched, idx) => (
                                <CountriesSearchResult 
                                    country={matched} 
                                    onClick={(c: CountryDTO) => {
                                        setFieldValue("country_id", c.id);
                                        setTypedCountryName(c.full_name);
                                    }}
                                    key={idx}
                                />
                            ))
                        }
                        </div>
                        <input type="text" name="country_id" hidden/>
                        <ValidationMessage fieldName="country_id" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Pojemność</label>
                    <div className="col-sm-6">
                        <Field type="number" name="capacity" className="form-control" />
                        <ValidationMessage fieldName="capacity" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Miejscowość</label>
                    <div className="col-sm-6">
                        <Field type="text" name="city" className="form-control" />
                        <ValidationMessage fieldName="city" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Ulica</label>
                    <div className="col-sm-6">
                        <Field type="text" name="street" className="form-control" />
                        <ValidationMessage fieldName="street" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Numer budynku</label>
                    <div className="col-sm-6">
                        <Field type="text" name="building_nr" className="form-control" />
                        <ValidationMessage fieldName="building_nr" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Numer lokalu</label>
                    <div className="col-sm-6">
                        <Field type="text" name="apartment_nr" className="form-control" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2">
                    <label className="col-sm-6 col-form-label">Kod pocztowy</label>
                    <div className="col-sm-6">
                        <Field type="text" name="zip_code" className="form-control" />
                        <ValidationMessage fieldName="zip_code" />
                    </div>
                </div>
                <div className="form-group row justify-content-center mb-2 text-center" >
                    <div className="col-sm-6">
                        <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Dodaj</button>
                    </div>
                </div>
            </Form>}
        </Formik>
        </>
    )
}
export default LocationForm;
