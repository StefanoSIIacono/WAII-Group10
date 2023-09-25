import { useState } from 'react';
import { Form, Button, Stack, Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { signup } from '../utils/Api';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch } from '../store/hooks';
import { addError } from '../store/slices/errors';

export function RegisterForm() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [address, setAddress] = useState('');
  const [city, setCity] = useState('');
  const [country, setCountry] = useState('');
  const [zip, setZip] = useState('');

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const request = await signup({
      email,
      password,
      name,
      surname,
      address: {
        zipCode: zip,
        city,
        country,
        address
      }
    });
    if (!request.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: request.error!,
          errorCode: request.statusCode.toString()
        })
      );
      console.log(request.error);
      return;
    }
    navigate('/login');
  };

  return (
    <div className="fill d-flex align-items-center justify-content-center">
      <Form onSubmit={handleSubmit}>
        <Stack gap={2}>
          <h1>Sign up</h1>
          <Row className="mb-3">
            <Form.Group as={Col} controlId="name">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                value={name}
                placeholder="Name"
                onChange={(ev) => setName(ev.target.value)}
                required
              />
            </Form.Group>
            <Form.Group as={Col} controlId="surname">
              <Form.Label>Surname</Form.Label>
              <Form.Control
                type="text"
                value={surname}
                placeholder="surname"
                onChange={(ev) => setSurname(ev.target.value)}
                required
              />
            </Form.Group>
          </Row>
          <Row className="mb-3">
            <Form.Group as={Col} controlId="email">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                value={email}
                placeholder="email"
                onChange={(ev) => setEmail(ev.target.value)}
                required
              />
            </Form.Group>
            <Form.Group as={Col} controlId="password">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                value={password}
                placeholder="password"
                onChange={(ev) => setPassword(ev.target.value)}
                required
                minLength={6}
              />
            </Form.Group>
          </Row>

          <Form.Group as={Col} className="mb-3" controlId="formGridAddress1">
            <Form.Label>Address</Form.Label>
            <Form.Control
              placeholder="1234 Main St"
              value={address}
              onChange={(ev) => setAddress(ev.target.value)}
              required
            />
          </Form.Group>

          <Row className="mb-3">
            <Form.Group as={Col} controlId="formGridCity">
              <Form.Label>City</Form.Label>
              <Form.Control
                placeholder="Turin"
                value={city}
                onChange={(ev) => setCity(ev.target.value)}
                required
              />
            </Form.Group>

            <Form.Group as={Col} controlId="formGridState">
              <Form.Label>Country</Form.Label>
              <Form.Control
                placeholder="italy"
                value={country}
                onChange={(ev) => setCountry(ev.target.value)}
                required
              />
            </Form.Group>

            <Form.Group as={Col} controlId="formGridZip">
              <Form.Label>Zip</Form.Label>
              <Form.Control
                type="text"
                placeholder="12345"
                value={zip}
                onChange={(ev) => setZip(ev.target.value)}
                required
              />
            </Form.Group>
          </Row>
          <Button className="mt-2 mx-auto w-50" type="submit">
            Register
          </Button>
          <p className="text-center mt-1 m">
            You have an account? <Link to="/login">login</Link>
          </p>
        </Stack>
      </Form>
    </div>
  );
}
