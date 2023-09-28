import { Outlet } from 'react-router-dom';
import { NavBar } from '../components/Navbar';
import { ErrorDisplay } from '../components/ErrorDisplay';

export function AppLayout() {
  return (
    <div style={{ height: '100vh' }}>
      <NavBar />
      <ErrorDisplay />
      <Outlet />
    </div>
  );
}
