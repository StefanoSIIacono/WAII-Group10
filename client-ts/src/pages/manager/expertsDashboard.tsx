import { useEffect, useState } from 'react';
import { /*useAppDispatch,*/ useAppSelector } from '../../store/hooks';
//import { getLastTicketsThunk } from '../../store/slices/tickets';
import { ExpertDTO } from '../../types';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Button, Badge } from 'react-bootstrap';

export function ExpertsDashboard() {
  //const dispatch = useAppDispatch();
  //const navigate = useNavigate();
  const { experts } = useAppSelector((state) => state.experts);
  const [selectedExpert, setSelectedExpert] = useState<ExpertDTO | undefined>();

  useEffect(() => {
    //dispatch(getExpertsThunk());
  }, []);

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
            <h3>Experts</h3>
            <Link to="/experts/new">
              <Button>New Expert</Button>
            </Link>
          </div>
          <div className="ticket-left-container-tickets">
            {experts.map((t) => (
              <Container
                onClick={() => setSelectedExpert(t)}
                key={t.email}
                style={selectedExpert?.email === t.email ? { borderColor: 'blue' } : {}}
                className={`border border-2 ${
                  selectedExpert?.email === t.email ? 'border-info' : 'border-dark'
                } rounded fill my-1`}>
                <Row className="d-flex justify-content-between">
                  <Col md="auto">{t.email}</Col>
                </Row>
                <Row>
                  {t.expertises.map((expertise) => (
                    <Col key={expertise.field} md="auto" className="m-2 p-0">
                      <Badge bg="info">{expertise.field}</Badge>
                    </Col>
                  ))}
                </Row>
              </Container>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
