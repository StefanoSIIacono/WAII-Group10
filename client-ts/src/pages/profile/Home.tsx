import { useEffect, useState } from 'react';
import { /*useAppDispatch,*/ useAppSelector } from '../../store/hooks';
//import { getLastTicketsThunk } from '../../store/slices/tickets';
import {
  ExpertDTO,
  MessageDTO,
  Priority,
  ProductDTO,
  Status,
  TicketDTO,
  TicketInProgressBodyDTO
} from '../../types';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Button, Badge, Form, Modal } from 'react-bootstrap';
import { getProduct, getTicketMessages, setTicketStatus } from '../../utils/Api';
import { updateTicketThunk } from '../../store/slices/tickets';
import { SearchSelect } from '../../components/searchSelect';

export function Home() {
  //const dispatch = useAppDispatch();
  //const navigate = useNavigate();
  const { tickets } = useAppSelector((state) => state.tickets);
  const { user } = useAppSelector((state) => state.authenticate);
  const [selectedTicket, setSelectedTicket] = useState<TicketDTO | undefined>();
  const [selectedProducts, setSelectedProducts] = useState<ProductDTO | undefined>();
  const [selectedMessages, setSelectedMessages] = useState<MessageDTO[] | undefined>();

  const [newMessageBody, setNewMessageBody] = useState('');

  const [showInProgressModal, setShowInProgressModal] = useState(false);
  const [currentStatusValue, setCurrentStatusValue] = useState<Status | undefined>();
  const [currentInProgressExpertSelection, setCurrentInProgressExpertSelection] = useState<
    ExpertDTO | undefined
  >();
  const [currentInProgressPrioritySelection, setCurrentInProgressPrioritySelection] = useState<
    Priority | undefined
  >();

  useEffect(() => {
    //dispatch(getLastTicketsThunk());
  }, []);

  useEffect(() => {
    const go = async () => {
      if (selectedTicket) {
        const messages = await getTicketMessages(selectedTicket.id);
        const product = await getProduct(selectedTicket.product);
        setCurrentStatusValue(selectedTicket.status.status);
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

  const updateStatus = async (
    newStatus: Status,
    ticketInProgressBodyDTO?: TicketInProgressBodyDTO
  ) => {
    console.log(newStatus, Status[0]);
    if (!selectedTicket) {
      return;
    }
    const response = await setTicketStatus(selectedTicket.id, newStatus, ticketInProgressBodyDTO);
    if (response.success) {
      updateTicketThunk(selectedTicket.id);
    }
  };

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

                <div className="ticket-right-container-second-header">
                  <div className="ticket-right-container-product-container">
                    <h5 className="p-0">{'Product'}</h5>
                    <div className="ticket-right-container-product-details">
                      <h6>{selectedProducts?.name}</h6>
                      <p>{selectedProducts?.brand}</p>
                    </div>
                  </div>
                  <Form.Group controlId="state">
                    <Form.Label>Set ticket status</Form.Label>
                    <Form.Select
                      onChange={(status) => {
                        if (+status.target.value === Status.IN_PROGRESS) {
                          setShowInProgressModal(true);
                          return;
                        }
                        updateStatus(+status.target.value);
                        setCurrentStatusValue(+status.target.value);
                      }}
                      value={currentStatusValue}
                      aria-label="Default select example">
                      <option value={Status.OPEN}>Open</option>
                      <option value={Status.CLOSED}>Closed</option>
                      <option value={Status.IN_PROGRESS}>In progress</option>
                    </Form.Select>
                  </Form.Group>
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
      <Modal
        show={showInProgressModal}
        onHide={() => setShowInProgressModal(false)}
        size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered>
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">Set In Progress</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Expert</Form.Label>
              <SearchSelect
                type="experts"
                onSelect={(selected) => setCurrentInProgressExpertSelection(selected as ExpertDTO)}
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
              <Form.Label>Priority</Form.Label>
              <Form.Select
                onChange={(priority) => {
                  setCurrentInProgressPrioritySelection(+priority.target.value);
                }}
                value={currentStatusValue}
                aria-label="Default select example">
                <option value={Priority.HIGH}>High</option>
                <option value={Priority.MEDIUM}>Medium</option>
                <option value={Priority.LOW}>Low</option>
              </Form.Select>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={() => setShowInProgressModal(false)}>Close</Button>
          <Button
            onClick={() => {
              updateStatus(Status.IN_PROGRESS, {
                expert: currentInProgressExpertSelection!.email,
                priority: currentInProgressPrioritySelection!
              });
              setCurrentStatusValue(Status.IN_PROGRESS);
            }}
            disabled={!currentInProgressExpertSelection || !currentInProgressPrioritySelection}>
            Confirm
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
