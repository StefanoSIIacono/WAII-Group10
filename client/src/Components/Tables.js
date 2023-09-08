import { Table, Col, Row} from "react-bootstrap";
import { ProductRow, ProfileRow, ExpertRow, TicketRow} from "./Rows";

function ProductsTable(props) {

  return <>
    <Table responsive hover className="TableStuff" >
          <h2><Row>
            <Col>Ean</Col>
            <Col>Name</Col>
            <Col>Brand</Col>
          </Row>
          </h2>
        <div className="ScrollB">
          <tbody >
            {props.products.map((p) => (<ProductRow classname="RowStyle" key={p.productId} product={p} />))}
          </tbody>
        </div>
    </Table>
  </>
}

function ProductTable(props) {
  return <>
    <Table responsive className="TableStuff">
      <thead>
        <tr>
          <th>Ean</th>
          <th>Name</th>
          <th>Brand</th>
        </tr>
      </thead>
      <tbody>
        <ProductRow classname="RowStyle" key={props.product.productId} product={props.product} />
      </tbody>
    </Table>
  </>
}

function ProfileTable(props) {

  return <>
    <Table responsive className="TableStuff">
      <thead>
        <tr>
          <th>Email</th>
          <th>Name</th>
          <th>Surname</th>
        </tr>
      </thead>
      <tbody>
      {!props.profile ? props.profiles.map((p) => (<ProfileRow classname="RowStyle" key={p.email} profile={p} />)) :
          <ProfileRow classname="RowStyle" key={props.profile.email} profile={props.profile} setEdit={props.setEdit} />
      }
      </tbody>
    </Table>
  </>
}

function ExpertTable(props) {

  return <>
    <Table responsive className="TableStuff">
      <thead>
      <tr>
        <th>Email</th>
        <th>Name</th>
        <th>Surname</th>
      </tr>
      </thead>
      <tbody>
      {!props.expert ? props.experts.map((e) => (<ExpertRow classname="RowStyle" key={e.email} expert={e} />)) :
          <ExpertRow classname="RowStyle" key={props.expert.email} expert={props.expert} setEdit={props.setEdit} />
      }
      </tbody>
    </Table>
  </>
}

function TicketsTable(props) {
  return <>
    <Table responsive hover className="TableStuff" >
          <h2><Row>
            <Col>ID</Col>
            <Col>Object</Col>
            <Col>Argument</Col>
            <Col>Object</Col>
            <Col>Expert</Col>
            <Col>Product</Col>
            <Col>Priority</Col>
            <Col>Status</Col>
            <Col>Messages</Col>
          </Row>
          </h2>
        <div className="ScrollB">
          <tbody >
            {props.tickets.map((p) => (<TicketRow classname="RowStyle" key={props.ticket.ticketI} ticket={props.ticket} />))}
          </tbody>
        </div>
    </Table>
  </>
}


export { ProductsTable, ProfileTable, ExpertTable, ProductTable, TicketsTable};