import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {Accordion, Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import AnalysisSideBar from '../navbar/analysissidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';


function AnalyseRun() {

   const[spinner, setSpinner] = useState(false)
   const[model, setModel] = useState({'name':'none','id':0})
   const populate_runlist = useStoreActions((actions) => actions.get_run_list)
   const populate_modellist = useStoreActions((actions) => actions.get_model_list)
   const model_arr = useStoreState(state => state.modellist)
   const run_arr = useStoreState(state => state.runlist)
   var navigate = useNavigate()

   useEffect(() => {
      populate_runlist();
      populate_modellist();
   }, [populate_runlist,populate_modellist]);


   const handle_change = (event) => {
      var modelname = event.target.value
      var id = event.target.id
      
      setModel({'name':modelname,'id':id})
   }

   const AnalyseModel = ({elementid}) => {
      return (<Form.Select aria-label="Default select example" value={model.name} id={elementid} onChange={handle_change}>
              {model_arr.map((item,index) =>
                 <option value={item.name} key={index}>{item.name}  </option>
              )}
             </Form.Select>)
   }

   const handleAnalyse = (e) => {
      e.preventDefault();
      navigate({
         pathname: '/modelresults',
         search: `?id=${model.id}&model=${model.name}`,
       });
    };

   const AnalyseRuns = () => {
      return(<div>
              {run_arr.map((run) =>
                 <Row id={run.contents} key={run.contents}>
                  <Col xs={2}>
                    {run.name}
                  </Col>
                  <Col xs={4}>
                    {run.description}
                  </Col>  
                  <Col xs={2}>
                    {run.label}
                  </Col>  
                  <Col xs={2}>
                    <AnalyseModel elementid={run.contents} />
                  </Col>
                  <Col xs={2}>
                     <Button variant="primary" id={run.contents} type="button" onClick={handleAnalyse} >Analyse</Button>
                  </Col>
                 </Row>)}
               </div>)
    }

 return(<div>
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
               <Row>
            <Col className="col-md-10">
         <Accordion defaultActiveKey="0">
            <Accordion.Item eventKey="0">
               <Accordion.Header>
                  Existing Runs
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     <AnalyseRuns />
                  </Container>
               </Accordion.Body>
            </Accordion.Item>
         </Accordion>
       </Col>
       </Row>
       </Container>
       </Col>
      </Row>
   </Container>
 </div>)
}
export default AnalyseRun;