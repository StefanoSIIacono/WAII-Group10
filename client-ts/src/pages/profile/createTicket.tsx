import { ChangeEvent, useRef, useState } from 'react';
import { Form, Button, Stack, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { SearchSelect } from '../../components/searchSelect';
import { ExpertiseDTO, ProductDTO } from '../../types';
import { createTicket } from '../../utils/Api';
import { Paperclip, XCircle } from 'react-bootstrap-icons';

export function CreateTicketForm() {
  const navigate = useNavigate();
  const hiddenFileInput = useRef<HTMLInputElement>(null);
  const [obj, setObj] = useState('');
  const [body, setBody] = useState('');
  const [product, setProduct] = useState<ProductDTO | undefined>();
  const [arg, setArg] = useState<ExpertiseDTO | undefined>();

  const [files, setFiles] = useState<File[]>([]);

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
      attachments: await Promise.all(
        files.map(async (f) => ({
          attachment: btoa(
            new Uint8Array(await f.arrayBuffer()).reduce(
              (data, byte) => data + String.fromCharCode(byte),
              ''
            )
          ),
          contentType: f.type
        }))
      )
    });
    if (!request.success) {
      console.log(request.error);
      return;
    }

    navigate('/dashboard');
  };

  const handleUploadClick = () => {
    if (hiddenFileInput.current) {
      hiddenFileInput.current.click();
    }
  };

  const handleUpload = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files !== null && e.target.files[0]) {
      const newFile = e.target.files[0];
      setFiles((oldFiles) => [...oldFiles, newFile]);
    }
  };

  const handleDeleteAttachment = (name: string) => {
    setFiles((oldFiles) => oldFiles.filter((f) => f.name !== name));
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
          <div className="ticket-right-attachments-container">
            {files.map((f) => (
              <div key={f.name}>
                <XCircle
                  onClick={() => handleDeleteAttachment(f.name)}
                  className="ticket-right-attachment-close-button"
                />
                <div className="border ticket-right-attachment-container">
                  {f.type.startsWith('image') ? (
                    <img className="ticket-right-attachment-image" src={URL.createObjectURL(f)} />
                  ) : (
                    <p>File</p>
                  )}
                </div>
              </div>
            ))}
          </div>
          <input
            onChange={handleUpload}
            ref={hiddenFileInput}
            accept=".jpg"
            type="file"
            style={{ display: 'none' }}
          />
          <Button disabled={files.length > 3} variant="secondary" onClick={handleUploadClick}>
            <Paperclip size={20} />
          </Button>
          <Button className="mt-2 mx-auto w-50" type="submit">
            Create
          </Button>
        </Stack>
      </Form>
    </div>
  );
}
