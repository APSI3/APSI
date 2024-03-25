import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider } from "react-router-dom";
import Footer from "./components/Footer";
import { UnAuthorizedPage } from "./pages/UnauthorizedPage";
import { AuthorizedPage } from "./pages/AuthorizedPage";
import UserPage from "./pages/UserPage";
import LoginPage from "./pages/LoginPage";

export const Paths = {
    login: "/login",
    main: "/",
}

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route>
            <Route path={Paths.login} element={<UnAuthorizedPage page={<LoginPage />} />} />
            <Route path={Paths.main} element={<AuthorizedPage page={<UserPage />} />} />
        </Route>
    )
)

function App() {
    return <>
        <RouterProvider router={router} />
        <footer>
            <Footer />
        </footer>
    </>
}

export default App;
