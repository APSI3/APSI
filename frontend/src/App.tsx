import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider } from "react-router-dom";
import Footer from "./components/Footer";
import { UnAuthorizedPage } from "./pages/UnauthorizedPage";
import { AuthorizedPage } from "./pages/AuthorizedPage";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import EventsPage from "./pages/EventsPage";
import EventPage from "./pages/EventPage";
import LocationsPage from "./pages/LocationsPage";


export const Paths = {
    login: "/login",
    main: "/",
    createEvent: "/createEvent",
    events: "/events",
    event: "/event/:eventId",
    locations: "/locations",
}

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route>
            <Route path={Paths.login} element={<UnAuthorizedPage page={<LoginPage />} />} />
            <Route path={Paths.main} element={<AuthorizedPage page={<MainPage />} />} />
            <Route path={Paths.events} element={<AuthorizedPage page={<EventsPage />} />} />
            <Route path={Paths.event} element={<AuthorizedPage page={<EventPage />} />} />
            <Route path={Paths.locations} element={<AuthorizedPage page={<LocationsPage />} />} />
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
