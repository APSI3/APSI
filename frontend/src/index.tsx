import ReactDOM from 'react-dom/client';
import './style/index.css'
import 'bootstrap/dist/css/bootstrap.css'
import 'react-toastify/dist/ReactToastify.css';
import './style/app.css';
import App from './App';

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

root.render(
    <App />
);