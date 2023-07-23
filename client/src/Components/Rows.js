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

export {ProductRow, ProfileRow, TicketRow};