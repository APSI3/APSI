import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider } from "react-router-dom";
import Footer from "./components/Footer";
import { UnAuthorizedPage } from "./pages/UnauthorizedPage";
import { AuthorizedPage } from "./pages/AuthorizedPage";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import CreateEventPage from "./pages/CreateEventPage";

export const Paths = {
    login: "/login",
    main: "/",
    createEvent: "/createevent"
}

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route>
            <Route path={Paths.login} element={<UnAuthorizedPage page={<LoginPage />} />} />
            <Route path={Paths.main} element={<AuthorizedPage page={<MainPage />} />} />
            <Route path={Paths.createEvent} element={<AuthorizedPage page={<CreateEventPage />} />} />
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
