import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import AnalysisSideBar from '../navbar/analysissidebar';
import FeatureSpinner from '../service/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureAnalysis() {
    const[spinner, setSpinner] = useState(false)
    const match_results = useStoreActions(actions => actions.get_match_results)
    const result_list = useStoreState(state => state.resultlist);
    const model_list = useStoreState(state => state.modellist);
    const runname = React.createRef()
    const modelname = React.createRef()
    
    const show_model_list =  model_list.map((model, i) =>
        <option>{model.name}</option>);

    const show_result_list =  result_list.map((result, i) =>
        <Row>
          <Col xs={4}>
              {result.name}
          </Col> 
          <Col xs={4}>
             <Form.Select aria-label="Default select example" ref={modelname}>
                     {show_model_list}
             </Form.Select>
          </Col> 
          <Col xs={4}>
             <Button variant="primary" onClick={() => perform_analysis()}>Analyse</Button>
          </Col> 
        </Row>);

    const perform_analysis = () => {
       var form={}
       if (modelname.current.value) {
           form.modelnamr_input = regex.current.value
           match_results(form)
        }
   }
        
    return (<div>
      <FeatureSpinner showspinner={spinner} />
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <AnalysisSideBar />
            </Col>
            <Col>
              <Container>
                <Row>
                  <Col className="col-md-10">
                     <img src={words_img} width={"100%"} height={"250px"} alt='word puzzle'/>
                  </Col>
                </Row>
                 {show_result_list}  
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default FeatureAnalysis;