import { createSlice } from '@reduxjs/toolkit';
import { TicketDTO } from '../../types';
import { getTicket, getTickets } from '../../utils/Api';
import { createAppAsyncThunk } from '../hooks';
import { addError } from './errors';

// Define a type for the slice state
interface TicketsState {
  loading: boolean;
  currentPage: number;
  totalPages: number;
  tickets: TicketDTO[];
}

const initialState: TicketsState = {
  loading: false,
  currentPage: 1,
  totalPages: 10,
  tickets: []
};

export const getLastTicketsThunk = createAppAsyncThunk(
  'getLastTickets',
  async (_, { dispatch }) => {
    const tickets = await getTickets();
    if (!tickets.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: tickets.error!,
          errorCode: tickets.statusCode.toString()
        })
      );
    }
    return tickets;
  }
);

export const getCurrentPageTicketsThunk = createAppAsyncThunk(
  'getCurrentPageTickets',
  async (_, { dispatch, getState }) => {
    const currentPage = getState().tickets.currentPage;
    const tickets = await getTickets(currentPage);
    if (!tickets.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: tickets.error!,
          errorCode: tickets.statusCode.toString()
        })
      );
    }
    return tickets;
  }
);

export const getNextTicketsThunk = createAppAsyncThunk(
  'getNextTickets',
  async (_, { dispatch, getState }) => {
    const currentPage = getState().tickets.currentPage;
    const tickets = await getTickets(currentPage + 1);
    if (!tickets.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: tickets.error!,
          errorCode: tickets.statusCode.toString()
        })
      );
    }
    return getTickets(currentPage + 1);
  }
);

export const getPreviousTicketsThunk = createAppAsyncThunk(
  'getPreviousTickets',
  async (_, { dispatch, getState }) => {
    const currentPage = getState().tickets.currentPage;
    const tickets = await getTickets(Math.max(currentPage - 1, 1));
    if (!tickets.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: tickets.error!,
          errorCode: tickets.statusCode.toString()
        })
      );
    }
    return tickets;
  }
);

export const updateTicketThunk = createAppAsyncThunk(
  'updateTicket',
  async (id: number, { dispatch }) => {
    const ticket = await getTicket(id);
    if (!ticket.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: ticket.error!,
          errorCode: ticket.statusCode.toString()
        })
      );
    }
    return ticket;
  }
);

export const counterSlice = createSlice({
  name: 'tickets',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getLastTicketsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getCurrentPageTicketsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getNextTicketsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getPreviousTicketsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getLastTicketsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.tickets = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(getCurrentPageTicketsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.tickets = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(getNextTicketsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.tickets = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(getPreviousTicketsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.tickets = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(updateTicketThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data) {
        const newTicket = action.payload.data;
        state.tickets = state.tickets.map((t) => (t.id === newTicket.id ? newTicket : t));
      }
    });
  }
});

// export const { startRequest, updateTickets, updateTicket } = counterSlice.actions;

export default counterSlice.reducer;
