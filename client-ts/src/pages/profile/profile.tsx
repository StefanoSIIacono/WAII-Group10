import { useState } from 'react';
import { Form, Button, Stack, Row, Col } from 'react-bootstrap';
import { editProfile } from '../../utils/Api';
import { useAppDispatch, useAppSelector } from '../../store/hooks';
import { checkAuthentication } from '../../store/slices/authentication';

export function UserProfile() {
  const dispatch = useAppDispatch();
  const { user } = useAppSelector((state) => state.authenticate);
  const [email] = useState(user?.email);
  const [name, setName] = useState(user?.name);
  const [surname, setSurname] = useState(user?.surname);
  const [address, setAddress] = useState(user?.address!.address);
  const [city, setCity] = useState(user?.address!.city);
  const [country, setCountry] = useState(user?.address!.country);
  const [zip, setZip] = useState(user?.address!.zipCode);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const request = await editProfile({
      name,
      surname,
      address: {
        zipCode: zip!,
        city: city!,
        country: country!,
        address: address!
      }
    });
    if (!request.success) {
      console.log(request.error);
      return;
    }
    dispatch(checkAuthentication());
  };

  const hasBeenModified =
    name !== user?.name ||
    surname !== user?.surname ||
    address !== user?.address?.address ||
    city !== user?.address?.city ||
    country !== user?.address?.country ||
    zip !== user?.address?.zipCode;

  return (
    <div className="fill d-flex align-items-center justify-content-center">
      <Form onSubmit={handleSubmit}>
        <Stack gap={2}>
          <h1>My profile</h1>
          <Row className="mb-3">
            <Form.Group as={Col} controlId="email">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" value={email} placeholder="email" disabled />
            </Form.Group>
          </Row>
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
          <Button disabled={!hasBeenModified} className="mt-2 mx-auto w-50" type="submit">
            Save
          </Button>
        </Stack>
      </Form>
    </div>
  );
}
