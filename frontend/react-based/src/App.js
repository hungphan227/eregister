import React from 'react';
import './assets/styles/eregister.scss'
import Popup from './screens/Popup';
import Notification from './components/Notification/Notification';
import WebSocketHandler from './WebSockerHandler';
import Routes from './routes/Routes';

function App() {
  return (
    <div>
      <Routes/>
      <Popup/>
      <Notification/>
      <WebSocketHandler/>
    </div>
  )
}

export default App;
