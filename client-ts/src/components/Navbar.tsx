import { Container, Nav, Navbar, Button } from 'react-bootstrap';
import { TicketDetailed } from 'react-bootstrap-icons';
import { NavLink } from 'react-router-dom';
//import { useAppSelector } from '../store/hooks';

export function NavBar() {
  //const { user } = useAppSelector((state) => state.authenticate);

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
          {/*
          <Nav className="me-auto">
            <Nav.Link href="#home">Products</Nav.Link>
          </Nav>
          */}
          <Nav>
            <NavLink to="/user" className="ms-2">
              <Button variant="outline-light" size="sm">
                Profile
              </Button>
            </NavLink>
          </Nav>
        </Container>
      </Navbar>
    </>
  );
}
