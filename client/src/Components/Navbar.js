import React from 'react';
import { Container, Navbar} from 'react-bootstrap';
import {TicketDetailed } from 'react-bootstrap-icons';
import { NavLink } from 'react-router-dom'

import 'bootstrap/dist/css/bootstrap.css';
import '../App.css'

const MyNavbar = () => {
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
                    <Navbar.Brand>
                        Profiles
                    </Navbar.Brand>
                    </NavLink>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export { MyNavbar };