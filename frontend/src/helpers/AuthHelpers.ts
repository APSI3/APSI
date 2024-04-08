import secureLocalStorage from "react-secure-storage";
import { LoggedUserDTO } from "../api/DTOs";

export class AuthHelpers {
    private static userKey = "userData";
    private static authKey = "auth";

    public static StoreUserData(userData: LoggedUserDTO, authHeader: string) {
        secureLocalStorage.setItem(this.userKey, userData);
        secureLocalStorage.setItem(this.authKey, authHeader);
    }

    public static GetAuthKey(): string | undefined {
        return secureLocalStorage.getItem(this.authKey) as string;
    }

    public static GetUserData(): LoggedUserDTO | undefined {
        return secureLocalStorage.getItem(this.userKey) as LoggedUserDTO;
    }

    public static IsLoggedIn(): boolean {
        return !!AuthHelpers.GetUserData();
    }

    public static ClearAllData() {
        secureLocalStorage.clear();
    }

    public static HasAnyRole(roles: string[]) : boolean {
        const data = AuthHelpers.GetUserData();
        return !!data?.type && roles.some(r => r === data?.type)
    }

    public static getRole(): string | undefined {
        const data = AuthHelpers.GetUserData();
        return data?.type;
    }
}

export class UserTypes {
    public static SUPERADMIN = "SUPERADMIN";
    public static PERSON = "PERSON";
    public static ORGANIZER = "ORGANIZER";
} 