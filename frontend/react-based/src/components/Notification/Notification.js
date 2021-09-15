import React from 'react'
import { CheckmarkFilled32, Misuse32 } from '@carbon/icons-react'
import './Notification.scss'

class Notification extends React.Component {

    static notification

    constructor(props) {
        super(props)
        Notification.notification = this

        this.state= {
            show: false,
            data: {},
            type: ''
        }
    }

    static show(data, type='success') {
        Notification.notification.setState({show: true, data: data, type: type})
        setTimeout(() => {
            Notification.notification.setState({show: false})
        }, 3000)
    }

    render() {
        if (!this.state.show) {
            return (<div></div>)
        }

        return(
            <div className={'notification notification-' + this.state.type}>
                <div className='notification-first-column'>
                    {this.state.type==='success'?<CheckmarkFilled32 className='checkmark'/>:<Misuse32 className='checkmark'/>}
                </div>
                <div>
                    {this.state.data}
                </div>
            </div>
        )
    }

}

export default Notification