import {Button} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import React from "react";

function ProductRow(props) {
    return (
        <>
            <tr>
                <ProductData product={props.product} />
            </tr>
        </>
    );
}

function ProductData(props) {
    return <>
        <td>{props.product.productId}</td>
        <td>{props.product.name}</td>
        <td>{props.product.brand}</td>
    </>
}

function ProfileRow(props) {
    return (
        <>
            <tr>
                <ProfileData profile={props.profile} />
                <ProfileButtons setEdit={props.setEdit} profile={props.profile}/>
            </tr>
        </>
    );
}

function ProfileData(props) {
    return <>
        <td>{props.profile.email}</td>
        <td>{props.profile.name}</td>
        <td>{props.profile.surname}</td>
    </>
}

function ProfileButtons(props) {
    const editTrue = () => {
        props.setEdit(true)
    }

    return <>
        <td>
            <NavLink to="/editProfile"> <Button variant='dark' size="sm" onClick={editTrue}>Edit</Button></NavLink>
        </td>
        <td>
            <NavLink to="/profile/tickets"> <Button variant="dark" size="sm">Tickets</Button></NavLink>
        </td>
    </>
}


function ExpertRow(props) {
    return (
        <>
            <tr>

                <ExpertData expert={props.expert} />
                <ExpertButtons setEdit={props.setEdit} expert={props.expert}/>
            </tr>
        </>
    );
}

function ExpertData(props) {
    return <>
        <td>{props.expert.email}</td>
        <td>{props.expert.name}</td>
        <td>{props.expert.surname}</td>
    </>
}
function ExpertButtons(props) {
    const editTrue = () => {
        props.setEdit(true)
    }

    return <>
        <td>
            <NavLink to="/editExpert"> <Button variant='dark' size="sm" onClick={editTrue}>Edit</Button></NavLink>
        </td>
        <td>
            <NavLink to="/expert/tickets"> <Button variant="dark" size="sm">Tickets</Button></NavLink>
        </td>
    </>
}

function TicketRow(props) {
    return (
        <>
            <tr>
                <TicketData ticket={props.ticket} />
            </tr>
        </>
    );
}

function TicketData(props) {
    return <>
        <td>{props.ticket.ticketId}</td>
        <td>{props.ticket.object}</td>
        <td>{props.ticket.argument}</td>
        <td>{props.ticket.expert.name} {props.ticket.expert.surname}</td>
        <td>{props.ticket.product.name}</td>
        <td>{props.ticket.ticketStatus}</td>
        <td>{props.ticket.messages}</td>
    </>
}

export {ProductRow, ProfileRow, ExpertRow, TicketRow};