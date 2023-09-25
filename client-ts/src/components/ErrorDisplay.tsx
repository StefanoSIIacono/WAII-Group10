import { Alert } from 'react-bootstrap';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { acknowledgeError } from '../store/slices/errors';
import { checkAuthentication } from '../store/slices/authentication';

export function ErrorDisplay() {
  const dispatch = useAppDispatch();
  const { error: currentError, errorQueue } = useAppSelector((state) => state.errors);

  if (errorQueue.some((e) => e.errorCode === '401')) {
    dispatch(checkAuthentication());
  }

  return (
    <>
      {currentError && (
        <Alert
          style={{ position: 'absolute', bottom: 50, right: 50 }}
          variant="danger"
          show={currentError !== undefined}
          onClose={() => dispatch(acknowledgeError())}
          dismissible>
          <Alert.Heading>{`${currentError?.errorTitle} (${currentError?.errorCode})`}</Alert.Heading>
          <p>{currentError?.errorDescription}</p>
        </Alert>
      )}
    </>
  );
}
