import React, { useState } from "react";
import {Box, Fab, IconButton, Modal} from "@mui/material";
import { Call, Delete } from "@mui/icons-material";
import { EventDTO } from "../../api/DTOs";
import { Api } from "../../api/Api";
import { useNavigate } from "react-router-dom";
import CloseIcon from "@mui/icons-material/Close";
import {toastDefaultError, toastInfo} from "../../helpers/ToastHelpers";

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    boxShadow: 24,
    p: 4,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center'
};

const cancel = (id: number, callback: () => void) => {
    Api.CancelEvent(id).then(res => {
        if (res.success) {
            toastInfo("Usunięto wydarznie");
        } else {
            toastDefaultError();
        }
    });
    callback();
};

const DeleteButton: React.FC<{ event: EventDTO, callback: (id: number) => void }> = ({ event, callback }) => {
    const [open, setOpen] = useState<boolean>(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setOpen(false)
    };
    const nav = useNavigate();
    return <>
        <Fab size="small" onClick={handleOpen}>
            <Delete />
        </Fab>
        <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={modalStyle}>
                <IconButton aria-label="close" onClick={() => setOpen(false)} style={{ position: 'absolute', top: 10, right: 10 }}>
                    <CloseIcon />
                </IconButton>
                <label style={{ marginBottom: '16px', marginTop: '16px', textAlign: 'center' }}>
                    Czy na pewno chcesz anulować wydarzenie? Spowoduje to wysłanie maili do osób, które zdążyły już kupić bilety.
                </label>
                <button className="btn btn-primary" type="button"
                    onClick={() => {
                        cancel(event.id, () => { nav("/events"); callback(event.id); })
                        setOpen(false);
                    }}
                >
                    Anuluj wydarzenie
                </button>
                <button className="btn" type="button"
                    onClick={() => setOpen(false)}
                >
                    Rozmyśliłem się
                </button>
            </Box>
        </Modal>
    </>
}

export default DeleteButton;