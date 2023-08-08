import React, {Component} from 'react';
import {Link} from 'react-router'
import {Navbar, NavItem} from 'react-bootstrap';

class Header extends Component {
  render(){
    return (
    <div>
      <Navbar brand='logo' right>
        <NavItem><Link to="/Home" activeClassName="active">Home</Link></NavItem>
        <NavItem><Link to="/Sign-In" activeClassName="active">Sign In</Link></NavItem>
        <NavItem><Link to="/Register" activeClassName="active">Register</Link></NavItem>
      </Navbar>
    </div>
    )
  }
}
export default Header;