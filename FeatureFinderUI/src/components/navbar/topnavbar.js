import React,{Component} from 'react';
import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function TopNavBar() {

   return (
    <div className="row">
       <div className="col-md-12">
           <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
               <Navbar.Brand href="#home">Feature Analysis</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto">
                            <Link className="nav-link" to="/documentlist">Documents</Link>
                            <Link className="nav-link" to="/featureanalysis">Analysis</Link>
                            <Link className="nav-link" to="/featurelist">Features</Link>
                            <Link className="nav-link" to="/featureexperiment">Experiment</Link>
                            <Link className="nav-link" to="/settings">Settings</Link>
                            <Link className="nav-link" to="/logout">Logout</Link>
                        </Nav>
                    </Navbar.Collapse>
               </Navbar>
         </div>
      </div>)
}
export default TopNavBar;