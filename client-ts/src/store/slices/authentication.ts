import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { LoginDTO, MeDTO } from '../../types';
import { getMe, login } from '../../utils/Api';
import { createAppAsyncThunk } from '../hooks';

// Define a type for the slice state
interface AuthenticationState {
  loading: boolean;
  authenticated: boolean;
  user?: MeDTO;
}

const initialState: AuthenticationState = {
  loading: false,
  authenticated: false
};

export const checkAuthentication = createAppAsyncThunk('checkAuthentication', async () => {
  return getMe();
});

export const loginUser = createAppAsyncThunk('login', async (loginDTO: LoginDTO, ThunkApi) => {
  const loginrequest = await login(loginDTO);
  if (!loginrequest.success) {
    return loginrequest;
  }
  ThunkApi.dispatch(checkAuthentication());
});

export const counterSlice = createSlice({
  name: 'authentication',
  initialState,
  reducers: {
    authenticate: (state, action: PayloadAction<MeDTO>) => {
      state.loading = false;
      state.authenticated = true;
      state.user = action.payload;
    },
    logout: (state) => {
      state.loading = false;
      state.authenticated = false;
      state.user = undefined;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(checkAuthentication.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(checkAuthentication.fulfilled, (_, action) => {
      if (action.payload.success && action.payload.data) {
        authenticate(action.payload.data);
        return;
      }
      logout();
    });
    builder.addCase(loginUser.pending, (state) => {
      state.loading = true;
    });
  }
});

export const { authenticate, logout } = counterSlice.actions;

export default counterSlice.reducer;
