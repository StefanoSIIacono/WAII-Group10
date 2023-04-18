import { Col, Container, Row} from "react-bootstrap";
import { SidebarProd, SidebarProf } from "./Sidebar";
import { ProductsTable, ProfileTable, ProductTable } from "./Tables";
import { MyNavbar } from "./Navbar";
import { Outlet } from "react-router-dom";
import { AddProfileForm, EditProfileForm, GetProfileForm, GetProductForm } from "./Forms";
import {useEffect} from "react";


function HomePage() {
  return (<Container className="Home">
    <h1>Welcome to our</h1>
    <p>Ticket Service Application</p>
  </Container>
  )
}

function ProductsPage(props) {
  return (
    <div><Row>
      <Col xs={3} className="Menu">
        <SidebarProd />
      </Col>
      <Col className="ProTitle" xs={8}>
        <h1>Products:</h1>
        <ProductsTable products={props.products} />
      </Col>
    </Row>
    </div>
  );
}
function GetProductPage(props){
    useEffect(() => {
        props.setProduct([])
    }, []);
    return(
    <Col className="ProTitle" xs={8}>
        <h1>Product:</h1>
        <h2>Insert the product id:</h2>
        <GetProductForm readProductByID={props.readProductByID}  />
        <ProductTable product={props.product} />
    </Col>)
}

function ProfilesPage(props) {

  return (
    <div>
      <Row>
        <Col xs={3} className="Menu">
          <SidebarProf edit={props.edit} setEdit={props.setEdit} />
        </Col>
        <Col className="ProTitle" xs={8}>
          <h1>Profile:</h1>
          <h2>Insert the profile email:</h2>
          <GetProfileForm readProfileByMail={props.readProfileByMail} setEdit={props.setEdit} />
          <ProfileTable profile={props.profile} />
        </Col>
      </Row>
    </div>
  );
}

function AddProfilePage(props) {
  return (
    <Row>
      <Col>
        <div>
          <h1>Add profile</h1>
          <AddProfileForm
            addProfile={props.addProfile}
          />
        </div>
      </Col>
    </Row>
  );
}


function EditProfilePage(props) {
  return (
    <Row>
      <Col>
        <div>
          <h1>Edit profile</h1>
          {!props.loading && <EditProfileForm
            key={props.profile.email}
            edit={props.edit}
            setEdit={props.setEdit}
            profile={props.profile}
            editProfile={props.editProfile}
          />}
        </div>
      </Col>
    </Row>
  );
}

function AppLayout() {
  return (
    <div>
      <MyNavbar />
      <div style={{
        backgroundImage: `url("../background.jpg")`,
        backgroundPosition: 'center',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        width: '100vw',
        height: '90vh'
      }}>
        <Container className="Content">
          <Outlet />
        </Container>
      </div>
    </div>
  )
}
export { AppLayout, HomePage, ProfilesPage, ProductsPage, AddProfilePage, EditProfilePage, GetProductPage }