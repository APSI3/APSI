import React from "react";
import {Fab} from "@mui/material";
import {ShoppingCart} from "@mui/icons-material";
import {toastDefaultError, toastInfo} from "../../helpers/ToastHelpers";
import {Api} from "../../api/Api";
import {CreateTicketRequest} from "../../api/Requests";
import {AuthHelpers} from "../../helpers/AuthHelpers";
import {useNavigate} from "react-router-dom";

const BuyButton: React.FC<{ ticketTypeId: number }> = ({ ticketTypeId }) => {
    const nav = useNavigate();
    const handleOnClick = () => {
        // TODO: check available ticket numbers
        const userData = AuthHelpers.GetUserData();
        if (!userData) {
            throw Error("Error fetching user data");
        }
        const createTicketRequest: CreateTicketRequest = {
            ticketTypeId,
            holderId: userData.id,
            purchaseDate: new Date(),
        };

        Api.CreateTicket(createTicketRequest).then(res => {
            if (res.success && res.data) {
                toastInfo("Zakupiono bilet");
                nav(`/ticketSummary/${res.data.id}`, { state: res.data });
            }
            else {
                toastDefaultError();
            }

        // send email to user
        })
    }

    return <Fab
        size="small"
        onClick={handleOnClick}
    >
        <ShoppingCart />
    </Fab>
}

export default BuyButton;