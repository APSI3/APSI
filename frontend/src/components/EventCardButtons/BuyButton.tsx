import React, { useState } from "react";
import {Box, CardMedia, Fab, IconButton, Modal, Tooltip} from "@mui/material";
import {ShoppingCart} from "@mui/icons-material";
import {toastDefaultError, toastInfo} from "../../helpers/ToastHelpers";
import {Api} from "../../api/Api";
import {useNavigate} from "react-router-dom";
import CloseIcon from '@mui/icons-material/Close';
import { modalStyle } from "../FormButton";
import { Field, Form, Formik } from "formik";
import { Option, ValidationMessage } from "../../helpers/FormHelpers";
import { CreateTicketRequest } from "../../api/Requests";

const BuyButton: React.FC<{ ticketTypeId: number, sectionMap?: string, ticketTypes: Option[], sections: Option[] }>= (
    { ticketTypeId, sectionMap, ticketTypes, sections }
) => {
    const [open, setOpen] = useState(false);
    const nav = useNavigate();

    const initialValues: CreateTicketRequest = {
        ticketTypeId: ticketTypeId,
        sectionId: 0,
        holderFirstName: "",
        holderLastName: "",
    }

    return <>
        <Tooltip title={"Kup bilet"} placement="left">
            <Fab size="small" onClick={() => setOpen(true)}>
                <ShoppingCart/>
            </Fab>
        </Tooltip>
        <Modal
            open={open}
            onClose={() => setOpen(false)}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={modalStyle}>
                <IconButton aria-label="close" onClick={() => setOpen(false)} style={{ position: 'absolute', top: 10, right: 10 }}>
                    <CloseIcon />
                </IconButton>
                <Formik
                    initialValues={initialValues}
                    onSubmit={async (values, fh) => {
                        Api.CreateTicket(values).then(res => {
                            if (res.success && res.data) {
                                toastInfo("Zakupiono bilet");
                                nav(`/ticketSummary/${res.data.id}`, { state: res.data });
                            }
                            else if (res.errors) fh.setErrors(res.errors)
                            else toastDefaultError();
                        })
                    }}
                >
                    {({ isSubmitting }) => <Form className="form">
                        <header className="mb-3 text-center h2">Kup bilet</header>
                        <div className="d-flex justify-content-center">
                            <label>Rozkład miejsc:</label>
                        </div>
                        {!!sectionMap && <div className="d-flex justify-content-center mb-5">
                            <CardMedia
                                component="img"
                                src={sectionMap}
                                alt="Section Map"
                                style={{ maxHeight: '15rem', width: 'auto' }}
                            />
                        </div>}
                        <div className="form-group row justify-content-center mb-2">
                            <label className="col-sm-6 col-form-label">Typ biletu</label>
                            <div className="col-sm-6" style={{ position: 'relative' }}>
                                <Field as="select" className="form-control" name="ticketTypeId" disabled={true}>
                                    {ticketTypes.map(tt => <option key={tt.value} value={tt.value}>{tt.label}</option>)}
                                </Field>
                                <ValidationMessage fieldName="ticketTypeId"/>
                            </div>
                        </div>
                        <div className="form-group row justify-content-center mb-2">
                            <label className="col-sm-6 col-form-label">Rodzaj miejsca</label>
                            <div className="col-sm-6" style={{ position: 'relative' }}>
                                <Field as="select" className="form-control" name="sectionId">
                                    <option key={0} value={0}>--Wybierz--</option>
                                    {sections.map(s => <option key={s.value} value={s.value}>{s.label}</option>)}
                                </Field>
                                <ValidationMessage fieldName="sectionId" />
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
                                <Field type="string" name="holderLastName" className="form-control" />
                                <ValidationMessage fieldName="holderLastName" />
                            </div>
                        </div>
                        <div className="form-group row justify-content-center mb-2 text-center" >
                            <div className="col-sm-4">
                                <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Kup</button>
                            </div>
                        </div>
                    </Form>}
                </Formik>
            </Box>
        </Modal>
    </>
}

export default BuyButton;