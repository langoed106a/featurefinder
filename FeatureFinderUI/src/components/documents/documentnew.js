import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import {useNavigate} from 'react-router-dom';
import TopNavBar from '../navbar/topnavbar';
import DocumentSideBar from '../navbar/documentsidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function DocumentNew() {
    const[spinner, setSpinner] = useState(false)
    const add_document = useStoreActions(actions => actions.add_document)
    const name = React.createRef()
    const type = React.createRef()
    const description = React.createRef()
    const content = React.createRef()
    const navigate=useNavigate()

   const perform_add = () => {
       var form={}
       if (name.current.value) {
           form.name_input = name.current.value
           if (type.current.value) {
               form.type_input = type.current.value
               if (content.current.value) {
                   form.content_input = encodeURIComponent(content.current.value)
               } else {
                   form.content_input = "text"
               }
               if (description.current.value) {
                form.description_input = encodeURIComponent(description.current.value)
            } else {
                form.description_input = "text"
            }
              add_document(form)
            }
            navigate("/");
        }
   }
        
    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <DocumentSideBar />
            </Col>
            <Col>
              <Container>
              <Form onSubmit={perform_add} id="documentnew">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Document Name</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={name}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Document Description</Form.Label>
                  <Form.Control type="text" placeholder="description" ref={description}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Document Type</Form.Label>
                  <Form.Select aria-label="Default select example" ref={type}>
                    <option>text</option>
                    <option>file</option>
                    <option>folder</option>
                </Form.Select>
                </Form.Group>
                <Row>
                    <Col>
                      <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                        <Form.Label>Document Content</Form.Label>
                        <Form.Control as="textarea" rows={10} ref={content} />
                      </Form.Group>
                    </Col>
                </Row>
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
export default DocumentNew;