import { useState } from 'react';
import { Form, Button, Stack, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { SearchSelect } from '../../components/searchSelect';
import { ExpertiseDTO, ProductDTO } from '../../types';
import { createTicket } from '../../utils/Api';

export function CreateTicketForm() {
  const navigate = useNavigate();
  const [obj, setObj] = useState('');
  const [body, setBody] = useState('');
  const [product, setProduct] = useState<ProductDTO | undefined>();
  const [arg, setArg] = useState<ExpertiseDTO | undefined>();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    if (!arg || !product) {
      return;
    }

    const request = await createTicket({
      obj,
      arg: arg.field,
      body,
      product: product.productId,
      attachments: []
    });
    if (!request.success) {
      console.log(request.error);
      return;
    }

    navigate('/dashboard');
  };

  return (
    <div className="fill d-flex align-items-center justify-content-center">
      <Form onSubmit={handleSubmit}>
        <Stack gap={2}>
          <h1 className="my-4">New Ticket</h1>
          <Row className="mb-3">
            <Form.Group as={Col} controlId="product">
              <Form.Label>Product</Form.Label>
              <SearchSelect
                type="products"
                onSelect={(selected) => setProduct(selected as ProductDTO)}
              />
            </Form.Group>
            <Form.Group as={Col} controlId="arg">
              <Form.Label>Argument</Form.Label>
              <SearchSelect
                type="expertises"
                onSelect={(selected) => setArg(selected as ExpertiseDTO)}
              />
            </Form.Group>
          </Row>
          <Form.Group controlId="object">
            <Form.Label>Object</Form.Label>
            <Form.Control
              type="text"
              value={obj}
              placeholder="Object"
              onChange={(ev) => setObj(ev.target.value)}
              required
            />
          </Form.Group>
          <Form.Group controlId="body">
            <Form.Label>Body</Form.Label>
            <Form.Control
              as="textarea"
              rows={6}
              type="text"
              value={body}
              placeholder="body"
              onChange={(ev) => setBody(ev.target.value)}
              required
            />
          </Form.Group>
          <Button className="mt-2 mx-auto w-50" type="submit">
            Create
          </Button>
        </Stack>
      </Form>
    </div>
  );
}
