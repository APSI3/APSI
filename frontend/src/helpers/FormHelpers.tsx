import { ErrorMessage } from "formik"
import React from "react"
import { LocationDTO } from "../api/DTOs"

export const ValidationMessage = ({ fieldName }: { fieldName: string }) => {
    return <ErrorMessage name={fieldName} render={msg => {
        if (typeof msg === "string"){
            return <div className="error-msg">
                {msg.split('\n').map((m, idx) => <React.Fragment key={idx}>
                    {m}
                    <br />
                </React.Fragment>)}
            </div>
        }
        else if (typeof msg === "object"){
            // formik trochę utrudnia życie przy błędach na arrayach
            const arr = msg as string[]
            if (arr.some(e => typeof e === "string")) {
                return <div className="error-msg">
                    {arr.filter(e => typeof e === "string").map((m, idx) => <React.Fragment key={idx}>
                        {m}
                        <br />
                    </React.Fragment>)}
                </div>
            }
        }
    }} />
}

export const getLocationString = (loc: LocationDTO) => {
    let string = "";
    if (!!loc.street)
        string += loc.street + " "
    if (!!loc.building_nr)
        string += loc.building_nr + " "
    
    string += loc.city;
    return string;
}

export type Option = {
    value: any,
    label: string
}
