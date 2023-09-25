import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AppLayout } from './pages/Layout';
import './App.css';
import { Home } from './pages/profile/Home';
import { LoginForm } from './pages/Login';
import { RegisterForm } from './pages/Register';
import { useAppDispatch, useAppSelector } from './store/hooks';
import { useEffect } from 'react';
import { checkAuthentication } from './store/slices/authentication';
import { CreateTicketForm } from './pages/profile/createTicket';
import { UserProfile } from './pages/profile/profile';
import { ExpertsDashboard } from './pages/manager/expertsDashboard';
import { CreateExpertForm } from './pages/manager/createExpert';

function App() {
  const dispatch = useAppDispatch();
  const { authenticated, loading } = useAppSelector((state) => state.authenticate);

  useEffect(() => {
    dispatch(checkAuthentication());
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        {!loading ? (
          authenticated ? (
            <Route element={<AppLayout />}>
              <Route path="/*" element={<Navigate to="/dashboard" />} />
              <Route path="/tickets/new" element={<CreateTicketForm />} />
              <Route path="/dashboard" element={<Home />} />
              <Route path="/profile" element={<UserProfile />} />
              <Route path="/experts" element={<ExpertsDashboard />} />
              <Route path="/experts/new" element={<CreateExpertForm />} />
              <Route path="/experts/edit/:id" element={<CreateExpertForm />} />
            </Route>
          ) : (
            <>
              <Route path="/*" element={<Navigate to="/login" />} />
              <Route path="/login" element={<LoginForm />} />
              <Route path="/register" element={<RegisterForm />} />
            </>
          )
        ) : null}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
