import { Button } from "react-bootstrap";
import { PlusCircle } from 'react-bootstrap-icons';

function AddButton(props) {
    return <p align='right'>
            <Button variant="primary" onClick={() => {

            }}>
                <PlusCircle size={42} />
            </Button>
    </p>;
}

export { AddButton };