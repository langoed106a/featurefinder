import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureNew() {
    const[spinner, setSpinner] = useState(false)
    const add_document = useStoreActions(actions => actions.addDocument)
    const matchreply = useStoreState(state => state.matchreply);
    const regex = React.createRef()
    const granularity = React.createRef()
    const precondition = React.createRef()
    const postcondition = React.createRef()
    const invariant = React.createRef()
    const language = React.createRef()
    const textinput = React.createRef()
    const highlight = React.createRef()

   const perform_add = () => {
       var form={}
       if (regex.current.value) {
           form.reg_input = encodeURIComponent(regex.current.value)
           if (textinput.current.value) {
               form.text_input = textinput.current.value
               if (granularity.current.value) {
                   form.gran_input = granularity.current.value
               } else {
                   form.gran_input = "text"
               }
               if (precondition.current.value) {
                   form.pre_input = encodeURIComponent(precondition.current.value)
               } else {
                  form.pre_input = "undefined"
               }
               if (postcondition.current.value) {
                  form.post_input = encodeURIComponent(postcondition.current.value)
               } else {
                  form.post_input = "undefined"
               }
              if (invariant.current.value) {
                 form.inv_input = encodeURIComponent(invariant.current.value)
              } else {
                 form.inv_input = "undefined"
              }
              if (language.current.value) {
                form.lang_input = language.current.value
              } else {
                form.lang_input = "english"
              }
              form.high_input = highlight.current.value
              add_document(form)
            }
        }
   }
        
    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <FeatureSideBar />
            </Col>
            <Col>
              <Container>
              <Form id="featurenew">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Feature Name</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={regex}/>
                </Form.Group>
                <Row>
                    <Col>
                      <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                        <Form.Label>Feature Regex</Form.Label>
                        <Form.Control as="textarea" rows={10} ref={regex} />
                      </Form.Group>
                    </Col>
                </Row>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Granularity</Form.Label>
                  <Form.Select aria-label="Default select example" ref={granularity}>
                    <option>sentence</option>
                    <option>text</option>
                </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Precondition</Form.Label>
                  <Form.Control type="text" placeholder="<text=contains('comma')>" ref={precondition} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Postcondition</Form.Label>
                  <Form.Control type="text" placeholder="$matches=3" ref={postcondition}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Invariant</Form.Label>
                  <Form.Control type="text" placeholder="<token='word'>" ref={invariant} />
                </Form.Group>
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
export default FeatureNew;