import { useState } from 'react';
import { Form, Button, Stack, Row, Col } from 'react-bootstrap';
import { createExpert } from '../../utils/Api';
import { useNavigate } from 'react-router-dom';
import { SearchSelect } from '../../components/searchSelect';
import { ExpertiseDTO } from '../../types';

export function CreateExpertForm() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [expertises, setExpertises] = useState<ExpertiseDTO[]>([]);
  const [newExpertise, setNewExpertise] = useState<ExpertiseDTO | undefined>();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const request = await createExpert({
      email,
      password,
      name,
      surname,
      expertises: expertises.map((e) => e.field)
    });
    if (!request.success) {
      console.log(request.error);
      return;
    }
    navigate('/login');
  };

  return (
    <div className="fill d-flex align-items-center justify-content-center">
      <Form onSubmit={handleSubmit}>
        <Stack gap={2}>
          <h1>Create expert</h1>
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
          {expertises.map((expertise, index) => (
            <Row key={expertise.field} className="d-flex align-items-end">
              <Form.Group xs={11} as={Col} controlId="arg">
                {index === 0 && <Form.Label>Expertises</Form.Label>}
                <Form.Control type="text" value={expertise.field} disabled />
              </Form.Group>
              <Col xs={1}>
                <Button
                  onClick={() =>
                    setExpertises((curr) => curr.filter((e) => e.field !== expertise.field))
                  }
                  variant="danger"
                  className="mx-auto"
                  type="submit">
                  -
                </Button>
              </Col>
            </Row>
          ))}
          <Row className="d-flex align-items-end">
            <Form.Group xs={11} as={Col} controlId="arg">
              {expertises.length < 1 && <Form.Label>Expertises</Form.Label>}
              <SearchSelect
                type="expertises"
                onSelect={(selected) => setNewExpertise(selected as ExpertiseDTO)}
              />
            </Form.Group>
            <Col xs={1}>
              <Button
                onClick={() => {
                  if (!newExpertise) {
                    return;
                  }
                  setExpertises((curr) => [...curr, newExpertise]);
                  setNewExpertise(undefined);
                }}
                variant="success"
                disabled={!newExpertise}
                type="submit">
                +
              </Button>
            </Col>
          </Row>

          <Button className="mt-2 mx-auto w-50" type="submit">
            Create
          </Button>
        </Stack>
      </Form>
    </div>
  );
}
