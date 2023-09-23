import { Outlet } from 'react-router-dom';
import { NavBar } from '../components/Navbar';

export function AppLayout() {
  return (
    <div style={{ height: '100vh' }}>
      <NavBar />
      <Outlet />
    </div>
  );
}
