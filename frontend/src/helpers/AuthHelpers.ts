import secureLocalStorage from "react-secure-storage";
import { UserDTO } from "../api/DTOs";

export class AuthHelpers {
    private static userKey = "userData";

    public static StoreUserData(userData: UserDTO) {
        secureLocalStorage.setItem(this.userKey, userData);
    }

    public static GetUserData(): UserDTO | undefined {
        return secureLocalStorage.getItem(this.userKey) as UserDTO;
    }

    public static IsLoggedIn(): boolean {
        return !!AuthHelpers.GetUserData();
    }

    public static ClearAllData() {
        secureLocalStorage.clear();
    }
}