import React from 'react'
import eregister from '../assets/images/eregister.png'
import { Logout32 } from '@carbon/icons-react';
import service from '../service/Service';

class Header extends React.Component {

    handleClickLogout = () => {
        service.logout();
    }

    render() {
        return(
            <div className='header'>
                <img src={eregister} alt='logo' className='header-logo' />
                <div className='header-button'>Find Teacher</div>
                <div className='header-button'>Messages</div>
                <div className='header-button'>Settings</div>
                <Logout32 className='header-logout' onClick={this.handleClickLogout} />
            </div>
        )
    }
}

export default Header