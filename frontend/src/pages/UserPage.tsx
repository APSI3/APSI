import { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import { User } from "../model/User";


export default function UserPage() {
    const [users, setUsers] = useState<User[] | null>(null);

    useEffect(() => {
        testFetch().then(res => res.res).then(data => {
            if (data !== undefined) {
                setUsers(data);
            }
        })
    }, []);

    async function testFetch() {
        // this is only example
        const response = await fetch("http://localhost:8080/user", {
            headers: {
                'Content-Type': 'application/json',
            }
        });
        const json = await response.json();
        return { res: json };
    }

    return (
        <>
            <Helmet>
                <title>APSI - All users</title>
            </Helmet>
            <div>Lista loginów użytkowników:</div>
            <div>
                {
                    users != null && 
                    users.map((user, index) => (
                        <div key={`user-{index}`}><span>{index + 1}. </span>{user.login}</div>
                    ))
                }
            </div>
        </>
    )
}
