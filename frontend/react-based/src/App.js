import React from 'react';
import Login from './screens/Login'
import CourseRegistration from './screens/CourseRegistration'
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import './assets/styles/eregister.scss'
import Popup from './screens/Popup';
import Notification from './components/Notification/Notification';

function App() {
  return (
    <div>
      <Router>
        <Switch>
          <Route exact path="/login" render={(props) => (<Login {...props} />)} />
          <Route path="/course-registration" render={(props) => { return (<CourseRegistration history={props.history} />) }} />
        </Switch>
      </Router>
      <Popup/>
      <Notification/>
    </div>
  )
}

export default App;
