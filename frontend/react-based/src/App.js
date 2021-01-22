import React from 'react';
import Login from './components/Login'
import ClassRegistration from './components/ClassRegistration'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import { Provider } from 'react-redux'
import {store} from "./store/Store";

function App() {
  return (
      <Provider store={store()}>
        <Router>
          <Switch>
            <Route exact path="/login" render={(props) => (<Login {...props} />) } />
            <Route path="/class-registration" render={(props) => {return (<ClassRegistration history={props.history} />)} } />
          </Switch>
        </Router>
      </Provider>
  );
}

export default App;
