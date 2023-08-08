import React, {useState, useEffect} from "react";
import {Accordion, Col, Container, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import DocumentSideBar from '../navbar/documentsidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function DocumentGroup() {
    const[spinner, setSpinner] = useState(false)
    const populate_documentgrouplist = useStoreActions((actions) => actions.get_documentgroup_list)
    const documentgroup_arr = useStoreState(state => state.documentgrouplist)

    const show_documentgroup_list =  documentgroup_arr.map((doc, i) =>
          <Row key={i}>
             <Col xs={2}>
               {doc.name}
             </Col> 
             <Col xs={6}>
               {doc.description}
             </Col>  
             <Col xs={2}>
               {doc.type}
             </Col>   
          </Row>);

   useEffect(() => {
      populate_documentgrouplist();
   }, [populate_documentgrouplist]);

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
                  Existing Document Groups
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     {show_documentgroup_list}
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
export default DocumentGroup;