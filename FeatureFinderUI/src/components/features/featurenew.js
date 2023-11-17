import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import {useNavigate} from 'react-router-dom';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureNew() {
    const[spinner, setSpinner] = useState(false)
    const add_document = useStoreActions(actions => actions.add_document)
    const matchreply = useStoreState(state => state.matchreply);
    const name = React.createRef()
    const regex = React.createRef()
    const description = React.createRef()
    const granularity = React.createRef()
    const precondition = React.createRef()
    const postcondition = React.createRef()
    const invariant = React.createRef()
    const navigate=useNavigate()

   const perform_add = (event) => {
       var form={}
       var form_field="", reg="", gran="", pre="", post="", inv=""
       event.preventDefault();
       if ((regex.current.value) && (name.current.value)) {
           form_field = encodeURIComponent(regex.current.value)
           reg="\"regex\":\""+form_field+"\""
           form.type_input = "regex"
           form.name_input = name.current.value
           if (granularity.current.value) {
                   form_field = granularity.current.value
           } else {
                   form_field = "text"
           }
           gran="\"granularity\":\""+form_field+"\""
           if (precondition.current.value) {
                   form_field = encodeURIComponent(precondition.current.value)
           } else {
                   form_field=" "
           }
           pre="\"precondition\":\""+form_field+"\""
           if (postcondition.current.value) {
              form_field = encodeURIComponent(postcondition.current.value)
           } else {
              form_field = " "
           }
           post="\"postcondition\":\""+form_field+"\""
           if (invariant.current.value) {
               form_field = encodeURIComponent(invariant.current.value)
           } else {
               form_field=" "
           }
           inv="\"invariant\":\""+form_field+"\""
           form.content_input = "{"+reg+","+gran+","+pre+","+post+","+inv+"}"
           form.description_input = description.current.value
           add_document(form)
        }
      navigate("/");
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
              <Form onSubmit={perform_add} id="featurenew">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Feature Name</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={name}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Description</Form.Label>
                  <Form.Control type="text" placeholder="description" ref={description}/>
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
                      <Button variant="primary" type="submit">Submit</Button>
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