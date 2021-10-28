import React from 'react'
import * as EventBus from "vertx3-eventbus-client";
import { COMMANDS, INTERNAL_EVENTS } from './constants/Constants'
import internalEventBus from './InternalEventBus'
import service from './service/Service';

class WebSocketHandler extends React.Component {

    eventBus
    openWebSocketEventHandler
    closeWebSocketEventHandler
    isWebSocketOpened = false

    render() {
        return(<div></div>)
    }

    componentDidMount() {
        service.getClientSessionId()

        this.openWebSocketEventHandler = (event) => {
            if (!this.isWebSockerOpened) {
                this.createWebSocket()
                this.isWebSocketOpened = true
            }
        }
        internalEventBus.on(INTERNAL_EVENTS.OPEN_WEB_SOCKET, this.openWebSocketEventHandler)

        this.closeWebSocketEventHandler = (event) => {
            this.eventBus.enableReconnect(false)
            this.eventBus.sockJSConn.close()
            this.isWebSocketOpened = false
        }
        internalEventBus.on(INTERNAL_EVENTS.CLOSE_WEB_SOCKET, this.closeWebSocketEventHandler)

        service.checkAuthentication(() => {
            if (!this.isWebSockerOpened) {
                this.createWebSocket()
                this.isWebSocketOpened = true
            }
        })

        // window.setTimeout(() => {
        //     console.log('---------------')
        //     this.eventBus.enableReconnect(false)
        //     this.eventBus.sockJSConn.close()
        //     window.setTimeout(() => {
        //         this.createWebSocket()
        //     }, 5000)
        // }, 5000)
    }

    createWebSocket = () => {
        let options = {
            vertxbus_reconnect_attempts_max: Infinity, // Max reconnect attempts
            vertxbus_reconnect_delay_min: 1000, // Initial delay (in ms) before first reconnect attempt
            vertxbus_reconnect_delay_max: 5000, // Max delay (in ms) between reconnect attempts
            vertxbus_reconnect_exponent: 2, // Exponential backoff factor
            vertxbus_randomization_factor: 0.5, // Randomization factor between 0 and 1
        }
        
        // this.eventBus = new EventBus('http://'+window.location.hostname+':9997/websocket', options)
        this.eventBus = new EventBus('/websocket', options)
        this.eventBus.enableReconnect(true)
        this.eventBus.onopen = () => {
            console.log('Open new connection with websocket server')
            this.eventBus.registerHandler('remainingSlots', (error, input) => {
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

            this.eventBus.send('in', {msg: 'Hi!'})
        }

        this.eventBus.onClose = () => {
            console.error('Cannot connect websocket server')
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