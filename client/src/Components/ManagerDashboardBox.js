import React from 'react';
import { Link } from 'react-router-dom';
import {Col} from "react-bootstrap";

function Box ({title, linkTo}) {
    return (
        <Col md={6} className="box-container">
            <div className="box">
                <h3>{title}</h3>
                <Link to={linkTo}>Vai alla pagina</Link>
            </div>
        </Col>
    );
};

export  { Box };