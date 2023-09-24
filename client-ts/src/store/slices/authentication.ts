import { createSlice } from '@reduxjs/toolkit';
import { LoginDTO, MeDTO } from '../../types';
import { getMe, login, logout } from '../../utils/Api';
import { createAppAsyncThunk } from '../hooks';

// Define a type for the slice state
interface AuthenticationState {
  loading: boolean;
  authenticated: boolean;
  user?: MeDTO;
}

const initialState: AuthenticationState = {
  loading: true,
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

export const logoutUser = createAppAsyncThunk('logout', async (_, ThunkApi) => {
  const request = await logout();
  if (!request.success) {
    return request;
  }
  ThunkApi.dispatch(checkAuthentication());
});

export const counterSlice = createSlice({
  name: 'authentication',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(checkAuthentication.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(checkAuthentication.fulfilled, (state, action) => {
      if (action.payload.success && action.payload.data) {
        state.loading = false;
        state.authenticated = true;
        state.user = action.payload.data;
        return;
      }
      state.loading = false;
      state.authenticated = false;
      state.user = undefined;
    });
    builder.addCase(loginUser.pending, (state) => {
      state.loading = true;
    });
  }
});

// export const {} = counterSlice.actions;

export default counterSlice.reducer;
