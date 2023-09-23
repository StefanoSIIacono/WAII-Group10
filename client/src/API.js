import {Product} from "./Components/Product";
import {Profile} from "./Components/Profile";
import {Expert} from "./Components/Expert";
import {Expertise} from "./Components/Expertise";
import {Ticket} from "./Components/Ticket";

const logIn = async (credentials) => {
    const response = await fetch('/login/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            //'Authorization': `Bearer ${jwtToken}`,
        },
        credentials: 'include',
        body: JSON.stringify(credentials),
    });
    if (response.ok) {
        const user = await response.json();
        console.log(user)
        return user;
    }
    else {
        const errDetails = await response.text();
        throw errDetails;
    }
};
/*
TODO:
messaggi e ticket non vengono rimossi per avere una history (di solito questi contenuti non vengono eliminati)
addTicket -> DONE
getTickets -> mancano i campi da controllare + check sullo user (l'ideale sarebbe farlo backend)
changeTicketStatus ? -> dipende come viene modificato lo status (dopo aver scritto un messaggio, manualmente o entrambi i casi) DONE, manca il check da fare sullo status
addMessage -> include eventuali attachments  pt.2 bisogna aspettare il backend
getUserInfo
changeUserInfo
removeProfile ? DONE (manca check sullo user) però non so se lo terrei
remoneExpert ? DONE (manca check sullo user) però non so se lo terrei
addExpert -> manager only 
assignTicket -> manager only
changeTicketPriority ->manager only
removeProduct ? -> manager only  (non so se lo terrei)
*/


// const getUserInfo = async () => {
//     const response = await fetch(APIURL + '/sessions/current', {
//         credentials: 'include',
//     });
//     const user = await response.json();
//     if (response.ok) {
//         return user;
//     } else {
//         throw user;
//     }
// };

const logOut = async () => {
    const response = await fetch('/login/', {
        method: 'DELETE',
        credentials: 'include'
    });
    if (response.ok)
        return null;
}

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

async function readProfiles() {
    let url = '/profiles/';
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (response.ok) {
            const list = await response.json();
            return list.map((p) => new Profile(p.email, p.name, p.surname))
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
    const url = '/profiles/'+ mail;
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (!response.ok) {
            const text = await response.text();
            throw new TypeError(text);
        } else {
            const res = await response.json();
            return new Profile(res.email, res.name, res.surname)
        }
    } catch (ex) {
        throw ex;
    }
}

async function readExperts() {
    let url = '/experts/';
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (response.ok) {
            const list = await response.json();
            return list.map((e) => new Expert(e.email, e.name, e.surname, e.expertises, e.changedStatuses))
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}

async function readExpertises() {
    let url = '/expertises/';
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (response.ok) {
            const list = await response.json();
            return list.map((e) => new Expertise(e.id, e.field))
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}

async function readExpertFromMail(mail) {
    const url = '/experts/'+ mail;
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (!response.ok) {
            const text = await response.text();
            throw new TypeError(text);
        } else {
            const res = await response.json();
            return new Expert(res.email, res.name, res.surname, res.expertises, res.changedStatuses)
        }
    } catch (ex) {
        throw ex;
    }
}

async function editProfile(profile) {
    const url = '/profiles/' + profile.email + '/newInfo';
    try {
        const response = await fetch(url, {
            method: 'PUT',
            credentials: 'include',
            body: JSON.stringify(profile),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            return true;
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}

async function signup(profile, password) {
    const url = '/profiles/';
    try {
        const response = await fetch(url, {
            method: 'POST',
            credentials: 'include',
            body: {
                email: profile.email,
                name: profile.name,
                surname: profile.surname,
                password: password // DA VERIFICARE HASH
            },
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            return true;
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}


/*async function deleteProfile(email) {
    try {
        const response = await fetch('/profiles/' + email, {
            method: 'DELETE',
            credentials: 'include'
        });
        if (response.ok)
            return null; 
        else {
            const text = await response.text();
            throw new TypeError(text);
        }
    }catch (ex) {
        throw ex;
    }
}
*/

async function readTickets() {
    let url = '/tickets/';
    try {
        const response = await fetch(url, {
            credentials: 'include',
        });
        if (response.ok) {
            const list = await response.json();
            return list.map((p) => new Ticket()); //TODO
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}


async function changeTicketStatus(ticketId, status) {
    const url = '/tickets/' + ticketId + '/' + status;
    try {
        const response = await fetch(url, {
            method: 'PUT',
            credentials: 'include',
        });
        if (response.ok) {
            return true;
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}


async function addTicket(ticket) {
    const url = '/tickets/';
    try {
        const response = await fetch(url, {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify(ticket),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            return true;
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}

/*async function addMessage(message) {
    const url = '/tickets/' + 'qualcosa;  //bisogna aspettare l'implementazione dei messaggi backend
    try {
        const response = await fetch(url, {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify(message),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            return true;
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}
*/

async function addExpert(expert) {
    const url = '/createExpert';
    try {
        const response = await fetch(url, {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify(expert),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            return true;
        } else {
            const text = await response.text();
            throw new TypeError(text);
        }
    } catch (ex) {
        throw ex;
    }
}

const API ={
    readProfileFromMail,
    readProductFromID,
    readProducts,
    readProfiles,
    readExperts,
    readExpertFromMail,
    readExpertises,
    readTickets,
    addTicket,
    changeTicketStatus,
    addExpert,
    editProfile,
    signup,
    logIn,
    logOut
};
export default API;
