import React from 'react'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Card from "react-bootstrap/Card"
import { Button, Modal } from 'react-bootstrap'

class ClassRegistration extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            courses: [],
            showPopup: false,
            popupContent: ''
        }
        this.getCourses()
    }

    render() {
        let columns = [];
        this.state.courses.forEach((course) => {
            columns.push(
                <Col>
                    <Card style={{ width: '18rem', marginTop: '50px'}}>
                        <Card.Body>
                            <Card.Title>{course.courseName}</Card.Title>
                            <Card.Text>
                                Course Number: {course.courseNumber}
                            </Card.Text>
                            <Card.Text>
                                Limit: {course.limit}
                            </Card.Text>
                            <Button variant="primary" onClick={() => {this.joinCourse(course.id)}}>Join</Button>
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
            if (res.status === 200) {
                res.json().then(data => {
                    this.setState({courses: data})
                })
            } else {
                this.setState({})
            }
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
                    console.log(data)
                    this.setState({popupContent: data})
                })
                this.setState({showPopup: true})
            } else {
                console.log('Error with status ' + res.status)
            }
        }).catch(error => {
            console.log(error)
        })
    }

}

export default  ClassRegistration