import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import {useNavigate} from 'react-router-dom';
import TopNavBar from '../navbar/topnavbar';
import DocumentSideBar from '../navbar/documentsidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function DataUpload() {
    const[spinner, setSpinner] = useState(false)
    const upload_file = useStoreActions(actions => actions.upload_file)
    const name = React.createRef()
    const type = React.createRef()
    const description = React.createRef()
    const content = React.createRef()
    const navigate = useNavigate()

    const handleFileUpload = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (e) => {
          const formData = new FormData();
          formData.append("file", file);
          upload_file(formData)
        };
        navigate("/");
      };

    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <DocumentSideBar />
            </Col>
            <Col>
              <Container>
                  <input type="file" onChange={handleFileUpload} />    
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default DataUpload;