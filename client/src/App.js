import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {Profile} from "./Components/Profile";
import { useEffect, useState } from 'react';
import {AppLayout, HomePage, ProfilesPage, ProductsPage} from "./Components/PageLayout";
import API from "./API"

function App() {

    const [products, setProducts] = useState([]);
    //const [profiles, setProfiles] = useState([]);
    //const [loading, setLoading] = useState(true);

    
    async function loadProducts(){
            //setLoading(true);
            let prod= [] 
            prod= await API.readProducts();
            setProducts(prod);
            //setLoading(false);
    };    

    const prof = [new Profile("uwu", "leoamantedelgranturco@gmail.com", "pandizenzero")]

    /*const addProfile = async (profile) => {
        try {
          //setLoading(true);
          await API.addProfile(profile);
          //reloadFilms(filt);
          //setLoading(false);
        } catch (e) {
          throw (e);
        }
      }
*/
    useEffect(() => {
        loadProducts();
      }, []);

    return (
      <BrowserRouter>
          <Routes>
              <Route element={<AppLayout />}>
                  <Route path='/' element={<HomePage />} />
                  <Route path='/products' element={<ProductsPage products={products} />} />
                  <Route path='/profiles' element={<ProfilesPage profiles={prof} />} />
                  <Route path='*' element={<h1>404 Page not found</h1>} />
              </Route>
          </Routes>
      </BrowserRouter >
  );
}

export default App;
