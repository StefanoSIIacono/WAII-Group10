import {Col, Container, Row} from "react-bootstrap";
import {SidebarProd, SidebarProf} from "./Sidebar";
import {ProductTable, ProfilesTable} from "./Tables";
import {MyNavbar} from "./Navbar";
import {Outlet} from "react-router-dom";

function HomePage(){
    return(<Container  className="Home">
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
                <h1>Products: filter</h1>
                <ProductTable products={props.products}/>
            </Col>
        </Row>
        </div>
    );
}

function ProfilesPage(props) {
    return (
        <div>
            <Row>
                <Col xs={3} className="Menu">
                    <SidebarProf />
                </Col>
                <Col className="ProTitle" xs={8}>
                    <h1>Profiles:</h1>
                    <ProfilesTable profiles={props.profiles}/>
                </Col>
            </Row>
        </div>
    );
}


function AppLayout() {
    return (
        <div>
            <MyNavbar />
            <div style={{backgroundImage: `url("../background.jpg")`,
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
export {AppLayout, HomePage, ProfilesPage, ProductsPage}