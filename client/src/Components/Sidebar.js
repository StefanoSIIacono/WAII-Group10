import React from 'react';
import { ListGroup } from 'react-bootstrap';

import 'bootstrap/dist/css/bootstrap.css';

function SidebarProd() {
    return (
        <ListGroup as="ul" className="Drawer">
            <ListGroup.Item as="li" variant="dark"> GetAll</ListGroup.Item>
            <ListGroup.Item as="li">GetFromID</ListGroup.Item>
        </ListGroup>
    )
}

function SidebarProf() {
    return (
        <ListGroup as="ul" className="Drawer">
            <ListGroup.Item as="li" variant="dark">GetFromMail</ListGroup.Item>
            <ListGroup.Item as="li">Put</ListGroup.Item>
            <ListGroup.Item as="li">Post</ListGroup.Item>
        </ListGroup>
    )
}

export { SidebarProd, SidebarProf };