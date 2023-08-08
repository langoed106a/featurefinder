import React, {Component} from "react";
import {Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import './sidebar.css'

class SideBarDocument extends Component {
    constructor(props) {
        super(props)
    }   

    render() {
       return (
          <Nav className="d-none d-md-block bg-light sidebar">
             <div className="sidebar-sticky"></div>
               <Nav.Item>
                   <Link className="nav-link" to="documentlist">Document List</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="documentnew">Document New</Link>
               </Nav.Item>
         </Nav> 
        );
    }
}
export default SideBarDocument;