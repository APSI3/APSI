import React, {useState} from "react";
import { FormDTO } from "../api/DTOs";
import {Box, Fab, IconButton, Paper, Typography, Modal, Grid} from "@mui/material";
import {Close, Check} from "@mui/icons-material";
import CloseIcon from '@mui/icons-material/Close';
import {modalStyle} from "./FormButton";
import {Field, Form, Formik} from "formik";
import {FormStatus, ValidationMessage} from "../helpers/FormHelpers";
import { object, string } from "yup";
import {toastDefaultError, toastInfo} from "../helpers/ToastHelpers";
import {Api} from "../api/Api";
import {RejectionRequest} from "../api/Requests";

const validationSchema = object().shape({
    description: string().required('Powód odrzucenia jest wymagany.'),
});

const ApplicationCard: React.FC<{ application: FormDTO }> = ({ application }) => {
    const [open, setOpen] = useState<boolean>(false);
    const handleAccept = () => {
        Api.AcceptApplication(application.id).then(res => {
            if (res.success && res.data) {
                toastInfo("Udało się stworzyć organizatora " + res.data.login);
                application.status = FormStatus.ACCEPTED;
                setOpen(false);
            } else {
                toastDefaultError();
            }
        });
    }

    const handleReject = (rejectionCause: string) => {
        const rejectionRequest: RejectionRequest = {
            id: application.id,
            cause: rejectionCause
        }

        Api.RejectApplication(rejectionRequest).then(res => {
            if (res.success && res.data) {
                toastInfo("Wniosek został pomyślnie odrzucony");
                application.status = FormStatus.REJECTED;
                setOpen(false);
            } else {
                toastDefaultError();
            }
        });
    }

    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-between', margin: '0.5rem', background: '#dee2e6' }}  elevation={3} >
            <Box style={{ marginLeft: '1rem' }}>
                <Grid container marginBottom={'5px'} marginTop={'20px'}>
                    <Typography variant="body1" style={{ marginRight: '0.5rem' }}><strong>Login:</strong></Typography>
                    <Typography variant="body1">{application.login}</Typography>
                </Grid>
                <Grid container marginBottom={'20px'}>
                    <Typography variant="body1" style={{ marginRight: '0.5rem' }}><strong>Email:</strong></Typography>
                    <Typography variant="body1" >{application.email}</Typography>
                </Grid>
            </Box>
            {application.status === FormStatus.PENDING ? (
            <div style={{display: 'flex', justifyContent: 'center'}}>
                <Fab size="small" style={{ marginRight: '1rem' }} color={"success"} onClick={handleAccept}><Check/></Fab>
                <Fab size="small" style={{ marginRight: '1rem' }} color={"error"} onClick={() => setOpen(true)}><Close/></Fab>
                    <Modal
                        open={open}
                        onClose={() => setOpen(false)}
                    >
                        <Box sx={modalStyle}>
                            <IconButton aria-label="close" onClick={() => setOpen(false)} style={{ position: 'absolute', top: 10, right: 10 }}>
                                <CloseIcon />
                            </IconButton>
                            <Formik
                                initialValues={{ description: '' }}
                                validationSchema={validationSchema}
                                onSubmit={values => handleReject(values.description)}
                            >
                                {({ isSubmitting }) => (
                                    <Form>
                                        <div className="form-group row justify-content-center mb-2">
                                            <label className="col-form-label">Powód odrzucenia:</label>
                                            <div className="col-sm-12">
                                                <Field as="textarea" name="description" className="form-control" style={{ minHeight: '100px' }} />
                                                <ValidationMessage fieldName="description" />
                                            </div>
                                        </div>
                                        <div className="form-group row justify-content-center mb-2 text-center" >
                                            <div className="col-sm-6">
                                                <button className="btn btn-primary form-control" type="submit" disabled={isSubmitting}>Prześlij</button>
                                            </div>
                                        </div>
                                    </Form>
                                )}
                            </Formik>
                        </Box>
                    </Modal>
            </div>) : <Typography marginRight={'1rem'} color={application.status === FormStatus.ACCEPTED ? 'green' : 'red'}>{application.status}</Typography>}
        </Paper>
    );
}

export default ApplicationCard;