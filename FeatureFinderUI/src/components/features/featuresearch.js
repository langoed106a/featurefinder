import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureSearch() {
    const[spinner, setSpinner] = useState(false)
    const match_results = useStoreActions(actions => actions.get_match_results)
    const matchreply = useStoreState(state => state.matchreply);
    const regex = React.createRef()
    const granularity = React.createRef()
    const precondition = React.createRef()
    const postcondition = React.createRef()
    const invariant = React.createRef()
    const language = React.createRef()
    const textinput = React.createRef()
    const highlight = React.createRef()

   const perform_search = () => {
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
               <FeatureSideBar />
            </Col>
            <Col>
              <Container>
              <Form id="featurenew">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Feature Name</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={regex}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Existing Feature List</Form.Label>
                  <Form.Select aria-label="Default select example" ref={granularity}>
                    <option>sentence</option>
                    <option>text</option>
                </Form.Select>
                </Form.Group>
                <Row>
                  <Col>
                    <Button variant="primary" onClick={() => perform_search()}>Submit</Button>
                  </Col>
                </Row>
               </Form>     
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default FeatureSearch;