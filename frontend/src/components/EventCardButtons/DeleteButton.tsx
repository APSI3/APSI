import React from "react";
import {Fab} from "@mui/material";
import {Delete} from "@mui/icons-material";

const DeleteButton: React.FC = () => {
    return <Fab size="small"><Delete /></Fab>
}

export default DeleteButton;