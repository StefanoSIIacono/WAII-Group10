import {Col, Container, Row, Alert, Button} from "react-bootstrap";
import { SidebarProd,
    //SidebarProf,
    SidebarTic} from "./Sidebar";
import { ProductsTable, ProfileTable, ExpertTable, ProductTable, TicketsTable } from "./Tables";
import { MyNavbar } from "./Navbar";
import { Outlet } from "react-router-dom";
import {
    SignupForm,
    EditProfileForm,
    GetProfileForm,
    GetExpertForm,
    GetProductForm,
    AddTicketForm,
    LoginForm,
    AddExpertForm
} from "./Forms";
import { Box } from "./ManagerDashboardBox.js"
import {useState} from "react";

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
        <div className="TableContainer">
          <ProductsTable className="ProTable" products={props.products} />
        </div>
      </Col>
    </Row>
    </div>
  );
}

function GetProductPage(props) {
  return (
    <div><Row>
      <Col xs={3} className="Menu">
        <SidebarProd />
      </Col>
      <Col className="ProTitle" xs={8}>
        <h1>Product:</h1>
        <h2>Insert the product id:</h2>
        <GetProductForm readProductByID={props.readProductByID} />
        <ProductTable product={props.product} />
      </Col>
    </Row>
    </div>);
}

function ProfilesPage(props) {

    const [email, setEmail] = useState('');

    const changeEmail = (email) => {
        setEmail(email)
    }

    const handleClean = () => {
        props.setEdit(false)
        props.readProfileByMail(null)
        setEmail('')
    }

  return (
    <div>
      <Row>
        <Col className="ProTitle" xs={8}>
          <h1>Profiles:</h1>
          <h2>Insert the profile email:</h2>
          <GetProfileForm readProfileByMail={props.readProfileByMail} setEdit={props.setEdit}
                          setProfile={props.setProfile} email={email} changeEmail={changeEmail}/>
          <Button align='right' variant='danger' onClick={handleClean}>Clean</Button>
          <ProfileTable profiles={props.profiles} profile={props.profile} setEdit={props.setEdit}/>
        </Col>

      </Row>
    </div>
  );
}

function ExpertsPage(props) {

    const [email, setEmail] = useState('');

    const changeEmail = (email) => {
        setEmail(email)
    }

    const handleClean = () => {
        props.setEdit(false)
        props.readExpertByMail(null)
        setEmail('')
    }

    return (
        <div>
            <Row>
                <Col className="ExpTitle" xs={8}>
                    <h1>Experts:</h1>
                    <h2>Insert the expert email:</h2>
                    <GetExpertForm readExpertByMail={props.readExpertByMail} setEdit={props.setEdit}
                                    setExpert={props.setExpert} email={email} changeEmail={changeEmail}/>
                    <Button align='right' variant='danger' onClick={handleClean}>Clean</Button>
                    <ExpertTable experts={props.experts} expert={props.expert} setEdit={props.setEdit}/>
                </Col>

            </Row>
        </div>
    );
}

function SignupPage(props) {
  return (
    <Row>
      <Col>
        <div>
          <h1 className="ProTitle">Sign up</h1>
          <SignupForm
            signup={props.signup}
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
          <h1 className="ProTitle">Edit profile</h1>
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

function TicketsPage(props) {
  return (
    <div><Row>
      <Col xs={3} className="Menu">
        <SidebarTic />
      </Col>
      <Col className="ProTitle" xs={8}>
        <h1>Tickets:</h1>
        <div className="TableContainer">
          <TicketsTable className="ProTable" tickets={props.tickets} />
        </div>
      </Col>
    </Row>
    </div>
  );
}

function CreateTicketPage(props) {
  return (
    <Row>
      <Col>
        <div>
          <h1 className="ProTitle">Add ticket</h1>
          <AddTicketForm
            addTicket={props.addTicket}
          />
        </div>
      </Col>
    </Row>
  );
}

// NUOVO
function CreateExpertPage(props) {
    return (
        <Row>
            <Col>
                <div>
                    <h1 className="ProTitle">Add expert</h1>
                    <AddExpertForm
                        addExpert={props.addExpert}
                        expertises={props.expertises}
                    />
                </div>
            </Col>
        </Row>
    );
}

// NUOVO
function ManagerDashboard(props){
    return (
        <div>
            <h1 style={{color: 'white'}}>Dashboard</h1>
            <div className="box-container">
                <Box title="Homepage" linkTo="/manager" />
                <Box title="Manage Experts" linkTo="/manager/experts"/>
            </div>
            <div className="box-container">
                <Box title="Manage Tickets" linkTo="/tickets"/>
                <Box title="Manage Profiles" linkTo="/profiles"/>
            </div>
        </div>
    );
}

function LoginPage(props) {
  return (
    <Container className="Auth-form-container">
      <Row className="align-items-center Auth-form" class="fill">
        <Col md={{ span: 6, offset: 3 }}><LoginForm login={props.login} /></Col>
      </Row>
    </Container>
  );
}

function AppLayout(props) {
  return (
    <div>
      <MyNavbar loggedIn={props.loggedIn} handleLogout={props.handleLogout} text="Login" />
      <div style={{
        backgroundImage: `url("../background.jpg")`,
        backgroundPosition: 'center',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        width: '100vw',
        height: '90vh'
      }}>
        <div class="flex-container">
          {props.message && <Row>
            <Alert variant={props.message.type} onClose={() => props.setMessage('')} dismissible>{props.message.msg}</Alert>
          </Row>}
          <Outlet />
        </div>
      </div>
    </div>
  )
}
export {
    AppLayout,
    HomePage,
    ProfilesPage,
    ExpertsPage,
    ProductsPage,
    SignupPage,
    EditProfilePage,
    GetProductPage,
    TicketsPage,
    CreateTicketPage,
    CreateExpertPage,
    ManagerDashboard,
    LoginPage
}