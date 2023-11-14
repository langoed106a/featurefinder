import React, {Component} from "react";
import {Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import './documentsidebar.css'

function DocumentSideBar() {

       return (
          <Nav className="d-none d-md-block bg-light sidebar">
             <div className="sidebar-sticky"></div>
               <Nav.Item>
                   <Link className="nav-link" to="/documentnew">New Document</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/documentlist">Document list</Link>
               </Nav.Item>
         </Nav> 
        );
}
export default DocumentSideBar;