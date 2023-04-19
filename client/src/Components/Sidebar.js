import React from 'react';
import { ListGroup } from 'react-bootstrap';
import { NavLink } from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.css';


function SidebarProd(props) {
    return (
        <ListGroup as="ul" className="Drawer">
            <NavLink to={'/products'}><ListGroup.Item as="li" variant="dark" > GetAll</ListGroup.Item></NavLink>
            <NavLink to={'/getProduct'}><ListGroup.Item as="li">GetFromID</ListGroup.Item></NavLink>
        </ListGroup>
    )
}

function SidebarProf(props) {
    return (
        <ListGroup as="ul" className="Drawer">
            <ListGroup.Item as="li" variant="dark">GetFromMail</ListGroup.Item>
            { props.edit && <NavLink to={'/editProfile'}><ListGroup.Item as="li">Put</ListGroup.Item></NavLink>}
            <NavLink to={'/addProfile'}><ListGroup.Item as="li" >Post</ListGroup.Item></NavLink>
        </ListGroup>
    )
}

export { SidebarProd, SidebarProf };