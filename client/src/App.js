import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {Profile} from "./Components/Profile";
import { useEffect, useState } from 'react';
import {Row, Alert} from 'react-bootstrap'
import {AppLayout, HomePage, ProfilesPage, ProductsPage, AddProfilePage, EditProfilePage, GetProductPage} from "./Components/PageLayout";
import API from "./API"

function App() {

    const [products, setProducts] = useState([]);
    const [product, setProduct] = useState([]);
    const [profile, setProfile] = useState([]);
    const [edit, setEdit] = useState(false);
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);
    
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
        let prod = await API.readProductFromID(id);
        setProduct(prod);
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
                  <Route path='/getProduct' element={<GetProductPage loading={loading} product={product} setProduct={setProduct} readProductByID={readProductByID} />} />
                  <Route path='/profiles' element={<ProfilesPage edit={edit} loading={loading} setEdit={setEdit} profile={profile} setProfile={setProfile} readProfileByMail={readProfileByMail}/>} />
                  <Route path='/addProfile' element={<AddProfilePage addProfile={addProfile} />} />
                  <Route path='/editProfile' element={<EditProfilePage edit={edit} loading={loading} setEdit={setEdit} profile={profile} editProfile={editProfile} />} />

                  <Route path='*' element={<h1>404 Page not found</h1>} />
              </Route>
          </Routes>
        </BrowserRouter >
      </div>
      
  );
}

export default App;
