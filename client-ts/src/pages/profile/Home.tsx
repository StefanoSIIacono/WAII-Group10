import { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks';
import {
  getCurrentPageTicketsThunk,
  getLastTicketsThunk,
  getNextTicketsThunk,
  getPreviousTicketsThunk
} from '../../store/slices/tickets';
import {
  ExpertDTO,
  MessageDTO,
  Priority,
  Roles,
  Status,
  TicketInProgressBodyDTO,
  validStatusChanges
} from '../../types';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Button, Badge, Form, Modal } from 'react-bootstrap';
import { ackMessage, addMessage, getTicketMessages, setTicketStatus } from '../../utils/Api';
import { updateTicketThunk } from '../../store/slices/tickets';
import { SearchSelect } from '../../components/searchSelect';
import dayjs from 'dayjs';
import { Paperclip, Send, XCircle, ArrowLeft, ArrowRight } from 'react-bootstrap-icons';
import { addError } from '../../store/slices/errors';
import { useInterval } from '../../components/useInterval';

export function Home() {
  const dispatch = useAppDispatch();
  const hiddenFileInput = useRef<HTMLInputElement>(null);
  //const navigate = useNavigate();
  const { tickets, currentPage, totalPages } = useAppSelector((state) => state.tickets);
  const { user } = useAppSelector((state) => state.authenticate);
  const [selectedTicketId, setSelectedTicketId] = useState<number | undefined>();
  const [selectedMessages, setSelectedMessages] = useState<MessageDTO[] | undefined>();

  const [files, setFiles] = useState<File[]>([]);

  const [newMessageBody, setNewMessageBody] = useState('');

  const [showInProgressModal, setShowInProgressModal] = useState(false);
  const [currentStatusValue, setCurrentStatusValue] = useState<Status | undefined>();
  const [currentInProgressExpertSelection, setCurrentInProgressExpertSelection] = useState<
    ExpertDTO | undefined
  >();
  const [currentInProgressPrioritySelection, setCurrentInProgressPrioritySelection] =
    useState<Priority>(Priority.MEDIUM);

  const selectedTicket = tickets.find((t) => t.id === selectedTicketId);
  const userRole = user?.role;

  let allowedStatusChanges =
    userRole === Roles.MANAGER
      ? [Status.OPEN, Status.CLOSED, Status.RESOLVED, Status.IN_PROGRESS]
      : userRole === Roles.PROFILE
      ? [Status.CLOSED, Status.RESOLVED, Status.REOPENED]
      : [Status.CLOSED, Status.OPEN];

  if (currentStatusValue !== undefined) {
    allowedStatusChanges = allowedStatusChanges.filter((asc) =>
      validStatusChanges[currentStatusValue].includes(asc)
    );
    if (!allowedStatusChanges.includes(currentStatusValue)) {
      allowedStatusChanges = [currentStatusValue, ...allowedStatusChanges];
    }
  }

  useEffect(() => {
    dispatch(getLastTicketsThunk());
  }, []);

  useInterval(async () => {
    dispatch(getCurrentPageTicketsThunk());
    if (selectedTicketId) {
      const messages = await getTicketMessages(selectedTicketId);
      if (messages.success && messages.data?.data) {
        setSelectedMessages(messages.data?.data);
      } else {
        dispatch(
          addError({
            errorTitle: 'Network Error',
            errorDescription: messages.error!,
            errorCode: messages.statusCode.toString()
          })
        );
      }
    }
  }, 5000);

  useEffect(() => {
    const go = async () => {
      if (selectedTicket) {
        setCurrentStatusValue(Status[selectedTicket.status.status]);
        if (userRole !== Roles.MANAGER) {
          const messages = await getTicketMessages(selectedTicket.id);
          if (messages.success && messages.data?.data) {
            setSelectedMessages(messages.data?.data);
          } else {
            dispatch(
              addError({
                errorTitle: 'Network Error',
                errorDescription: messages.error!,
                errorCode: messages.statusCode.toString()
              })
            );
          }
          if (
            selectedTicket.totalMessages !== undefined &&
            selectedTicket.lastReadMessageIndex !== undefined &&
            selectedTicket.totalMessages - 1 > selectedTicket.lastReadMessageIndex
          ) {
            const result = await ackMessage(selectedTicket.id);
            if (result.success) {
              dispatch(updateTicketThunk(selectedTicket.id));
            }
          }
        }
      }
    };
    go();
  }, [selectedTicket]);

  const updateStatus = async (
    newStatus: Status,
    ticketInProgressBodyDTO?: TicketInProgressBodyDTO
  ) => {
    if (!selectedTicket) {
      return;
    }
    const response = await setTicketStatus(selectedTicket.id, newStatus, ticketInProgressBodyDTO);
    if (response.success) {
      if (userRole === Roles.EXPERT) {
        dispatch(getCurrentPageTicketsThunk());
        return;
      }
      dispatch(updateTicketThunk(selectedTicket.id));
    } else {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: response.error!,
          errorCode: response.statusCode.toString()
        })
      );
    }
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!selectedTicketId) {
      return;
    }

    const request = await addMessage(selectedTicketId, {
      body: newMessageBody,
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

    if (request.success) {
      setNewMessageBody('');
      setFiles([]);
      const messages = await getTicketMessages(selectedTicketId);
      if (messages.success && messages.data?.data) {
        setSelectedMessages(messages.data?.data);
      } else {
        dispatch(
          addError({
            errorTitle: 'Network Error',
            errorDescription: messages.error!,
            errorCode: messages.statusCode.toString()
          })
        );
      }
    } else {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: request.error!,
          errorCode: request.statusCode.toString()
        })
      );
    }
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
    <div className="ticket-body">
      <div className="ticket-container">
        <div className="ticket-left-container">
          <div className="ticket-left-container-header">
            <h3>Tickets</h3>
            {userRole === Roles.PROFILE && (
              <Link to="/tickets/new">
                <Button>New Ticket</Button>
              </Link>
            )}
          </div>
          <div className="ticket-left-container-tickets">
            {tickets.map((t) => {
              return (
                <Container
                  onClick={() => setSelectedTicketId(t.id)}
                  key={t.id}
                  className={`border border-2 ${
                    selectedTicket?.id === t.id ? 'border-info' : 'border-dark'
                  } rounded fill my-1 ticket-left-container-ticket`}>
                  <Row className="d-flex justify-content-between">
                    <Col className="ellipsis-text">{t.obj}</Col>
                    <Col md="auto">{dayjs(t.creationDate).format('DD/MM/YYYY HH:mm')}</Col>
                  </Row>
                  <div className="d-flex justify-content-between">
                    <div>
                      <Badge className="mx-1" bg="info">
                        {t.status.status}
                      </Badge>
                      <Badge className="mx-1" bg="secondary">
                        {t.arg.field}
                      </Badge>
                      <Badge className="mx-1">{t.priority}</Badge>
                    </div>
                    <div>
                      {userRole !== Roles.MANAGER &&
                        t.totalMessages !== undefined &&
                        t.lastReadMessageIndex !== undefined &&
                        t.totalMessages - 1 > t.lastReadMessageIndex && (
                          <Badge bg="danger">{t.totalMessages - 1 - t.lastReadMessageIndex}</Badge>
                        )}
                    </div>
                  </div>
                </Container>
              );
            })}
          </div>
          <div className="ticket-left-container-pagination">
            <Button
              onClick={() => {
                dispatch(getPreviousTicketsThunk());
              }}
              disabled={currentPage < 2}>
              <ArrowLeft />
            </Button>
            <p>{`Page ${currentPage}`}</p>
            <Button
              onClick={() => {
                dispatch(getNextTicketsThunk());
              }}
              disabled={currentPage === totalPages}>
              <ArrowRight />
            </Button>
          </div>
        </div>
        <div className="ticket-right-container">
          {selectedTicket && (
            <div className="ticket-right-conditional-container">
              <div className="ticket-right-header">
                <h1 className="m-0">{`${selectedTicket?.obj}`}</h1>
                <div className="ticket-right-container-badges">
                  <Badge bg="info">{selectedTicket?.status.status}</Badge>
                  <Badge bg="secondary">{selectedTicket?.arg.field}</Badge>
                  <Badge>{selectedTicket?.priority}</Badge>
                  <p className="p-1">{`Last status update: ${
                    selectedTicket
                      ? dayjs(selectedTicket.status.timestamp).format('DD/MM/YYYY HH:mm')
                      : ''
                  }`}</p>
                </div>
                <p className="my-3">{`Opened by: ${selectedTicket.profile.name} ${selectedTicket.profile.surname} (${selectedTicket.profile.email})`}</p>
                <div className="ticket-right-container-second-header">
                  <div className="ticket-right-container-product-container">
                    <h5 className="p-0">{'Product'}</h5>
                    <div className="ticket-right-container-product-details">
                      <h6>{selectedTicket.product.name}</h6>
                      <p>{selectedTicket.product.brand}</p>
                    </div>
                  </div>
                  <Form.Group controlId="state">
                    <Form.Label>Set ticket status</Form.Label>
                    <Form.Select
                      onChange={(status) => {
                        if (+status.target.value === currentStatusValue) {
                          return;
                        }
                        if (+status.target.value === Status.IN_PROGRESS) {
                          setShowInProgressModal(true);
                          return;
                        }
                        updateStatus(+status.target.value);
                        setCurrentStatusValue(+status.target.value);
                      }}
                      disabled={allowedStatusChanges.length < 2}
                      value={currentStatusValue}
                      aria-label="Default select example">
                      {allowedStatusChanges.map((asc) => (
                        <option key={asc} value={asc}>
                          {Status[asc]}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </div>
                {selectedTicket.expert && (
                  <div className="ticket-right-containe-expert-container">
                    <p>{`Assigned expert: ${selectedTicket.expert.name} ${
                      selectedTicket.expert.surname
                    } ${userRole === Roles.MANAGER ? `(${selectedTicket.expert.email})` : ''}`}</p>
                  </div>
                )}
              </div>
              {userRole !== Roles.MANAGER && (
                <div className="ticket-right-messages-container">
                  <div className="ticket-right-chat-container">
                    {selectedMessages?.map((m) => (
                      <div key={m.id} className="border-bottom my-2">
                        <div>
                          <h4>
                            {m.expert
                              ? `${m.expert.name} ${m.expert.surname}`
                              : `${selectedTicket.profile.name} ${selectedTicket.profile.surname}`}
                          </h4>
                          <p>{dayjs(m.timestamp).format('DD/MM/YYYY HH:mm')}</p>
                        </div>
                        <p>{m.body}</p>
                        {m.attachments.length > 0 && <h6>{'Attachments:'}</h6>}
                        <div className="ticket-right-attachments-container">
                          {m.attachments.map((a) => (
                            <div key={a.id} className="border ticket-right-attachment-container">
                              {a.contentType.startsWith('image') ? (
                                <img
                                  className="ticket-right-attachment-image"
                                  src={URL.createObjectURL(
                                    new Blob([
                                      Uint8Array.from(atob(a.attachment), (c) => c.charCodeAt(0))
                                    ])
                                  )}
                                />
                              ) : (
                                <p>File</p>
                              )}
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                  <div className="ticket-right-attachments-container">
                    {files.map((f) => (
                      <div key={f.name}>
                        <XCircle
                          onClick={() => handleDeleteAttachment(f.name)}
                          className="ticket-right-attachment-close-button"
                        />
                        <div className="border ticket-right-attachment-container">
                          {f.type.startsWith('image') ? (
                            <img
                              className="ticket-right-attachment-image"
                              src={URL.createObjectURL(f)}
                            />
                          ) : (
                            <p>File</p>
                          )}
                        </div>
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
                        disabled={currentStatusValue !== Status.IN_PROGRESS}
                        placeholder="Message"
                        onChange={(ev) => setNewMessageBody(ev.target.value)}
                        required
                      />
                    </Form.Group>
                    <div className="ticket-right-messages-button-container">
                      <Button disabled={currentStatusValue !== Status.IN_PROGRESS} type="submit">
                        <Send size={20} />
                      </Button>
                      <input
                        onChange={handleUpload}
                        ref={hiddenFileInput}
                        accept=".jpg, .jpeg, .png"
                        type="file"
                        style={{ display: 'none' }}
                      />
                      <Button
                        variant="secondary"
                        onClick={handleUploadClick}
                        disabled={currentStatusValue !== Status.IN_PROGRESS || files.length > 3}>
                        <Paperclip size={20} />
                      </Button>
                    </div>
                  </Form>
                </div>
              )}
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
                expertise={selectedTicket?.arg.field}
                onSelect={(selected) => setCurrentInProgressExpertSelection(selected as ExpertDTO)}
              />
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
              <Form.Label>Priority</Form.Label>
              <Form.Select
                onChange={(priority) => {
                  setCurrentInProgressPrioritySelection(+priority.target.value);
                }}
                value={currentInProgressPrioritySelection}
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
              setShowInProgressModal(false);
            }}
            disabled={!currentInProgressExpertSelection || !currentInProgressPrioritySelection}>
            Confirm
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
