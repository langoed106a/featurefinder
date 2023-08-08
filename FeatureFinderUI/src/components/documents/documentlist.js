import React, {useState, useEffect} from "react";
import {Accordion, Col, Container, Row} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import DocumentSideBar from '../navbar/documentsidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function DocumentList() {
    const[spinner, setSpinner] = useState(false)
    const populate_documentlist = useStoreActions((actions) => actions.get_document_list)
    const document_list = useStoreState(state => state.documentlist)

    const show_document_list =  document_list.map((doc, i) =>
          <Row key={i}>
             <Col>
                  {doc.name}
             </Col> 
             <Col>
               {doc.description}
             </Col>  
             <Col>
               {doc.type}
             </Col>   
          </Row>);

   useEffect(() => {
      populate_documentlist();
   }, [populate_documentlist]);

    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <DocumentSideBar />
            </Col>
            <Col>
            <Container>
               <Row>
                  <Col className="col-md-10">
                     <img src={words_img} width={"100%"} height={"250px"} alt='word puzzle'/>
                  </Col>
               </Row>
               <Row>
            <Col className="col-md-10">
         <Accordion defaultActiveKey="0">
            <Accordion.Item eventKey="0">
               <Accordion.Header>
                  Existing documents
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     {show_document_list}
                  </Container>
               </Accordion.Body>
            </Accordion.Item>
         </Accordion>
       </Col>
       </Row>
       </Container>
       </Col>
      </Row>
   </Container>
 </div>)
}
export default DocumentList;