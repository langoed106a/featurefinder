import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import AnalysisSideBar from '../navbar/analysissidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function ModelNew() {
    const[spinner, setSpinner] = useState(false)
    const name = React.createRef()
    const description = React.createRef()
    const feature_list = React.createRef()
    const matrix = React.createRef()

   const perform_add = () => {
       var form={}
       if (regex.current.value) {
           form.reg_input = regex.current.value
           if (textinput.current.value) {
               form.text_input = textinput.current.value
               if (granularity.current.value) {
                   form.gran_input = granularity.current.value
               } else {
                   form.gran_input = "text"
               }
               if (precondition.current.value) {
                   form.pre_input = precondition.current.value
               } else {
                  form.pre_input = "undefined"
               }
               if (postcondition.current.value) {
                  form.post_input = postcondition.current.value
               } else {
                  form.post_input = "undefined"
               }
              if (invariant.current.value) {
                 form.inv_input = invariant.current.value
              } else {
                 form.inv_input = "undefined"
              }
              if (language.current.value) {
                form.lang_input = language.current.value
              } else {
                form.lang_input = "english"
              }
              form.high_input = highlight.current.value
              match_results(form)
            }
        }
   }
        
    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <AnalysisSideBar />
            </Col>
            <Col>
              <Container>
              <Form id="modelnew">
                <Row>
                    <Col>
                     <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                      <Form.Label>Model Name</Form.Label>
                      <Form.Control type="text" placeholder="name" ref={name}/>
                     </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                      <Form.Label>Model Description</Form.Label>
                      <Form.Control type="text" placeholder="name" ref={description}/>
                     </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col>
                      <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                        <Form.Label>Feature Names </Form.Label>
                        <Form.Control as="textarea" rows={15} ref={feature_list} />
                      </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col>
                      <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                        <Form.Label>Results matrix (class name in last column) </Form.Label>
                        <Form.Control as="textarea" rows={15} ref={matrix} />
                      </Form.Group>
                    </Col>
                </Row>
                <Row>
                  <Col>
                    <Button variant="primary" onClick={() => perform_add()}>Submit</Button>
                  </Col>
                </Row>
               </Form>     
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default ModelNew;