import LoginForm from "../components/LoginForm";
import {Paths} from "../App";

export default function LoginPage() {
    return <>
        <LoginForm/>
        <p className="justify-content-center">Nie masz jeszcze konta? <a href={Paths.register}>Zarejestruj się</a></p>
    </>
}