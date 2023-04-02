import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {Product} from "./Components/Product";
import {Profile} from "./Components/Profile";
import {AppLayout, HomePage, ProfilesPage, ProductsPage} from "./Components/PageLayout";
//import API from "./API"

function App() {
    const prod = [ new Product("ciao", "sono un", "prodotto"), new Product("4935531465706","JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976","JMT"), new Product("4935531465706","JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976","JMT"), new Product("4935531465706","JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976","JMT"), new Product("4935531465706","JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976","JMT")]
    const prof = [new Profile("uwu", "leoamantedelgranturco@gmail.com", "pandizenzero")]

    return (
      <BrowserRouter>
          <Routes>
              <Route element={<AppLayout />}>
                  <Route path='/' element={<HomePage />} />
                  <Route path='/products' element={<ProductsPage products={prod}/>} />
                  <Route path='/profiles' element={<ProfilesPage profiles={prof}/>} />
                  <Route path='*' element={<h1>404 Page not found</h1>} />
              </Route>
          </Routes>
      </BrowserRouter >
  );
}

export default App;
