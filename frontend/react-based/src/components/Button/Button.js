import React from 'react'
import './Button.scss'

class Button extends React.Component {
    render() {
        let { name, handleClick } = this.props

        return(
            <button className='button' onClick={handleClick}>{name}</button>
        )
    }
}

export default Button