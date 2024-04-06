import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider } from "react-router-dom";
import Footer from "./components/Footer";
import { UnAuthorizedPage } from "./pages/UnauthorizedPage";
import { AuthorizedPage } from "./pages/AuthorizedPage";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import EventsPage from "./pages/EventsPage";
import EventPage from "./pages/EventPage";
import LocationsPage from "./pages/LocationsPage";
import LocationPage from "./pages/LocationPage";
import RegistrationPage from "./pages/RegistrationPage";
import TicketsPage from "./pages/TicketsPage";
import UsersPage from "./pages/UsersPage";
import ApplicationsPage from "./pages/ApplicationsPage";


export const Paths = {
    login: "/login",
    register: "/register",
    main: "/",
    createEvent: "/createEvent",
    events: "/events",
    event: "/event/:eventId",
    tickets: "/tickets",
    locations: "/locations",
    location: "/location/:locationId",
    applications: "/applications",
    users: "/users",
}

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route>
            <Route path={Paths.login} element={<UnAuthorizedPage page={<LoginPage />} />} />
            <Route path={Paths.register} element={<UnAuthorizedPage page={<RegistrationPage />} />} />
            <Route path={Paths.main} element={<AuthorizedPage page={<MainPage />} />} />
            <Route path={Paths.events} element={<AuthorizedPage page={<EventsPage />} />} />
            <Route path={Paths.tickets} element={<AuthorizedPage page={<TicketsPage />} />} />
            <Route path={Paths.event} element={<AuthorizedPage page={<EventPage />} />} />
            <Route path={Paths.locations} element={<AuthorizedPage page={<LocationsPage />} />} />
            <Route path={Paths.location} element={<AuthorizedPage page={<LocationPage />} />} />
            <Route path={Paths.applications} element={<AuthorizedPage page={<ApplicationsPage />} />} />
            <Route path={Paths.users} element={<AuthorizedPage page={<UsersPage />} />} />
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
