import PersonUserCreationForm from "../components/PersonUserCreationForm";
import {Paths} from "../App";

export default function RegistrationPage() {
    return <>
        <PersonUserCreationForm/>
        <br/>
        <p className="justify-content-center">Masz już konto? <a href={Paths.login}>Zaloguj się</a></p>
    </>
}