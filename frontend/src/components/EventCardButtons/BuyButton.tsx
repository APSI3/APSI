import React from "react";
import {Fab} from "@mui/material";
import {ShoppingCart} from "@mui/icons-material";

const BuyButton: React.FC = () => {
    return <Fab size="small"><ShoppingCart /></Fab>
}

export default BuyButton;