import React, {Component} from "react";
import {Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import './featuresidebar.css'

function FeatureSideBar() {

       return (
          <Nav className="d-none d-md-block bg-light sidebar">
             <div className="sidebar-sticky"></div>
               <Nav.Item>
                   <Link className="nav-link" to="/featurenew">New Feature</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/featuregroup">Feature Groups</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/featuresearch">Feature Search</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/featureimport">Import Features</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/featuregroupnew">New Group</Link>
               </Nav.Item>
         </Nav> 
        );
}
export default FeatureSideBar;