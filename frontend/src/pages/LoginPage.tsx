import LoginForm from "../components/LoginForm";
import {Paths} from "../App";
import React, {useState} from "react";
import {Box, IconButton, Modal} from "@mui/material";
import {modalStyle} from "../components/FormButton";
import CloseIcon from "@mui/icons-material/Close";
import CreateOrganizerForm from "../components/CreateOrganizerForm";

export default function LoginPage() {
    const [open, setOpen] = useState<boolean>(false);
    return <>
        <LoginForm/>
        <p className="justify-content-center">Nie masz jeszcze konta? <a href={Paths.register}>Zarejestruj się</a></p>
        <>
            <p className="justify-content-center">Chesz założyć konto organizatora? <a href='#' onClick={() => setOpen(true)}>Złóż wniosek</a></p>
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
                    <CreateOrganizerForm onClose={() => setOpen(false)}/>
                </Box>
            </Modal>
        </>
    </>
}