import React, {useState} from "react";
import {Fab, Tooltip} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    boxShadow: 24,
    p: 4,
};

const FormButton: React.FC<{title: string, icon: React.ReactNode, form: React.ReactNode }> = ({ title, icon = <AddIcon/>, form }) => {
    const [ open, setOpen ] = useState<boolean>(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    return (
        <>
            <Tooltip title={title} placement="left">
                <Fab color="primary" aria-label="add" onClick={handleOpen}>
                    {icon}
                </Fab>
            </Tooltip>

            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>{form}</Box>
            </Modal>
        </>
    );
};


export default FormButton;