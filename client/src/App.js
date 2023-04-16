import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {Profile} from "./Components/Profile";
import { useEffect, useState } from 'react';
import {Row, Alert} from 'react-bootstrap'
import {AppLayout, HomePage, ProfilesPage, ProductsPage, AddProfilePage, EditProfilePage} from "./Components/PageLayout";
import API from "./API"

function App() {

    const [products, setProducts] = useState([]);
    const [profile, setProfile] = useState([]);
    const [mode, setMode] = useState('show');
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(true);
    
    async function loadProducts(){
            setLoading(true);
            let prod= [] 
            try{
              prod= await API.readProducts();
              setProducts(prod);
            }catch(e){
              throw e;
            }
            setLoading(false);
    };

    const readProfileByMail = async(email) => {
      try{
        setLoading(true); 
        let prof = await API.readProfileFromMail(email);
        setProfile(prof);
        setLoading(false);
      }catch(e){
        throw(e);
      }
    };

    const readProductByID = async(id) => {
      try{
        setLoading(true); 
        let prof = await API.readProductFromID(id);
        setProducts(prof);
        setLoading(false);
      }catch(e){
        throw(e);
      }
    };

    const addProfile = async (profile) => {
        try {
          setLoading(true);
          await API.addProfile(profile);
          setLoading(false);
        } catch (e) {
          throw (e);
        }
      }

      const editProfile = async (profile) => {
        try {
          setLoading(true);
          await API.editProfile(profile);
          setProfile([]);
          setLoading(false);
        } catch (e) {
          throw (e);
        }
      }

    useEffect(() => {
        loadProducts();
      }, []);

    return (
      <div>
        {message && <Row>
        <Alert variant={message.type} onClose={() => setMessage('')} dismissible>{message.msg}</Alert>
        </Row>}
        <BrowserRouter>
          <Routes>
              <Route element={<AppLayout />}>
                  <Route path='/' element={<HomePage />} />
                  <Route path='/products' element={<ProductsPage products={products} />} />
                  <Route path='/profiles' element={<ProfilesPage mode={mode} loading={loading} setMode={setMode} profile={profile} setProfile={setProfile} readProfileByMail={readProfileByMail}/>} />
                  <Route path='/addProfile' element={<AddProfilePage mode={mode} setMode={setMode} addProfile={addProfile} />} />
                  <Route path='/editProfile' element={<EditProfilePage mode={mode} loading={loading} setMode={setMode} profile={profile} editProfile={editProfile} />} />
                  <Route path='*' element={<h1>404 Page not found</h1>} />
              </Route>
          </Routes>
        </BrowserRouter >
      </div>
      
  );
}

export default App;
