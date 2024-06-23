import React, {useState} from "react";
import {Box, Button, Fab, IconButton, Typography} from "@mui/material";
import {Delete} from "@mui/icons-material";
import {UserDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import {toastDefaultError, toastError, toastInfo} from "../helpers/ToastHelpers";
import Modal from "@mui/material/Modal";
import {modalStyle} from "./FormButton";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";

const DeleteUserButton: React.FC<{ user: UserDTO, disabled: boolean, onDelete: (id: string) => void }> = ({ user, disabled, onDelete }) => {
    const [ open, setOpen ] = useState<boolean>(false);
    const handleDelete = () => {
        if (user.id) {
            Api.DeleteUser(user.id).then( res => {
                if (res.success) {
                    toastInfo("Udało się usunąć użytkownika.");
                    setOpen(!open);
                    onDelete(user.id);
                } else {
                    toastDefaultError();
                }
            })
        } else {
            toastError("Nie można usunąć użytkownika");
        }
    }

    const handleModal = () => {
        setOpen(!open);
    };

    return <>
        <Fab size="small" color="error" onClick={handleModal} disabled={disabled}>
            <Delete/>
        </Fab>
        <Modal
            open={open}
            onClose={handleModal}
        >
            <Box sx={{...modalStyle, '&::-webkit-scrollbar': {display: 'none'}}}>
                <IconButton aria-label="close" onClick={handleModal} style={{ position: 'absolute', top: 10, right: 10 }}>
                    <CloseIcon/>
                </IconButton>
                <Box style={{
                    display: 'flex',
                    marginTop: '20px',
                    marginBottom: '20px',
                    flexDirection: 'column',
                }}>
                    <Typography>{'Czy na pewno chcesz usunąć tego użytkownika?'}</Typography>
                    <Box style={{
                        display: 'flex',
                        flexDirection: 'row',
                        marginTop: '20px',
                        justifyContent: 'center',
                        gap: '1rem',
                    }}>
                        <Button color="primary" variant="contained" endIcon={<CheckIcon />} onClick={handleDelete}>Tak</Button>
                        <Button color="error" variant="contained" endIcon={<CloseIcon />} onClick={handleModal}>Nie</Button>
                    </Box>
                </Box>
            </Box>
        </Modal>
    </>
}

export default DeleteUserButton;