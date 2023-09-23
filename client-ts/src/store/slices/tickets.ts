import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ApiResponse, PagedDTO, Roles, Status, TicketDTO } from '../../types';
import { getTicket, getTickets } from '../../utils/Api';
import { createAppAsyncThunk } from '../hooks';

// Define a type for the slice state
interface TicketsState {
  loading: boolean;
  currentPage: number;
  tickets: TicketDTO[];
}

const initialState: TicketsState = {
  loading: false,
  currentPage: 1,
  tickets: Array(20)
    .fill({
      id: 1,
      obj: 'ciao',
      arg: {
        field: 'computers'
      },
      profile: 'this',
      product: 'a product',
      status: {
        status: Status.OPEN,
        statusChanger: Roles.EXPERT,
        timestamp: '1'
      }
    })
    .map((t, i) => ({
      ...t,
      obj: `obj${i}`,
      id: i
    }))
};

export const getLastTicketsThunk = createAppAsyncThunk('getLastTickets', async () => {
  return getTickets();
});

export const getNextTicketsThunk = createAppAsyncThunk('getNextTickets', async (_, ThunkApi) => {
  const currentPage = ThunkApi.getState().tickets.currentPage;
  return getTickets(currentPage + 1);
});

export const getPreviousTicketsThunk = createAppAsyncThunk(
  'getPreviousTickets',
  async (_, ThunkApi) => {
    const currentPage = ThunkApi.getState().tickets.currentPage;
    return getTickets(Math.max(currentPage - 1, 1));
  }
);

export const updateTicketThunk = createAppAsyncThunk('updateTicket', async (id: number) => {
  return getTicket(id);
});

export const counterSlice = createSlice({
  name: 'tickets',
  initialState,
  reducers: {
    updateTickets: (state, action: PayloadAction<ApiResponse<PagedDTO<TicketDTO>>>) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.tickets = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
      }
    },
    updateTicket: (state, action: PayloadAction<ApiResponse<TicketDTO>>) => {
      state.loading = false;
      if (action.payload.success && action.payload.data) {
        const newTicket = action.payload.data;
        state.tickets = state.tickets.map((t) => (t.id === newTicket.id ? newTicket : t));
      }
    },
    startRequest: (state) => {
      state.loading = true;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(getLastTicketsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(getNextTicketsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(getPreviousTicketsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(getLastTicketsThunk.fulfilled, (_, action) => {
      updateTickets(action.payload);
    });
    builder.addCase(getNextTicketsThunk.fulfilled, (_, action) => {
      updateTickets(action.payload);
    });
    builder.addCase(getPreviousTicketsThunk.fulfilled, (_, action) => {
      updateTickets(action.payload);
    });
    builder.addCase(updateTicketThunk.fulfilled, (_, action) => {
      updateTicket(action.payload);
    });
  }
});

export const { startRequest, updateTickets, updateTicket } = counterSlice.actions;

export default counterSlice.reducer;
