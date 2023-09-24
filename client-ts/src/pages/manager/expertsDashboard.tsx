import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../store/hooks';
import {
  getExpertsThunk,
  getNextExpertsThunk,
  getPreviousExpertsThunk
} from '../../store/slices/experts';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Button, Badge } from 'react-bootstrap';
import { ArrowLeft, ArrowRight } from 'react-bootstrap-icons';

export function ExpertsDashboard() {
  const dispatch = useAppDispatch();
  //const navigate = useNavigate();
  const { experts, currentPage, totalPages } = useAppSelector((state) => state.experts);
  const [selectedExpertEmail, setSelectedExpertEmail] = useState<string | undefined>();

  const selectedExpert = experts.find((t) => t.email === selectedExpertEmail);

  useEffect(() => {
    dispatch(getExpertsThunk());
  }, []);

  return (
    <div className="ticket-body">
      <div className="ticket-container">
        <div className="ticket-left-container">
          <div className="ticket-left-container-header">
            <h3>Experts</h3>
            <Link to="/experts/new">
              <Button>New Expert</Button>
            </Link>
          </div>
          <div className="ticket-left-container-tickets">
            {experts.map((t) => (
              <Container
                onClick={() => setSelectedExpertEmail(t.email)}
                key={t.email}
                style={selectedExpert?.email === t.email ? { borderColor: 'blue' } : {}}
                className={`border border-2 ${
                  selectedExpert?.email === t.email ? 'border-info' : 'border-dark'
                } rounded fill my-1 experts-left-expert-container`}>
                <Row className="d-flex justify-content-between">
                  <Col md="auto">{`${t.name} ${t.surname}`}</Col>
                </Row>
                <Row className="d-flex justify-content-between">
                  <Col md="auto">{t.email}</Col>
                </Row>
                <Row>
                  {t.expertises.slice(0, 3).map((expertise) => (
                    <Col key={expertise.field} md="auto" className="m-2 p-0">
                      <Badge bg="info">{expertise.field}</Badge>
                    </Col>
                  ))}
                </Row>
              </Container>
            ))}
          </div>
          <div className="ticket-left-container-pagination">
            <Button
              onClick={() => {
                dispatch(getPreviousExpertsThunk());
              }}
              disabled={currentPage === 1}>
              <ArrowLeft />
            </Button>
            <p>{`Page ${currentPage}`}</p>
            <Button
              onClick={() => {
                dispatch(getNextExpertsThunk());
              }}
              disabled={currentPage === totalPages}>
              <ArrowRight />
            </Button>
          </div>
        </div>
        <div className="ticket-right-container">
          {selectedExpert && (
            <div className="ticket-right-conditional-container">
              <div className="ticket-right-header">
                <h1 className="m-0">{`${selectedExpert.name} ${selectedExpert.surname}`}</h1>
                <h3 className="m-0">{selectedExpert.email}</h3>
                <div className="ticket-right-container-badges">
                  {selectedExpert.expertises.map((expertise) => (
                    <Badge key={expertise.field} bg="info">
                      {expertise.field}
                    </Badge>
                  ))}
                </div>
                <div className="ticket-right-container-second-header">
                  <div className="ticket-right-container-product-container"></div>
                  <Link to={`/experts/edit/${selectedExpert.email}`}>
                    <Button>Edit Expert</Button>
                  </Link>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
