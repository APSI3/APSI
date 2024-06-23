import React from "react";
import { UserDTO} from "../api/DTOs";
import {Box, Paper, Typography} from "@mui/material";
import DeleteUserButton from "./DeleteUserButton";
import {UserTypes} from "../helpers/AuthHelpers";

const UserCard: React.FC<{ user: UserDTO, onDelete: (id: string) => void }> = ({ user, onDelete }) => {
    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly', margin: '0.5rem', background: '#dee2e6' }}  elevation={3} >
            <Box style={{
                display: "flex",
                width: "100%",
                justifyContent: "space-between",
                alignItems: "center"
            }}
                  padding={2} border={1} borderColor="grey.300" borderRadius={1}>
                <Typography variant="body1" style={{flex: 0}}>{`${user.id}. `}</Typography>
                <Typography variant="body1" style={{flex: 1}}>{user.login}</Typography>
                <Typography variant="body1" style={{flex: 1}}>{user.email}</Typography>
                <Typography variant="body1" style={{flex: 1}}>{user.type}</Typography>
                <DeleteUserButton user={user} disabled={user.type === UserTypes.SUPERADMIN} onDelete={onDelete}/>
            </Box>
        </Paper>
    );
}

export default UserCard;