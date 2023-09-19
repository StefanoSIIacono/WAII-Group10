import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ApiResponse, ProductDTO } from '../../types';
import { createAppAsyncThunk } from '../hooks';

// Define a type for the slice state
interface ProductsState {
  loading: boolean;
  products: ProductDTO[];
}

const initialState: ProductsState = {
  loading: false,
  products: []
};

export const searchProductsThunk = createAppAsyncThunk(
  'searchProducts',
  async (/*name: string*/) => {
    //return searchProductsByName(name);
    return {
      success: true,
      statusCode: 200,
      data: [
        {
          productId: 'id1',
          name: 'name1',
          brand: 'brand1'
        },
        {
          productId: 'id2',
          name: 'name2',
          brand: 'brand2'
        },
        {
          productId: 'id3',
          name: 'name3',
          brand: 'brand3'
        },
        {
          productId: 'id4',
          name: 'name4',
          brand: 'brand4'
        }
      ]
    };
  }
);

export const counterSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    updateProducts: (state, action: PayloadAction<ApiResponse<ProductDTO[]>>) => {
      state.loading = false;
      if (action.payload.success && action.payload.data) {
        state.products = action.payload.data;
      }
    },
    startRequest: (state) => {
      state.loading = true;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(searchProductsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(searchProductsThunk.fulfilled, (_, action) => {
      updateProducts(action.payload);
    });
  }
});

export const { startRequest, updateProducts } = counterSlice.actions;

export default counterSlice.reducer;
