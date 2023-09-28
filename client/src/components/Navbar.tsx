import { Container, Nav, Navbar, Button } from 'react-bootstrap';
import { TicketDetailed } from 'react-bootstrap-icons';
import { NavLink } from 'react-router-dom';
import { useAppDispatch } from '../store/hooks';
import { logoutUser } from '../store/slices/authentication';
import { useAppSelector } from '../store/hooks';
import { Roles } from '../types';

export function NavBar() {
  const { user } = useAppSelector((state) => state.authenticate);
  const dispatch = useAppDispatch();

  const userRole = user?.role;

  return (
    <>
      <Navbar className="justify-content-between" bg="dark" data-bs-theme="dark">
        <Container>
          <NavLink to="/" className="navItem">
            <Navbar.Brand>
              <TicketDetailed color="white" style={{ marginRight: '10px' }} size={32} />
              Ticket Service
            </Navbar.Brand>
          </NavLink>
          <Nav>
            {userRole === Roles.PROFILE && (
              <NavLink to="/profile" className="ms-2">
                <Button variant="outline-light" size="sm">
                  Profile
                </Button>
              </NavLink>
            )}
            {userRole === Roles.MANAGER && (
              <NavLink to="/experts" className="ms-2">
                <Button variant="outline-light" size="sm">
                  Experts
                </Button>
              </NavLink>
            )}
            <Button
              onClick={() => dispatch(logoutUser())}
              variant="outline-light"
              size="sm"
              className="ms-2">
              Logout
            </Button>
          </Nav>
        </Container>
      </Navbar>
    </>
  );
}
