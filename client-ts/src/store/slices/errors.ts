import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { ErrorType } from '../../types';

// Define a type for the slice state
interface ErrorsState {
  error?: ErrorType;
  errorQueue: ErrorType[];
}

const initialState: ErrorsState = {
  errorQueue: []
};

export const counterSlice = createSlice({
  name: 'errors',
  initialState,
  reducers: {
    addError: (state, action: PayloadAction<ErrorType>) => {
      if (+action.payload.errorCode < -1) {
        return;
      }
      if (!state.error) {
        state.error = action.payload;
        return;
      }
      state.errorQueue = [action.payload, ...state.errorQueue];
    },
    acknowledgeError: (state) => {
      state.error = state.errorQueue.pop();
    }
  }
});

export const { addError, acknowledgeError } = counterSlice.actions;

export default counterSlice.reducer;
