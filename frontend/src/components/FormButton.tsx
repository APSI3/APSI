import React, {useState} from "react";
import {Fab, Tooltip, IconButton} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import CloseIcon from '@mui/icons-material/Close';

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    minWidth: 400,
    width: '40vw',
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
                <div>
                    <Box sx={style}>
                        <IconButton aria-label="close" onClick={handleClose} style={{ position: 'absolute', top: 10, right: 10 }}>
                            <CloseIcon />
                        </IconButton>
                        {form}
                    </Box>
                </div>
            </Modal>
        </>
    );
};


export default FormButton;