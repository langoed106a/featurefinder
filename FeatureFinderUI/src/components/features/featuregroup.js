import React, {useState, useEffect} from "react";
import {Accordion, Col, Container, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureGroup() {
    const[spinner, setSpinner] = useState(false)
    const populate_featuregrouplist = useStoreActions((actions) => actions.get_featuregroup_list)
    const featuregroup_arr = useStoreState(state => state.featuregrouplist)

    const show_feature_group_list =  featuregroup_arr.map((doc, i) =>
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
      populate_featuregrouplist();
   }, [populate_featuregrouplist]);

    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <FeatureSideBar />
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
                  Existing Feature Groups
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     {show_feature_group_list}
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
export default FeatureGroup;