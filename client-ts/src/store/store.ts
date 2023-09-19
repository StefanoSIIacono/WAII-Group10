import { configureStore } from '@reduxjs/toolkit';
import authenticationReducer from './slices/authentication';
import ticketsReducer from './slices/tickets';

export const store = configureStore({
  reducer: {
    authenticate: authenticationReducer,
    tickets: ticketsReducer
  }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
