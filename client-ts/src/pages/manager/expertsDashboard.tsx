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
import { getExpertStats } from '../../utils/Api';
import { StatsDTO } from '../../types';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  BarChart,
  Bar,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import { addError } from '../../store/slices/errors';

export function ExpertsDashboard() {
  const dispatch = useAppDispatch();
  //const navigate = useNavigate();
  const { experts, currentPage, totalPages } = useAppSelector((state) => state.experts);
  const [selectedExpertEmail, setSelectedExpertEmail] = useState<string | undefined>();
  const [selectedStats, setSelectedStats] = useState<StatsDTO | undefined>(undefined);

  const selectedExpert = experts.find((t) => t.email === selectedExpertEmail);

  useEffect(() => {
    dispatch(getExpertsThunk());
  }, []);

  useEffect(() => {
    const go = async () => {
      if (selectedExpertEmail) {
        const result = await getExpertStats(selectedExpertEmail);
        if (result.success && result.data) {
          setSelectedStats(result.data);
        } else {
          dispatch(
            addError({
              errorTitle: 'Network Error',
              errorDescription: result.error!,
              errorCode: result.statusCode.toString()
            })
          );
        }
      }
    };
    go();
  }, [selectedExpertEmail]);

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
              disabled={currentPage < 2}>
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
            <>
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
                <Container className="experts-right-stats">
                  <Row>
                    <Col className="experts-right-stats-container">
                      <Row>
                        <Col className="experts-right-stats-container-label">
                          {'Tickets In Progress'}
                        </Col>
                      </Row>
                      <Row>
                        <Col className="experts-right-stats-container-value">
                          {selectedStats?.ticketInProgress}
                        </Col>
                      </Row>
                    </Col>
                    <Col className="experts-right-stats-container">
                      <Row>
                        <Col className="experts-right-stats-container-label">
                          {'Total tickets assigned'}
                        </Col>
                      </Row>
                      <Row>
                        <Col className="experts-right-stats-container-value">
                          {selectedStats?.totalAssignedEver}
                        </Col>
                      </Row>
                    </Col>
                  </Row>
                  <Row>
                    <Col className="experts-right-stats-container">
                      <Row>
                        <Col className="experts-right-stats-container-label">
                          {'Total Tickets Closed'}
                        </Col>
                      </Row>
                      <Row>
                        <Col className="experts-right-stats-container-value">
                          {selectedStats?.totalClosed}
                        </Col>
                      </Row>
                    </Col>
                    <Col className="experts-right-stats-container">
                      <Row>
                        <Col className="experts-right-stats-container-label">
                          {'Average time to close Ticket'}
                        </Col>
                      </Row>
                      <Row>
                        <Col className="experts-right-stats-container-value">
                          {selectedStats && selectedStats.totalClosed > 0
                            ? `${(
                                selectedStats.totalTimeToSolveTickets /
                                selectedStats.totalClosed /
                                60 /
                                60
                              ).toFixed(2)} h`
                            : 'No Data'}
                        </Col>
                      </Row>
                    </Col>
                  </Row>
                  {selectedStats && (
                    <>
                      <Row>
                        <Col className="experts-right-stats-graph-label">
                          {'Number of Closed Tickets Per Day'}
                        </Col>
                      </Row>
                      <Row>
                        <Col>
                          <ResponsiveContainer width="100%" height={500}>
                            <LineChart
                              width={400}
                              height={500}
                              data={selectedStats.closedPerDay}
                              margin={{
                                top: 5,
                                right: 30,
                                left: 20,
                                bottom: 5
                              }}>
                              <CartesianGrid strokeDasharray="3 3" />
                              <XAxis dataKey="data" />
                              <YAxis />
                              <Tooltip />
                              <Legend />
                              <Line
                                type="monotone"
                                dataKey="nticketClosed"
                                name="Ticket closed"
                                stroke="#8884d8"
                                activeDot={{ r: 8 }}
                              />
                            </LineChart>
                          </ResponsiveContainer>
                        </Col>
                      </Row>
                      <Row>
                        <Col className="experts-right-stats-graph-label">
                          {'Number of Closed Tickets Per Expertise'}
                        </Col>
                      </Row>
                      <Row>
                        <Col>
                          <ResponsiveContainer width="100%" height={500}>
                            <BarChart
                              width={400}
                              height={500}
                              data={selectedStats.closedPerExpertise}
                              margin={{
                                top: 5,
                                right: 30,
                                left: 20,
                                bottom: 5
                              }}>
                              <CartesianGrid strokeDasharray="3 3" />
                              <XAxis dataKey="expertise" />
                              <YAxis />
                              <Tooltip />
                              <Legend />
                              <Bar name="Ticket closed" dataKey="nticketClosed" fill="#82ca9d" />
                            </BarChart>
                          </ResponsiveContainer>
                        </Col>
                      </Row>
                    </>
                  )}
                </Container>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
