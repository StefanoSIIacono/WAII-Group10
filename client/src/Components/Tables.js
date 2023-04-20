import { Table, Col, Row} from "react-bootstrap";
import { ProductRow, ProfileRow } from "./Rows";

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
        <ProfileRow key={props.profile.email} profile={props.profile} />
      </tbody>
    </Table>
  </>
}


export { ProductsTable, ProfileTable, ProductTable };