import React from 'react'
import Button from '../components/Button';
import { Close32 } from '@carbon/icons-react'

class Popup extends React.Component {

    static popup

    constructor(props) {
        super(props)
        Popup.popup = this;

        this.state = {
            show: false,
            data: {}
        }
    }

    static show(data) {
        Popup.popup.setState({show: true, data: data})
    }

    handleClickClose = () => {
        this.setState({show: false})
    }

    handleClickConfirm = (handleClickButton) => {
        handleClickButton()
        this.setState({show: false})
    }

    render() {
        let data = this.state.data

        if (!this.state.show) {
            return(<div></div>)
        }

        return(
            <div className='popup'>
                <div className='popup-background'></div>
                <div className='popup-main'>
                    <div className='popup-main-header'>
                        Course Detail
                        <Close32 className='close-button' onClick={this.handleClickClose} />
                    </div>
                    <div className='popup-main-body'>
                        <div className='courseName'>{data.courseName}</div>
                        <div className='teacher space'>Teacher</div>
                        <div className='limit'>{data.teacher}</div>
                        <div className='teacher space'>Limit</div>
                        <div className='limit'>{data.limit}</div>
                        <div className='teacher space'>Remaining Slot</div>
                        <div className='limit'>{data.remainingSlots}</div>
                        <div className='teacher space'>Intro</div>
                        <div className='limit'>{data.description}</div>
                    </div>
                    <div className='popup-main-footer'>
                        <Button name='Confirm' handleClick={()=>{this.handleClickConfirm(data.handleClickButton)}} />
                    </div>
                </div>
            </div>
        )
    }
}

export default Popup