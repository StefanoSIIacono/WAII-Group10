import React from 'react';
import { Link } from 'react-router-dom';
import {Col} from "react-bootstrap";

function Box ({title, linkTo}) {
    return (
        <Col md={6} className="box-container">
            <div className="box">
                <Link to={linkTo}><h3>{title}</h3></Link>
            </div>
        </Col>
    );
};

export  { Box };

/* COSA PUO' FARE IL MANAGER:
    Profiles:
        getAll
        getProfileByEmail
    Expert:
        getAll
        getById
        addExpertise
    Expertise:
        getAll
        getExpertise
        getExpertsByExpertise
        createExpertise
        deleteExpertise
    Product:
        getAll
        getProduct
    Security:
        createExpert
        login
    Ticket:
        getAll
        getTicketById
        openTicket
        inProgressTicket
        setTicketPriority
*/