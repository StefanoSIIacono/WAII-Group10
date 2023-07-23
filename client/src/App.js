import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import {AppLayout, HomePage, ProfilesPage, ProductsPage, AddProfilePage, EditProfilePage, GetProductPage, TicketsPage, CreateTicketPage, LoginPage} from "./Components/PageLayout";
import API from "./API"

function App() {

    const [products, setProducts] = useState([]);
    const [tickets, setTickets] = useState([]);
    const [product, setProduct] = useState([]);
    const [profile, setProfile] = useState([]);
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
    
    const handleLogin = async (credentials) => {
      try {
        const user = await API.logIn(credentials);
        setLoggedIn(true);
        setMessage({ msg: `Welcome, ${user.name}!`, type: 'success' });
      } catch (err) {
        setMessage({ msg: 'Incorrect username or password', type: 'danger' });
      }
    };

    const handleLogout = async () => {
      await API.logOut();
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
        } catch (error) { }
      };
      checkAuth();
    }, []);

    return (
      <div>
        <BrowserRouter>
          <Routes>
            <Route path='/login' element={ loggedIn ? <Navigate replace to='/' /> : <LoginPage login={handleLogin} />} />
            <Route element={<AppLayout message={message} setMessage={setMessage} loggedIn={loggedIn} handleLogout={handleLogout} />}>
                <Route path='/' element={<HomePage />} />
                <Route path='/products' element={<ProductsPage products={products} />} />
                <Route path='/getProduct' element={<GetProductPage loading={loading} product={product} setProduct={setProduct} readProductByID={readProductByID} />} />
                <Route path='/profiles' element={<ProfilesPage edit={edit} loading={loading} setEdit={setEdit} profile={profile} setProfile={setProfile} readProfileByMail={readProfileByMail}/>} />
                <Route path='/addProfile' element={<AddProfilePage addProfile={addProfile} />} />
                <Route path='/editProfile' element={<EditProfilePage edit={edit} loading={loading} setEdit={setEdit} profile={profile} editProfile={editProfile} />} />
                <Route path='/tickets' element={<TicketsPage tickets={tickets} />} />
                <Route path='/createTicket' element={<CreateTicketPage addTickets={addTicket} />} />
                <Route path='*' element={<h1>404 Page not found</h1>} />
            </Route>
          </Routes>
        </BrowserRouter >
      </div>
  );
}

export default App;
