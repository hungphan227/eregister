import { createStore, applyMiddleware, combineReducers } from "redux";
import thunk from 'redux-thunk';

function logger({ getState }) {
    return next => action => {
        console.log('action', action)
        console.log('state when action is dispatched', getState());
        const returnVal = next(action);
        return returnVal;
    }
}

const rootReducers = combineReducers({
})

export const store = () => createStore(rootReducers, applyMiddleware(thunk))