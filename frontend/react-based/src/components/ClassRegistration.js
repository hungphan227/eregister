import React from 'react'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Card from "react-bootstrap/Card"
import { Button, Modal } from 'react-bootstrap'
import * as EventBus from 'vertx-bus-client'

class ClassRegistration extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            courses: [],
            showPopup: false,
            popupContent: ''
        }
        this.getCourses()
        this.callSocket()
    }

    render() {
        let columns = [];
        this.state.courses.forEach((course) => {
            columns.push(
                <Col>
                    <Card style={{ width: '18rem', marginTop: '50px'}}>
                        <Card.Body>
                            <Card.Title style={{color: 'purple'}}>{course.courseName}</Card.Title>
                            <Card.Text>
                                <div style={{textAlign: 'center', fontSize: '50px', color: 'yellowgreen'}}><b>{course.remainingSlots}</b></div>
                            </Card.Text>
                            <Card.Text>
                                Course Number: {course.courseNumber}
                            </Card.Text>
                            <Card.Text>
                                Limit: {course.limit}
                            </Card.Text>
                            <Button variant="secondary" style={{marginLeft: '40%'}} onClick={() => {this.joinCourse(course.id)}}>Join</Button>
                        </Card.Body>
                    </Card>
                </Col>
            )
        })

        return (
            <div>
                <Container>
                    <Row>
                        {columns}
                    </Row>
                </Container>
                <Modal show={this.state.showPopup} onHide={() => {this.setState({showPopup: false})}}>
                    <Modal.Body>{this.state.popupContent}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="primary" onClick={() => {this.setState({showPopup: false})}}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }

    getCourses = () => {
        fetch("/courses", {
            method: 'GET',
            mode: 'no-cors'
        }).then(res => {
            console.log(res)
            if (res.status === 200) {
                res.json().then(data => {
                    this.setState({courses: data})
                })
                return
            }
            if (res.status === 401) {
                this.props.history.push('/login')
                return
            }
            this.setState({})
        }).catch(error => {
            console.log(error)
        })
    }

    joinCourse(courseId) {
        fetch("/join-course/"+courseId, {
            method: 'GET',
            mode: 'no-cors'
        }).then(res => {
            console.log(res)
            if (res.status === 200) {
                res.text().then(data => {
                    this.setState({popupContent: data})
                })
                this.setState({showPopup: true})
                return
            }
            console.log('Error with status ' + res.status)
        }).catch(error => {
            console.log(error)
        })
    }

    callSocket = () => {
        // let sock = new SockJS('http://localhost:9997/remainingSlots');
        // sock.onopen = function() {
        //     console.log('open web socket');
        // }

        let eventBus = new EventBus('http://localhost:9997/remainingSlots');

        eventBus.onopen = () => {
            eventBus.registerHandler('out', (error, message) => {
                // TODO - Optimize rerender
                let course = JSON.parse(message.body)
                let courses = [...this.state.courses]
                let index = courses.findIndex(c => c.courseNumber === course.courseNumber)
                courses[index] = course
                this.setState({courses: courses});
            });

            eventBus.send('in', {msg: 'Hi!'})
        }
    }

}

export default  ClassRegistration