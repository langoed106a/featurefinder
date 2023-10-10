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

   const perform_add = () => {
       var form={}
       var form_field=""
       var content = {}
       if (regex.current.value) {
           form_field = encodeURIComponent(regex.current.value)
           content["regex"] = form_field
           form.name_input = name.current.value
           form.type_input = "regex"
           if (granularity.current.value) {
                   form_field = granularity.current.value
           } else {
                   form_field = "text"
           }
           content["granularity"] = form_field
           if (precondition.current.value) {
                   form_field = encodeURIComponent(precondition.current.value)
           } else {
                  form_field = "undefined"
           }
           content["precondition"] = form_field
           if (postcondition.current.value) {
              form_field = encodeURIComponent(postcondition.current.value)
           } else {
              form_field = "undefined"
           }
           content["postcondition"] = form_field
           if (invariant.current.value) {
               form_field = encodeURIComponent(invariant.current.value)
           } else {
                form_field = "undefined"
           }
           content["invariant"] = form_field
           form.content_input = JSON.stringify(content)
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