import {useCallback, useEffect, useState} from "react";
import {UserDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import {AuthHelpers} from "../helpers/AuthHelpers";
import UserCard from "../components/UserCard";
import Pages from "../components/Pages";

export default function UsersPage() {
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    const getUsers = useCallback((currentIndex: number) => {
        Api.GetUsers(currentIndex).then(res => {
            if (res.success && res.data) {
                setUsers(res.data.items ?? []);
                setMaxIdx(res.data.totalPages - 1);
            }
        });
    }, []);

    useEffect(() => {
        getUsers(currentIdx);
    }, [currentIdx]);

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        getUsers(index);
    };

    const handleDelete = (id: string) => {
        setUsers(users.filter(u => u.id !== id))
    }

    return <>
        {users.map(user => <UserCard user={user} onDelete={handleDelete}/>)}
        <br />
        <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
    </>
}