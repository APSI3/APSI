import { BrowserRouter, Routes, Route } from "react-router-dom";
import UserPage from './pages/UserPage';
import './App.css';
import CreateEventPage from "./pages/CreateEventPage";



function App() {
  return (
    <div className="App">
      <header className="App-header">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<UserPage />} />
            <Route path="/user" element={<UserPage />} />
            <Route path="/createEvent" element={<CreateEventPage />} />
          </Routes>
        </BrowserRouter>
      </header>
    </div>
  );
}

export default App;
