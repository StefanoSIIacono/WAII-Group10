import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {Profile} from "./Components/Profile";
import { useEffect, useState } from 'react';
import {Row, Alert} from 'react-bootstrap'
import {AppLayout, HomePage, ProfilesPage, ProductsPage, AddProfilePage, EditProfilePage} from "./Components/PageLayout";
import API from "./API"

function App() {

    const [products, setProducts] = useState([]);
    const [profiles, setProfile] = useState([]);
    const [mode, setMode] = useState('show');
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(true);
    const [editedProfile, setEditedProfile] = useState(new Profile('newemail@polito.it', 'nuova password'));
    
    async function loadProducts(){
            setLoading(true);
            let prod= [] 
            prod= await API.readProducts();
            setProducts(prod);
            setLoading(false);
    };

    async function readProfileByMail(){
      setLoading(true); 
      let prof = await API.readProfileFromMail();
      setProfile(prof);
      setLoading(false);
};    

    async function readProductByID(){
      setLoading(true); 
      let prof = await API.readProductFromID();
      setProfile(prof);
      setLoading(false);
    };    

    const addProfile = async (profile) => {
        try {
          setLoading(true);
          await API.addProfile(profile);
          //reloadFilms(filt);
          setLoading(false);
        } catch (e) {
          throw (e);
        }
      }

      const editProfile = async (profile) => {
        try {
          setLoading(true);
          await API.editProfile(profile);
          //reloadFilms(filt);
          setLoading(false);
        } catch (e) {
          throw (e);
        }
      }

      const openEdit = (profile) => {
        setEditedProfile(profile)
        setMode('edit')
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
                  <Route path='/profiles' element={<ProfilesPage mode={mode} setMode={setMode} profiles={profiles} />} />
                  <Route path='/addProfile' element={<AddProfilePage mode={mode} setMode={setMode} addProfile={addProfile} />} />
                  <Route path='/editProfile' element={<EditProfilePage mode={mode} setMode={setMode} editProfile={editProfile} editedProfile={editedProfile} />} />
                  <Route path='*' element={<h1>404 Page not found</h1>} />
              </Route>
          </Routes>
        </BrowserRouter >
      </div>
      
  );
}

export default App;
