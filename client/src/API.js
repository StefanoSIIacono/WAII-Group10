import {Product} from "./Components/Product";
import {Profile} from "./Components/Profile"

const URL = 'http://localhost:8080';

async function readProducts() {
    let url = URL + '/products';
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (!response.ok) {
            const text = await response.text();
            throw new TypeError(text);
        } else {
            const list = await response.json();
            return list.map((p) => new Product(p.ean, p.name, p.brand));
        }
    } catch (ex) {
        throw ex;
    }
}

async function readProductFromID(id) {
    const url = URL + '/courses/'+ id;
    try {
        const response = await fetch(url, {
            credentials: 'include',
            body: JSON.stringify(id),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            const text = await response.text();
            throw new TypeError(text);
        } else {
            const res = await response.json()
            return new Product(res.id, res.name, res.brand)
        }
    } catch (ex) {
        throw ex;
    }
}

async function readProfileFromMail(mail) {
    const url = URL + '/courses/'+ mail;
    try {
        const response = await fetch(url, {
            credentials: 'include',
            body: JSON.stringify(mail),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            const text = await response.text();
            throw new TypeError(text);
        } else {
            const res = await response.json()
            return new Profile(res.id, res.email, res.password)
        }
    } catch (ex) {
        throw ex;
    }
}


const API = {readProfileFromMail, readProductFromID, readProducts};
export default API