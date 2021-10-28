import React from 'react'
import Card from 'react-bootstrap/Card'
import {Form} from 'react-bootstrap'
import Button from 'react-bootstrap/Button'
import 'bootstrap/dist/css/bootstrap.min.css'
import service from '../service/Service'
import { history } from '../routes/Routes'
import { SCREEN_NAMES, INTERNAL_EVENTS } from '../constants/Constants'
import internalEventBus from '../InternalEventBus'

class Login extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            showLoginError: false,
            username: '',
            password: ''
        }
    }

    componentDidMount() {
        service.checkAuthentication(() => {
            history.push(SCREEN_NAMES.SCREEN_COURSE_REGISTRATION)
        })
    }

    handleClickLogin = () => {
        service.login(this.state.username, this.state.password, () => {
            internalEventBus.dispatch(INTERNAL_EVENTS.OPEN_WEB_SOCKET, {})
            history.push(SCREEN_NAMES.SCREEN_COURSE_REGISTRATION)
        }, () => {
            this.setState({
                showLoginError: true
            })
        })
    }

    render() {
        return (
            <div style={{ width: '500px', margin: 'auto', marginTop: '300px' }}>
                <Card>
                    <Card.Body>
                        <Form>
                            <Form.Group controlId="formBasicEmail">
                                <Form.Label>Username</Form.Label>
                                <Form.Control type="text" placeholder="Enter username" onChange={(event) => {this.state.username = event.target.value}}/>
                            </Form.Group>

                            <Form.Group controlId="formBasicPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control type="password" placeholder="Enter password" onChange={(event) => {this.state.password = event.target.value}}/>
                            </Form.Group>

                            <Button variant="secondary" onClick={this.handleClickLogin}>
                                Login
                            </Button>
                        </Form>
                    </Card.Body>
                </Card>
                {this.state.showLoginError?<i style={{color:'red'}}>Wrong username or password</i>:''}
            </div>
        )
    }

    // submit = () => {
    //     return fetch("/login", {
    //         method: 'POST',
    //         headers: {
    //             "Content-Type": "application/x-www-form-urlencoded",
    //         },
    //         mode: 'no-cors',
    //         body: 'username=' + this.state.username + '&password=' + this.state.password
    //     }).then(res => {
    //         console.log(res)
    //         if (res.status === 200) {
    //             this.props.history.push('/course-registration')
    //         } else {
    //             this.setState({
    //                 showLoginError: true
    //             })
    //         }
    //     }).catch(error => {
    //         console.log(error)
    //     })
    // }
}

export default Login