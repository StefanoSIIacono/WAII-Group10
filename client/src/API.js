import {Product} from "./Components/Product";
import {Profile} from "./Components/Profile";


async function readProducts() {
    let url = '/products/';
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (response.ok) {
            const list = await response.json();
            return list.map((p) => new Product(p.productId, p.name, p.brand));
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}

async function readProductFromID(id) {
    const url = '/products/'+ id;
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
            return new Product(res.productId, res.name, res.brand)
        }
    } catch (ex) {
        throw ex;
    }
}

async function readProfileFromMail(mail) {
    const url = '/courses/'+ mail;
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
            return new Profile(res.email, res.password)
        }
    } catch (ex) {
        throw ex;
    }
}

const API ={readProfileFromMail, readProductFromID, readProducts};
export default API;
