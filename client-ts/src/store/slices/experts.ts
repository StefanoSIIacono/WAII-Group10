import { createSlice } from '@reduxjs/toolkit';
import { ExpertDTO } from '../../types';
import { getExperts } from '../../utils/Api';
import { createAppAsyncThunk } from '../hooks';
import { addError } from './errors';

// Define a type for the slice state
interface ExpertsState {
  loading: boolean;
  currentPage: number;
  totalPages: number;
  experts: ExpertDTO[];
}

const initialState: ExpertsState = {
  loading: false,
  currentPage: 1,
  totalPages: 10,
  experts: []
};

export const getExpertsThunk = createAppAsyncThunk('getExperts', async (_, { dispatch }) => {
  const experts = await getExperts();
  if (!experts.success) {
    dispatch(
      addError({
        errorTitle: 'Network Error',
        errorDescription: experts.error!,
        errorCode: experts.statusCode.toString()
      })
    );
  }
  return experts;
});

export const getCurrentExpertsThunk = createAppAsyncThunk(
  'getCurrentExperts',
  async (_, { dispatch, getState }) => {
    const currentPage = getState().experts.currentPage;
    const experts = await getExperts(currentPage);
    if (!experts.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: experts.error!,
          errorCode: experts.statusCode.toString()
        })
      );
    }
    return experts;
  }
);

export const getNextExpertsThunk = createAppAsyncThunk(
  'getNextExperts',
  async (_, { dispatch, getState }) => {
    const currentPage = getState().experts.currentPage;
    const experts = await getExperts(currentPage + 1);
    if (!experts.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: experts.error!,
          errorCode: experts.statusCode.toString()
        })
      );
    }
    return experts;
  }
);

export const getPreviousExpertsThunk = createAppAsyncThunk(
  'getPreviousRxperts',
  async (_, { dispatch, getState }) => {
    const currentPage = getState().experts.currentPage;
    const experts = await getExperts(Math.max(currentPage - 1, 1));
    if (!experts.success) {
      dispatch(
        addError({
          errorTitle: 'Network Error',
          errorDescription: experts.error!,
          errorCode: experts.statusCode.toString()
        })
      );
    }
    return experts;
  }
);

export const counterSlice = createSlice({
  name: 'experts',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(getExpertsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getNextExpertsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getPreviousExpertsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getCurrentExpertsThunk.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(getExpertsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.experts = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(getNextExpertsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.experts = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(getPreviousExpertsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.experts = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
    builder.addCase(getCurrentExpertsThunk.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.success && action.payload.data?.data) {
        state.experts = action.payload.data.data;
        state.currentPage = action.payload.data.meta.currentPage;
        state.totalPages = action.payload.data.meta.totalPages;
      }
    });
  }
});

export default counterSlice.reducer;
