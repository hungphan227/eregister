import { history } from "../routes/Routes"
import { SCREEN_NAMES, INTERNAL_EVENTS } from '../constants/Constants'
import internalEventBus from "../InternalEventBus"

const service = {
    getClientSessionId() {
        fetch("/get-client-session-id", {
            method: 'GET'
        }).then(res => {
            console.log('getClientSessionId res status:', res.status)
        }).catch(error => {
            console.error(error)
        })
    },

    login(username, password, onSuccess, onError) {
        fetch("/login", {
            method: 'POST',
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: 'username=' + username + '&password=' + password
        }).then(res => {
            console.log('login res status:', res.status)
            if (res.status === 200) {
                onSuccess()
            } else {
                onError()
            }
        }).catch(error => {
            console.error(error)
        })
    },

    checkAuthentication(onSuccess) {
        fetch("/check-authentication", {
            method: 'GET'
        }).then(res => {
            console.log('checkAuthentication res status:', res.status)
            if (res.status === 200) {
                onSuccess()
            }
        }).catch(error => {
            console.error(error)
        })
    },

    logout() {
        fetch("/logout", {
            method: 'GET'
        }).then(res => {
            console.log('logout res status:', res.status)
            if (res.status === 200) {
                internalEventBus.dispatch(INTERNAL_EVENTS.CLOSE_WEB_SOCKET, {})
                history.push(SCREEN_NAMES.SCREEN_LOGIN)
            }
        }).catch(error => {
            console.error(error)
        })
    },

    getCourses(onSuccess) {
        this.callGetRestApi('/courses', onSuccess)
    },

    getCourse(courseId, onSuccess) {
        this.callGetRestApi('/course/'+courseId, onSuccess)
    },

    joinCourse(courseId, onSuccess, onError) {
        this.callRestApi('PUT', "/join-course/"+courseId, '', onSuccess, onError)
    },

    callGetRestApi(uri, onSuccess, onError) {
        this.callRestApi('GET', uri, '', onSuccess, onError)
    },

    callRestApi(method, uri, body='', onSuccess=()=>{}, onError=()=>{}, headers={}) {
        let options = {
            method: method,
            headers: headers
        }
        if (body !== '') options.body = body
        fetch(uri, options).then(res => {
            console.log('callRestApi res status:', res.status)
            if (res.status === 401) {
                history.push(SCREEN_NAMES.SCREEN_LOGIN)
                return
            }
            res.json().then(data => {
                console.log('callRestApi data:', data)
                if (res.status === 200) {
                    onSuccess(data)
                    return
                }
                onError(data)
            })
        }).catch(error => {
            console.error(error)
        })
    }
}

export default service