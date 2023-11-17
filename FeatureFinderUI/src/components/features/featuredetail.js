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
    const[contents, setContents] = useState({})
    const {searching} = useStoreState((state) => state);
    const document = useStoreState(state => state.document)
    const {get_regex_document} = useStoreActions((actions) => actions)
    const [name, setName] = useState('')
    const [type, setType] = useState('')
    const [description, setDescription] = useState('')
   
    const getdocumentbyid = () => {
       let id = searchParams.get("id")
       get_regex_document(id)
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
                  <Form.Control type="text" placeholder="name" defaultValue={document.name} readOnly />
                  <Form.Label>Description</Form.Label>
                  <Form.Control type="text" placeholder="description" defaultValue={document.description} readOnly/>
                  <Form.Label>Type</Form.Label>
                  <Form.Control type="text" placeholder="type" defaultValue={document.type} readOnly />
                </Form.Group>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1" role="form">
                  <Form.Label>Regex</Form.Label>
                  <Form.Control as="textarea" rows={10}  value={decodeURIComponent(document.regex)} readOnly/>
                  <Form.Label>Granularity</Form.Label>
                  <Form.Control type="text" placeholder="" defaultValue={document.granularity} readOnly />
                  <Form.Label>Precondition</Form.Label>
                  <Form.Control type="text" placeholder="" defaultValue={decodeURIComponent(document.precondition)} readOnly/>
                  <Form.Label>Postcondition</Form.Label>
                  <Form.Control type="text" placeholder="" defaultValue={decodeURIComponent(document.postcondition)} readOnly />
                  <Form.Label>Invariant</Form.Label>
                  <Form.Control type="text" placeholder="" defaultValue={decodeURIComponent(document.invariant)}  readOnly />
                  <Form.Label>Language</Form.Label>
                  <Form.Control type="text" placeholder="" defaultValue="english" readOnly />
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