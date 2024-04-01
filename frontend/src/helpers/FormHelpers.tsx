import { ErrorMessage } from "formik"
import React from "react"

export const ValidationMessage = ({ fieldName }: { fieldName: string }) => {
    return <ErrorMessage name={fieldName} render={msg => {
        return <div className="error-msg">
            {msg.split('\n').map((m, idx) => <React.Fragment key={idx}>
                {m}
                <br />
            </React.Fragment>)}
        </div>
    }} />
}