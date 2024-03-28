import secureLocalStorage from "react-secure-storage";
import { UserDTO } from "../api/DTOs";

export class AuthHelpers {
    private static userKey = "userData";
    private static authKey = "auth";

    public static StoreUserData(userData: UserDTO, authHeader: string) {
        secureLocalStorage.setItem(this.userKey, userData);
        secureLocalStorage.setItem(this.authKey, authHeader);
    }

    public static GetAuthKey(): string | undefined {
        return secureLocalStorage.getItem(this.authKey) as string;
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