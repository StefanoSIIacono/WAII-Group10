import { useRef, useState } from 'react';
import { Form, Button, Stack, Row, Col } from 'react-bootstrap';
import { addExpertiseToExpert, createExpert, deleteExpertiseFromExpert } from '../../utils/Api';
import { useNavigate, useParams } from 'react-router-dom';
import { SearchSelect } from '../../components/searchSelect';
import { ExpertiseDTO } from '../../types';
import Typeahead from 'react-bootstrap-typeahead/types/core/Typeahead';
import { Plus, Trash } from 'react-bootstrap-icons';
import { useAppDispatch, useAppSelector } from '../../store/hooks';
import { getCurrentExpertsThunk } from '../../store/slices/experts';
import { addError } from '../../store/slices/errors';

export function CreateExpertForm() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { id } = useParams();
  const { experts } = useAppSelector((state) => state.experts);
  const expert = experts.find((e) => e.email === id);
  const inputRef = useRef<Typeahead>(null);
  const [email, setEmail] = useState(expert?.email ?? '');
  const [password, setPassword] = useState('');
  const [name, setName] = useState(expert?.name ?? '');
  const [surname, setSurname] = useState(expert?.surname ?? '');
  const [expertises, setExpertises] = useState<ExpertiseDTO[]>(expert?.expertises ?? []);
  const [newExpertise, setNewExpertise] = useState<ExpertiseDTO | undefined>();
  const [error, setError] = useState('');

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError('');
    if (!expert) {
      const request = await createExpert({
        email,
        password,
        name,
        surname,
        expertises: expertises.map((e) => e.field)
      });
      if (!request.success) {
        if (request.statusCode === 409) {
          setError('Email già esistente');
          return;
        }
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
      navigate('/experts');
      return;
    }

    const expertisesToAdd = expertises.filter(
      (e) => !expert.expertises.some((exp) => exp.field === e.field)
    );
    const expertisesToDelete = expert.expertises.filter(
      (e) => !expertises.some((exp) => exp.field === e.field)
    );
    for (const expertiseToAdd of expertisesToAdd) {
      const result = await addExpertiseToExpert(expert.email, expertiseToAdd);
      if (!result.success) {
        dispatch(
          addError({
            errorTitle: 'Network Error',
            errorDescription: result.error!,
            errorCode: result.statusCode.toString()
          })
        );
      }
    }
    for (const expertiseToDelete of expertisesToDelete) {
      const result = await deleteExpertiseFromExpert(expert.email, expertiseToDelete);
      if (!result.success) {
        dispatch(
          addError({
            errorTitle: 'Network Error',
            errorDescription: result.error!,
            errorCode: result.statusCode.toString()
          })
        );
      }
    }

    dispatch(getCurrentExpertsThunk());
  };

  const expertisesModified =
    (!expertises.every((arr2Item) => expert?.expertises.some((e) => e.field === arr2Item.field)) ||
      (expert && expertises.length !== expert.expertises.length)) &&
    expertises.length > 0;

  return (
    <div className="fill d-flex align-items-center justify-content-center">
      <Form onSubmit={handleSubmit}>
        <Stack gap={2}>
          <h1>{`${expert ? 'Edit' : 'Create'} expert`}</h1>
          <Row className="mb-3">
            <Form.Group as={Col} controlId="name">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                value={name}
                max={200}
                placeholder="Name"
                disabled={!!expert}
                onChange={(ev) => setName(ev.target.value)}
                required
              />
            </Form.Group>
            <Form.Group as={Col} controlId="surname">
              <Form.Label>Surname</Form.Label>
              <Form.Control
                type="text"
                value={surname}
                disabled={!!expert}
                max={200}
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
                disabled={!!expert}
                max={200}
                placeholder="email"
                onChange={(ev) => setEmail(ev.target.value)}
                required
              />
              {!!error && <p style={{ color: 'red' }}>{error}</p>}
            </Form.Group>
            {!expert && (
              <Form.Group as={Col} controlId="password">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  value={password}
                  placeholder="password"
                  onChange={(ev) => setPassword(ev.target.value)}
                  required
                  minLength={6}
                  max={200}
                />
              </Form.Group>
            )}
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
                  className="mx-auto">
                  <Trash />
                </Button>
              </Col>
            </Row>
          ))}
          <Row className="d-flex align-items-end">
            <Form.Group xs={11} as={Col} controlId="arg">
              {expertises.length < 1 && <Form.Label>Expertises</Form.Label>}
              <SearchSelect
                type="expertises"
                ref={inputRef}
                allowNewElement={true}
                minLength={0}
                elementsToFilter={expertises.map((e) => e.field)}
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
                  inputRef.current?.clear();
                }}
                variant="success"
                disabled={!newExpertise}>
                <Plus />
              </Button>
            </Col>
          </Row>

          <Button
            className="mt-2 mx-auto w-50"
            type="submit"
            disabled={expert && !expertisesModified}>
            {expert ? 'Save' : 'Create'}
          </Button>
        </Stack>
      </Form>
    </div>
  );
}
