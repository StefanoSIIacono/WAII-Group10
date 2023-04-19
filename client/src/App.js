import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
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
        setProfile(prof);
        setLoading(false);
      }catch(e){
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

    useEffect(() => {
        loadProducts();
    }, []);

    useEffect(() => {
        setProduct([])
    }, []);

    return (
      <div>
        <BrowserRouter>
          <Routes>
              <Route element={<AppLayout message={message} setMessage={setMessage} />}>
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
