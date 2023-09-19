import { useState } from 'react';
import { Form, Button, Stack } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAppDispatch } from '../store/hooks';
import { loginUser } from '../store/slices/authentication';

export function LoginForm() {
  const dispatch = useAppDispatch();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    dispatch(
      loginUser({
        username,
        password
      })
    );
  };

  return (
    <div className="fill d-flex align-items-center justify-content-center">
      <Form onSubmit={handleSubmit}>
        <Stack gap={2}>
          <h1>Login</h1>
          <Form.Group controlId="username">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              value={username}
              placeholder="email"
              onChange={(ev) => setUsername(ev.target.value)}
              required
            />
          </Form.Group>
          <Form.Group controlId="password">
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
          <p>
            You don&apos;t have an account? <Link to="/register">Register</Link>
          </p>

          <Button type="submit">Login</Button>
        </Stack>
      </Form>
    </div>
  );
}
