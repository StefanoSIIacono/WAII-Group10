import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import {
    AppLayout,
    HomePage,
    ProfilesPage,
    ProductsPage,
    AddProfilePage,
    EditProfilePage,
    GetProductPage,
    TicketsPage,
    CreateTicketPage,
    CreateExpertPage,
    ManagerDashboard,
    LoginPage
} from "./Components/PageLayout";
import API from "./API"

function App() {

    const [products, setProducts] = useState([]);
    const [tickets, setTickets] = useState([]); // dovrebbe comprendere tutti i campi del ticket
    const [product, setProduct] = useState([]); // perchè è un array? 
    const [profile, setProfile] = useState([]); // perchè è un array?
    const [edit, setEdit] = useState(false);
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [loggedIn, setLoggedIn] = useState(false);
    
    async function loadProducts(){
            try{
              setLoading(true);
              let prod= [] 
              prod= await API.readProducts();
              setProducts(prod);
              setLoading(false);
            }catch(e){
              setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
            }
    };

    const readProfileByMail = async(email) => {
      try{
        setLoading(true); 
        let prof = await API.readProfileFromMail(email);
        setEdit(true);
        setProfile(prof);
        setLoading(false);
      }catch(e){
        setEdit(false);
        setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
      }
    };

    const readProductByID = async(id) => {
      try{
        setLoading(true); 
        let prod = await API.readProductFromID(id);
        setProduct(prod);
        setLoading(false);
      }catch(e){
        setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
      }
    };

    const addProfile = async (profile) => {
        try {
          setLoading(true);
          await API.addProfile(profile);
          setMessage({ msg: `Profile linked to ${profile.email} correctly added`, type: 'success' });
          setLoading(false);
        } catch (e) {
          setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
        }
      }

    const editProfile = async (profile) => {
      try {
        setLoading(true);
        await API.editProfile(profile);
        setMessage({ msg: `Profile linked to ${profile.email} correctly edited`, type: 'success' });
        setProfile([]);
        setLoading(false);
      } catch (e) {
        setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
      }
    }

    // da passare coi prop, oppure metterla nel form del ticket?
    const addTicket = async (ticket) => {
      try {
        setLoading(true);
        await API.addTicket(ticket);
        setMessage({ msg: `Ticket correctly added`, type: 'success' });
        setLoading(false);
      } catch (e) {
        setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
      }
    }

    //NUOVO
    const addExpert = async (expert) => {
      try {
        setLoading(true);
        await API.addExpert(expert);
        setMessage({ msg: `Expert correctly added`, type: 'success' });
        setLoading(false);
      } catch (e) {
        setMessage({ msg: JSON.parse(e.message).detail, type: 'danger' });
      }
    }

    
    const handleLogin = async (credentials) => {
      try {
          // const [user, token] = await API.logIn(credentials)
          // localStorage.setItem('jwtToken', token) -> valid after closing the browser
          // sessionStorage.setItem('jwtToken', token) -> valid for the session only
        const user = await API.logIn(credentials);
        setLoggedIn(true);
        setMessage({ msg: `Welcome, ${user.name}!`, type: 'success' });
      } catch (err) {
        setMessage({ msg: 'Incorrect username or password', type: 'danger' });
      }
    };

    const handleLogout = async () => {
      await API.logOut();
      //localStorage.removeItem('jwtToken');
      setLoggedIn(false);
      setMessage('');
    };

    useEffect(() => {
        loadProducts();
    }, []);

    useEffect(() => {
        setProduct([])
    }, []);

    useEffect(() => {
      const checkAuth = async () => {
        try {
          await API.getUserInfo();
          setLoggedIn(true);
        } catch (error) {/*
        We have to handle the error
        */}
      };
      checkAuth();
    }, []);

    return (
      <div>
        <BrowserRouter>
          <Routes>
            <Route path='/login' element={ loggedIn ? <Navigate replace to='/' /> : <LoginPage login={handleLogin}/>} />
            <Route element={<AppLayout message={message} setMessage={setMessage} loggedIn={loggedIn} handleLogout={handleLogout} />}>
                <Route path='/' element={<HomePage />} />
                <Route path='/products' element={<ProductsPage products={products} />} />
                <Route path='/getProduct' element={<GetProductPage loading={loading} product={product} setProduct={setProduct} readProductByID={readProductByID} />} />
                <Route path='/profiles' element={<ProfilesPage edit={edit} loading={loading} setEdit={setEdit} profile={profile} setProfile={setProfile} readProfileByMail={readProfileByMail}/>} />
                <Route path='/addProfile' element={<AddProfilePage addProfile={addProfile} />} />
                <Route path='/editProfile' element={<EditProfilePage edit={edit} loading={loading} setEdit={setEdit} profile={profile} editProfile={editProfile} />} />
                <Route path='/tickets' element={<TicketsPage tickets={tickets} />} />
                <Route path='/createTicket' element={<CreateTicketPage addTickets={addTicket} />} />
                <Route path='/manager' element={<ManagerDashboard addTickets={addTicket} />} />
                <Route path='/manager/addExpert' element={<CreateExpertPage addExpert={addExpert} />} />
                <Route path='*' element={<h1>404 Page not found</h1>} />
            </Route>
          </Routes>
        </BrowserRouter >
      </div>
  );
}

export default App;
