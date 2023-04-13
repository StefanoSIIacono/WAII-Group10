import { Table } from "react-bootstrap";
import {ProductRow, ProfileRow} from "./Rows";

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
        {props.products.map((p) => (<ProductRow classname="RowStyle" key={p.productId} product={p} />))}
      </tbody>
    </Table>
  </>
}

function ProfilesTable(props) {
  return <>
    <Table responsive className="TableStuff">
      <thead>
      <tr>
        <th>PROFILE ID</th>
        <th>EMAIL</th>
        <th>PASSWORD</th>
      </tr>
      </thead>
      <tbody>
      {props.profiles.map((p) => (<ProfileRow key={p.profileID} profile={p} />))}
      </tbody>
    </Table>
  </>
}


export {ProductTable, ProfilesTable};