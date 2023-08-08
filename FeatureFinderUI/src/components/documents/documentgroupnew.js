import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import DocumentSideBar from '../navbar/documentsidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function DocumentGroupNew() {
    const[spinner, setSpinner] = useState(false)
    const add_document_group = useStoreActions(actions => actions.addDocumentGroup)
    const documentlist = useStoreState(state => state.documentlist);
    const documentgrouplist = []
    const name = React.createRef()
    const description = React.createRef()
    const newdocument = React.createRef()

  const show_document_list =  documentlist.map((document, i) =>
    <option>document.name</option>);

  const show_group_document_list =  documentgrouplist.map((document, i) =>
    <option>document.name</option>);

  const perform_add = () => {
       if (newdocument.current.value) {
           documentgrouplist.append(newdocument.current.value)
        }
   }

  const perform_submit = () => {
       if (documentgrouplist.length>0) {
           add_document_group(documentgrouplist)
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
              <Form id="groupnew">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Document Group Name</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={name}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Group Description</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={description}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Form.Label>Existing Documents</Form.Label>
                  <Form.Select aria-label="Default select example" ref={documentlist}>
                      {show_group_document_list}
                </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                  <Row>
                    <Col>
                       <Form.Label>Documents To Be Added</Form.Label>
                       <Row>
                         <Col>
                           <Form.Select aria-label="Default select example" ref={newdocument}>
                             {show_document_list}
                           </Form.Select>
                         </Col>
                         <Col>
                           <Button variant="primary" onClick={() => perform_add()}>Add</Button>
                         </Col>
                       </Row>
                   </Col>
                 </Row>
                </Form.Group>
                <Row>
                  <Col>
                    <Button variant="primary" onClick={() => perform_submit()}>Submit</Button>
                  </Col>
                </Row>
               </Form>     
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default DocumentGroupNew;