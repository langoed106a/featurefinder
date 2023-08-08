import React, {Component} from "react";
import {Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import './documentsidebar.css'

function AnalysisSideBar() {

       return (
          <Nav className="d-none d-md-block bg-light sidebar">
             <div className="sidebar-sticky"></div>
               <Nav.Item>
                   <Link className="nav-link" to="/featureanalysis">Analyse</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/modelnew">New Model</Link>
               </Nav.Item>
               <Nav.Item>
                   <Link className="nav-link" to="/confusionmatrix">Confusion Matrix</Link>
               </Nav.Item>
         </Nav> 
        );
}
export default AnalysisSideBar;