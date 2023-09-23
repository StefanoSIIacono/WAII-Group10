import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ApiResponse, ExpertDTO, PagedDTO } from '../../types';
import { getExperts } from '../../utils/Api';
import { createAppAsyncThunk } from '../hooks';

// Define a type for the slice state
interface ExpertsState {
  loading: boolean;
  currentPage: number;
  experts: ExpertDTO[];
}

const initialState: ExpertsState = {
  loading: false,
  currentPage: 1,
  experts: Array(20)
    .fill({
      email: 'expert@email.com',
      name: 'expertName',
      surname: 'expertSurname',
      expertises: [{ field: 'ciao' }, { field: 'salut' }]
    })
    .map((t, i) => ({
      ...t,
      obj: `obj${i}`,
      id: i
    }))
};

export const getExpertsThunk = createAppAsyncThunk('getExperts', async () => {
  return getExperts();
});

export const getNextExpertsThunk = createAppAsyncThunk('getNextExperts', async (_, ThunkApi) => {
  const currentPage = ThunkApi.getState().experts.currentPage;
  return getExperts(currentPage + 1);
});

export const getPreviousExpertsThunk = createAppAsyncThunk(
  'getPreviousRxperts',
  async (_, ThunkApi) => {
    const currentPage = ThunkApi.getState().experts.currentPage;
    return getExperts(Math.max(currentPage - 1, 1));
  }
);

export const counterSlice = createSlice({
  name: 'experts',
  initialState,
  reducers: {
    updateTickets: (state, action: PayloadAction<ApiResponse<PagedDTO<ExpertDTO>>>) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.experts = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
      }
    },
    startRequest: (state) => {
      state.loading = true;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(getExpertsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(getNextExpertsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(getPreviousExpertsThunk.pending, () => {
      startRequest();
    });
    builder.addCase(getExpertsThunk.fulfilled, (_, action) => {
      updateTickets(action.payload);
    });
    builder.addCase(getNextExpertsThunk.fulfilled, (_, action) => {
      updateTickets(action.payload);
    });
    builder.addCase(getPreviousExpertsThunk.fulfilled, (_, action) => {
      updateTickets(action.payload);
    });
  }
});

export const { startRequest, updateTickets } = counterSlice.actions;

export default counterSlice.reducer;
