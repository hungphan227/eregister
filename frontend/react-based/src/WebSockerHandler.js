import React from 'react'
import * as EventBus from "vertx3-eventbus-client";
import { COMMANDS, INTERNAL_EVENTS } from './constants/Constants'
import internalEventBus from './InternalEventBus'

class WebSocketHandler extends React.Component {

    render() {
        return(<div></div>)
    }

    componentDidMount() {
        this.createWebSocket();
    }

    createWebSocket = () => {
        let options = {
            vertxbus_reconnect_attempts_max: Infinity, // Max reconnect attempts
            vertxbus_reconnect_delay_min: 1000, // Initial delay (in ms) before first reconnect attempt
            vertxbus_reconnect_delay_max: 5000, // Max delay (in ms) between reconnect attempts
            vertxbus_reconnect_exponent: 2, // Exponential backoff factor
            vertxbus_randomization_factor: 0.5, // Randomization factor between 0 and 1
        }

        let eventBus = new EventBus('http://localhost:9997/websocket', options)
        eventBus.enableReconnect(true)
        eventBus.onopen = () => {
            console.log('Open new connection with websocket server');
            eventBus.registerHandler('remainingSlots', (error, input) => {
                console.log('Receive from web socket server: ', input)
                let message = JSON.parse(input.body)
                switch (message.type) {
                    case 'COMMAND':
                        this.handleCommand(message.content, message.extraData)
                        break
                    default:
                        console.error('the type has not been found')
                }
            });

            eventBus.send('in', {msg: 'Hi!'})
        }
    }

    handleCommand(command, data) {
        switch (command) {
            case COMMANDS.GET_COURSE:
                internalEventBus.dispatch(INTERNAL_EVENTS.GET_COURSE, data)
                break
            default:
                console.error('the command has not been found')
        }
    }
    
}

export default WebSocketHandler