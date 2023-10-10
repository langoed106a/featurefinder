import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row, Tab, Tabs} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import {useSearchParams} from 'react-router-dom';
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';

function FeatureDetail() {
    const [searchParams, setSearchParams] = useSearchParams()
    const[spinner, setSpinner] = useState(false)
    const {searching} = useStoreState((state) => state);
    const document = useStoreState(state => state.document)
    const {get_document} = useStoreActions((actions) => actions)
    const [name, setName] = useState('')
    const [type, setType] = useState('')
    const [description, setDescription] = useState('')
    const [contents, setContents] = useState('')
     
    const getdocumentbyid = () => {
       let id = searchParams.get("id")
       get_document(id)
    }

    useEffect(
      getdocumentbyid,
      [] 
    )

    const MainContent = () => {  
    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <FeatureSideBar />
            </Col>
            <Col>
              <Container>
              <Form id="detail">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1" role="form">
                  <Form.Label>Name</Form.Label>
                  <Form.Control type="text" placeholder="name" defaultValue={document.name} />
                  <Form.Label>Description</Form.Label>
                  <Form.Control type="text" placeholder="description" defaultValue={document.description}/>
                  <Form.Label>Type</Form.Label>
                  <Form.Control type="text" placeholder="type" defaultValue={document.type} />
                  <Form.Label>Contents</Form.Label>
                  <Form.Control as="textarea" rows={10} defaultValue={document.contents} /> 
                </Form.Group>
               </Form>    
              </Container>
            </Col>
         </Row>
     </Container>
    </div>)
  }
  return (<div>
             { searching ? <FeatureSpinner />: <MainContent /> }
            </div>)

}
export default FeatureDetail;