import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AppLayout } from './pages/Layout';
import './App.css';
import { Home } from './pages/profile/Home';
import { LoginForm } from './pages/Login';
import { RegisterForm } from './pages/Register';
import { useAppDispatch, useAppSelector } from './store/hooks';
import { useEffect } from 'react';
import { checkAuthentication } from './store/slices/authentication';
//import { TicketPage } from './pages/Ticket';
import { CreateTicketForm } from './pages/profile/createTicket';

function App() {
  const dispatch = useAppDispatch();
  const { authenticated } = useAppSelector((state) => state.authenticate);

  useEffect(() => {
    dispatch(checkAuthentication());
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        {!authenticated ? (
          <Route element={<AppLayout />}>
            <Route path="/*" element={<Navigate to="/dashboard" />} />
            <Route path="/tickets/new" element={<CreateTicketForm />} />
            <Route path="/dashboard" element={<Home />} />
          </Route>
        ) : (
          <>
            <Route path="/*" element={<Navigate to="/login" />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
          </>
        )}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
