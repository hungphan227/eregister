import React from 'react'
import Header from './Header'
import InformationCard from '../components/InformationCard'
import Notification from '../components/Notification/Notification'
import computerScience from '../assets/images/computer-science.png'
import * as EventBus from 'vertx-bus-client'
import Popup from './Popup'

class CourseRegistration extends React.Component {

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

    handleClickBook(course) {
        course.handleClickButton = () => {
            this.joinCourse(course.id)
        }

        Popup.show(course)
    }

    render() {
        return (
            <div>
                <Header></Header>
                <div className='eregister-body'>
                    {this.renderInformationCards()}
                </div>
            </div>
        )
    }

    renderInformationCards() {
        return(
            <div>
                {
                    this.state.courses.map((course, idx) => {
                        return(
                            <InformationCard data={course} handleClickButton={()=>this.handleClickBook(course)} />
                        )
                    })
                }
            </div>
        )
    }

    getCourses = () => {
        fetch("/courses", {
            method: 'GET'
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
            method: 'PUT'
        }).then(res => {
            console.log('joinCourse result', res)
            
                res.text().then(data => {
                    // this.setState({popupContent: data})
                    if (res.status === 200) {
                        Notification.show(data, 'success')
                    } else {
                        console.log('----------------------')
                        Notification.show(data, 'error')
                    }
                    
                })
                // this.setState({showPopup: true})
            // console.log('Error with status ' + res.status)
        }).catch(error => {
            console.error(error)
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

export default  CourseRegistration