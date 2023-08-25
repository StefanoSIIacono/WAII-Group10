import React from 'react';
import { ListGroup } from 'react-bootstrap';
import { NavLink } from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.css';


function SidebarProd(props) {
    return (
        <ListGroup as="ul" className="Drawer">
            <NavLink to={'/products'}  className="Drawer-li"><ListGroup.Item as="li" variant="dark" > GetAll</ListGroup.Item></NavLink>
            <NavLink to={'/getProduct'}  className="Drawer-li"><ListGroup.Item as="li">GetFromID</ListGroup.Item></NavLink>
        </ListGroup>
    )
}

function SidebarProf(props) {
    return (
        <ListGroup as="ul" className="Drawer">
            <ListGroup.Item as="li" variant="dark">GetFromMail</ListGroup.Item>
            { props.edit && <NavLink to={'/editProfile'}  className="Drawer-li"><ListGroup.Item as="li">Put</ListGroup.Item></NavLink>}
        </ListGroup>
    )
}

function SidebarTic(props) {
    return (
        <ListGroup as="ul" className="Drawer">
           <ListGroup as="ul" className="Drawer">
            <NavLink to={'/tickets'}  className="Drawer-li"><ListGroup.Item as="li" variant="dark" > GetAll</ListGroup.Item></NavLink>
            <NavLink to={'/createTicket'}  className="Drawer-li"><ListGroup.Item as="li">CreateTicket</ListGroup.Item></NavLink>
        </ListGroup>
        </ListGroup>
    )
}

export { SidebarProd, SidebarProf, SidebarTic};