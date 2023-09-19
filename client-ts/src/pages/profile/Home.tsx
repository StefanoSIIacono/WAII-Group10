import { useEffect, useState } from 'react';
import { /*useAppDispatch,*/ useAppSelector } from '../../store/hooks';
//import { getLastTicketsThunk } from '../../store/slices/tickets';
import { MessageDTO, ProductDTO, Status, TicketDTO } from '../../types';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Button, Badge, Form } from 'react-bootstrap';
import { getProduct, getTicketMessages } from '../../utils/Api';

export function Home() {
  //const dispatch = useAppDispatch();
  //const navigate = useNavigate();
  const { tickets } = useAppSelector((state) => state.tickets);
  const { user } = useAppSelector((state) => state.authenticate);
  const [selectedTicket, setSelectedTicket] = useState<TicketDTO | undefined>();
  const [selectedProducts, setSelectedProducts] = useState<ProductDTO | undefined>();
  const [selectedMessages, setSelectedMessages] = useState<MessageDTO[] | undefined>();

  const [newMessageBody, setNewMessageBody] = useState('');

  useEffect(() => {
    //dispatch(getLastTicketsThunk());
  }, []);

  useEffect(() => {
    const go = async () => {
      if (selectedTicket) {
        const messages = await getTicketMessages(selectedTicket.id);
        const product = await getProduct(selectedTicket.product);
        if (messages.success && messages.data?.data) {
          setSelectedMessages(messages.data?.data);
        }
        if (product.success && product.data) {
          setSelectedProducts(product.data);
        }
      }
    };
    go();
    setSelectedProducts({
      name: 'product1',
      productId: '1',
      brand: 'brand1'
    });
    setSelectedMessages([
      {
        id: 1,
        index: 0,
        timestamp: '1',
        body: 'CIAO COME VA',
        attachments: [],
        ticket: 1
      }
    ]);
  }, [selectedTicket]);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
  };
  /*
  const rowEvents: { onClick: RowEventHandler<TicketDTO> } = {
    onClick: (_, row) => {
      navigate(`/tickets/${row.id}`);
    }
  };
*/
  return (
    <div className="ticket-body">
      <div className="ticket-container">
        <div className="ticket-left-container">
          <div className="ticket-left-container-header">
            <h3>Tickets</h3>
            <Link to="/tickets/new">
              <Button>New Ticket</Button>
            </Link>
          </div>
          <div className="ticket-left-container-tickets">
            {tickets.map((t) => (
              <Container
                onClick={() => setSelectedTicket(t)}
                key={t.id}
                style={selectedTicket?.id === t.id ? { borderColor: 'blue' } : {}}
                className={`border border-2 ${
                  selectedTicket?.id === t.id ? 'border-info' : 'border-dark'
                } rounded fill my-1`}>
                <Row className="d-flex justify-content-between">
                  <Col md="auto">{t.obj}</Col>
                  <Col md="auto">{t.status.timestamp}</Col>
                </Row>
                <Row>
                  <Col md="auto" className="m-2 p-0">
                    <Badge bg="info">{Status[t.status.status]}</Badge>
                  </Col>
                  <Col md="auto" className="m-2 p-0">
                    <Badge bg="secondary">{t.arg.field}</Badge>
                  </Col>
                  <Col md="auto" className="m-2 p-0">
                    <Badge>{t.priority}</Badge>
                  </Col>
                </Row>
              </Container>
            ))}
          </div>
        </div>
        <div className="ticket-right-container">
          {selectedTicket && (
            <div className="ticket-right-conditional-container">
              <div className="ticket-right-header">
                <h1 className="m-0">{`${selectedTicket?.obj}`}</h1>
                <div className="ticket-right-container-badges">
                  <Badge bg="primary">{selectedTicket?.arg.field}</Badge>
                  <Badge bg="info">{selectedTicket && Status[selectedTicket.status.status]}</Badge>
                  <p className="p-1">{`Last update: ${selectedTicket?.status.timestamp}`}</p>
                </div>

                <div className="ticket-right-container-product-container">
                  <h5 className="p-0">{'Product'}</h5>
                  <div className="ticket-right-container-product-details">
                    <h6>{selectedProducts?.name}</h6>
                    <p>{selectedProducts?.brand}</p>
                  </div>
                </div>
              </div>
              <div className="ticket-right-messages-container">
                <div className="ticket-right-chat-container">
                  {selectedMessages?.map((m) => (
                    <div key={m.id}>
                      <div>
                        <h4>{m.expert?.email ?? user?.email}</h4>
                        <p>{m.timestamp}</p>
                      </div>
                      <p>{m.body}</p>
                    </div>
                  ))}
                </div>

                <Form className="ticket-right-messages-form" onSubmit={handleSubmit}>
                  <Form.Group controlId="body">
                    <Form.Control
                      as="textarea"
                      rows={4}
                      type="text"
                      value={newMessageBody}
                      placeholder="Message"
                      onChange={(ev) => setNewMessageBody(ev.target.value)}
                      required
                    />
                  </Form.Group>
                  <Button className="mt-2 mx-auto w-50" type="submit">
                    Send
                  </Button>
                </Form>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
