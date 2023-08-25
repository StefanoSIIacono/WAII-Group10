import React from 'react';
import { Container, Navbar, Button} from 'react-bootstrap';
import {TicketDetailed } from 'react-bootstrap-icons';
import { NavLink } from 'react-router-dom'
import {LogoutButton} from './Forms'

import 'bootstrap/dist/css/bootstrap.css';
import '../App.css'

// TODO: if manager is logged in, the home button redirect to the dashboard
const MyNavbar = (props) => {
    return (
        <Navbar className="Navbar" collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container fluid>
                <NavLink to='/' className='navItem' >
                    <Navbar.Brand >
                        <TicketDetailed color="white" style={{ marginRight: '10px'}} size={32} />
                        Ticket Service
                    </Navbar.Brand>
                </NavLink>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse className="justify-content-end" style={{marginRight: '5vh'}}>
                    <NavLink to='/' className='navItem' >
                        <Navbar.Brand style={{marginRight: '10vh'}}>
                            Home
                        </Navbar.Brand>
                    </NavLink>
                    <NavLink to='/products' className='navItem' >
                    <Navbar.Brand style={{marginRight: '10vh'}}>
                        Products
                    </Navbar.Brand>
                    </NavLink>
                    <NavLink to='/profiles' className='navItem' >
                    <Navbar.Brand style={{marginRight: '10vh'}}>
                        Profiles
                    </Navbar.Brand>
                    </NavLink>
                    <NavLink to='/tickets' className='navItem' >
                    <Navbar.Brand>
                        Tickets
                    </Navbar.Brand>
                    </NavLink>
                    <Navbar.Text>
                        {props.loggedIn ? <LogoutButton logout={props.handleLogout} /> : (
                            <>
                            <NavLink to="/signup"> <Button variant="outline-light" size="lg">Signup</Button></NavLink>
                            <NavLink to="/login"><Button variant="outline-light" size="lg">{props.text}</Button></NavLink>
                        </>
                        )}
                    </ Navbar.Text>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export { MyNavbar };