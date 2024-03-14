import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import {Link, useNavigate} from 'react-router-dom';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';


const runname = React.createRef()
const rundescription = React.createRef()
const featuregroup = React.createRef()
const documentgroup = React.createRef()

function FeatureGroupRun() {
    const bulk_run = useStoreActions((actions) => actions.start_async_bulk_run)
    const result_document_list = useStoreActions((actions) => actions.get_result_document_list)
    const document_list = useStoreState(state => state.documentresultlist)
    const { matchresults, searching } = useStoreState((state) => state);
    const navigate=useNavigate();
    const runname = React.createRef()
    const featuregroup = React.createRef()
    const documentgroup = React.createRef()
    const description = React.createRef()
    var resultid="0"

    useEffect(() => {
      result_document_list();
   }, [result_document_list]);


    const perform_run = (event) => {
      var form={}
      var form_field=""
      var index = parseInt(resultid)
      event.preventDefault();
      if ((runname.current.value) && (featuregroup.current.value) && (documentgroup.current.value)) {
          form_field = runname.current.value
          form.name_input = form_field
          if (description.current.value) {
            form.description_input = description.current.value
          } else {
            form.description_input =""
          }
          form.lang_input="english"
          form.outputlocation_input = document_list[index].name
          form.featuregroupname_input = featuregroup.current.value
          form.documentgroupname_input = documentgroup.current.value
          bulk_run(form)
          navigate("/");
       }
  }

  const handleChange = (event) => {
    resultid = event.target.value
 }

  const show_current_result_list =  document_list.map((document, i) =>
           <option key={i} value={i}>{document.name}</option>);


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
              <Form onSubmit={perform_run} id="grouprun">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1" role="form">
                  <Form.Label>Run Name</Form.Label>
                  <Form.Control type="text" placeholder="name" ref={runname} maxlength="15" required/>
                  <Form.Label>Run Description</Form.Label>
                  <Form.Control type="text" placeholder="description" ref={description} maxlength="15" required/>
                  <Form.Label>Feature Groups ( separate with a comma )</Form.Label>
                  <Form.Control type="text" placeholder="feature1,feature2" ref={featuregroup} required/>
                  <Form.Label>Document Groups ( separate with a comma )</Form.Label>
                  <Form.Control type="text" placeholder="group1,group2" ref={documentgroup} required/>
                  <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                     <Form.Label>Choose output location</Form.Label>
                     <Form.Select aria-label="Default select example" onChange={handleChange}>
                       {show_current_result_list}
                     </Form.Select>
                  </Form.Group>
                </Form.Group>
                   <Col>
                      <Button variant="primary" type="submit">Run</Button>
                    </Col>
               </Form>    
              </Container>
            </Col>
         </Row>
     </Container>
   </div>)
  }

   return (<div>
            {searching ? <FeatureSpinner/>: <MainContent />}
          </div>)

}
export default FeatureGroupRun;