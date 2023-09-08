import { useState } from "react";
import { Button, Form, Row, Col } from 'react-bootstrap';
import { Profile } from "./Profile";
import { useNavigate, NavLink } from "react-router-dom";
import { Expert } from "./Expert";

function GetProfileForm(props) {

    const handleSubmit = (event) => {
        event.preventDefault();
        props.readProfileByMail(props.email);
        props.setEdit(true);
      }

    return <>
        <Form onSubmit={handleSubmit}>
            <Form.Group className='mb-3'>
                <Form.Control type='email' value={props.email} placeholder="Email"
                              onChange={(event) => { props.changeEmail(event.target.value)}} />
            </Form.Group>
            <div align='left'>
                <Button type='submit' variant='dark' >Get</Button>
            </div>
        </Form>
    </>
}

function GetProductForm(props){
    const [id, setId] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        props.readProductByID(id);
    }

    return <>
        <Form onSubmit={handleSubmit}>
            <Form.Group className='mb-3'>
                <Form.Control type='text' value={id} required={true} placeholder="Id" onChange={(event) => { setId(event.target.value) }} />
            </Form.Group>
            <div align='right'>
                <Button type='submit' variant='dark'>Get</Button>
            </div>
        </Form>
    </>
}
function EditProfileForm(props) {
    let navigate = useNavigate();
    const [name, setName] = useState(props.profile.name);
    const [surname, setSurname] = useState(props.profile.surname);


    const handleSubmit = (event) => {
        event.preventDefault();
        props.editProfile(new Profile(props.profile.email, name, surname));
        props.setEdit(false);
        navigate('/profiles');
    }

    return <>
        <div style={{ padding: 10}} class="FontText" >
            <Form onSubmit={handleSubmit}>
                <Form.Group className='mb-3'>
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} required={true} placeholder="Name" onChange={(event) => { setName(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} required={true} placeholder="Surname" onChange={(event) => { setSurname(event.target.value) }} />
                </Form.Group>
                <div align='right'>
                    <NavLink to='/profiles'><Button variant='secondary' onClick={() => props.setEdit(false)}>Cancel</Button></NavLink> &nbsp;
                    <Button type='submit' variant='dark'>Save</Button>
                </div>
            </Form>
        </div>
    </>

}


function SignupForm(props) {
    let navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [password, setPassword] = useState('');
    const [address, setAddress] = useState('')

    const handleSubmit = (event) => {
        event.preventDefault();
        props.signup(new Profile(email, name, surname), password);

        setEmail('');
        setName('');
        setSurname('');
        setPassword('');
        navigate('/');

    }

    return <>
        <div style={{ padding: 10 }} class="FontText">
            <Form onSubmit={handleSubmit}>
                <Form.Group className='mb-3'>
                    <Form.Label>Email</Form.Label>
                    <Form.Control type='email' value={email} required={true} placeholder="Email" onChange={(event) => { setEmail(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} required={true} placeholder="Name" onChange={(event) => { setName(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} required={true} placeholder="Surname" onChange={(event) => { setSurname(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Password</Form.Label>
                    <Form.Control type='text' value={password} required={true} placeholder="Password" onChange={(event) => { setPassword(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Address</Form.Label>
                    <Form.Control type='text' value={address} required={true} placeholder="Address" onChange={(event) => { setAddress(event.target.value) }} />
                </Form.Group>
                <div align='right'>
                    <NavLink to='/'><Button variant='secondary'>Cancel</Button></NavLink> &nbsp;
                    <Button type='submit' variant='dark'>Add</Button>
                </div>
            </Form></div>
    </>

}

function AddTicketForm(props) {
    let navigate = useNavigate();
    const [object, setObject] = useState('');
    const [argument, setArgument] = useState('');
    const [productEan, setProductEan] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        props.addTicket()//new Ticket(email, name, surname));

        setObject('');
        setArgument('');
        setProductEan('');
        navigate('/tickets');

    }

    return <>
        <div style={{ padding: 10 }} class="FontText">
            <Form onSubmit={handleSubmit}>
                <Form.Group className='mb-3'>
                    <Form.Label>object</Form.Label>
                    <Form.Control type='text' value={object} required={true} placeholder="Object" onChange={(event) => { setObject(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Argument</Form.Label>
                    <Form.Control type='text' value={argument} required={true} placeholder="Argument" onChange={(event) => { setArgument(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>ProductEan</Form.Label>
                    <Form.Control type='text' value={productEan} required={true} placeholder="ProductEan" onChange={(event) => { setProductEan(event.target.value) }} />
                </Form.Group>
                <div align='right'>
                    <NavLink to='/tickets'><Button variant='secondary'>Cancel</Button></NavLink> &nbsp;
                    <Button type='submit' variant='dark'>Add</Button>
                </div>
            </Form></div>
    </>

}

function GetExpertForm(props) {

    const handleSubmit = (event) => {
        event.preventDefault();
        props.readExpertByMail(props.email);
        props.setEdit(true);
    }

    return <>
        <Form onSubmit={handleSubmit}>
            <Form.Group className='mb-3'>
                <Form.Control type='email' value={props.email} placeholder="Email"
                              onChange={(event) => { props.changeEmail(event.target.value)}} />
            </Form.Group>
            <div align='left'>
                <Button type='submit' variant='dark' >Get</Button>
            </div>
        </Form>
    </>
}

// NUOVO
function AddExpertForm(props) {
    let navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [selected, setSelected] = useState([]);

    // Adds expertises or delete according to checkboxes
    const handleCheckboxChange = (value) => {
        if (selected.includes(value)) {
            setSelected(selected.filter((val) => val !== value));
        } else {
            setSelected([...selected, value]);
        }
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        props.addExpert(new Expert(email, password, name, surname, selected))

        navigate('/manager');
    }

    return <>
        <div style={{ padding: 10 }} class="FontText">
            <Form onSubmit={handleSubmit}>
            <Form.Group className='mb-3'>
                    <Form.Label>Email</Form.Label>
                    <Form.Control type='email' value={email} required={true} placeholder="Email" onChange={(event) => { setEmail(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Password</Form.Label>
                    <Form.Control type='text' value={password} required={true} placeholder="Password" onChange={(event) => { setPassword(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} required={true} placeholder="Name" onChange={(event) => { setName(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} required={true} placeholder="Surname" onChange={(event) => { setSurname(event.target.value) }} />
                </Form.Group>
                <Form.Group className='mb-3'>
                    <Form.Label>Expertises:</Form.Label>
                    {props.expertises.map((e) => (
                        <div key={e.field}>
                            <Form.Check
                                type="checkbox"
                                label={e.field}
                                checked={props.expertises.includes(e)}
                                onChange={() => handleCheckboxChange(e)}
                            />
                        </div>
                    ))}
                </Form.Group>
                <div align='right'>
                    <NavLink to='/manager'><Button variant='secondary'>Cancel</Button></NavLink> &nbsp;
                    <Button type='submit' variant='dark'>Add</Button>
                </div>
            </Form>
        </div>
    </>
}


function LoginForm(props) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
  
    const handleSubmit = (event) => {
      event.preventDefault();
      const credentials = { username, password };
  
      props.login(credentials);
    };
  
    return (
      <Form onSubmit={handleSubmit}>
        <div className="d-grid gap-4">
          <h1>Login</h1>
          <Form.Group controlId='username'>
            <Form.Label>Username</Form.Label>
            <Form.Control type='text' value={username} onChange={ev => setUsername(ev.target.value)} required={true} />
          </Form.Group>
  
          <Form.Group controlId='password'>
            <Form.Label>Password</Form.Label>
            <Form.Control type='password' value={password} onChange={ev => setPassword(ev.target.value)} required={true} minLength={6} />
          </Form.Group>
  
          <Button type="submit">Login</Button>
  
        </div>
  
      </Form>
    )
  };
  
function LogoutButton(props) {
    return (
      <Row>
        <Col>
        <Button variant="outline-light" onClick={props.logout}>Logout</Button>
        </Col>
      </Row>
    )
}

export {
    SignupForm,
    EditProfileForm,
    GetProfileForm,
    GetExpertForm,
    GetProductForm,
    AddTicketForm,
    AddExpertForm,
    LoginForm,
    LogoutButton};