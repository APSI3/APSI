import React from "react";
import {Fab} from "@mui/material";
import {Visibility} from "@mui/icons-material";
import {Link} from "react-router-dom";

const DetailsButton: React.FC<{ id: number }> = ({ id }) => {
return <Link to={`/event/${id}`}><Fab size="small"><Visibility /></Fab></Link>
}

export default DetailsButton;