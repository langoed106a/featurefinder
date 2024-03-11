import React, {useState, useEffect} from "react";
import {useNavigate, createSearchParams} from "react-router-dom";
import {Accordion, Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import AnalysisSideBar from '../navbar/analysissidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function AnalyseRun() {
   const[spinner, setSpinner] = useState(false)
   const[viewresults, setViewResults] = useState(false)
   const populate_runlist = useStoreActions((actions) => actions.get_run_list)
   const populate_modellist = useStoreActions((actions) => actions.get_model_list)
   const get_results_list = useStoreActions((actions) => actions.get_results_list)
   const delete_document = useStoreActions((actions) => actions.delete_document)
   const model_arr = useStoreState(state => state.modellist)
   const run_arr = useStoreState(state => state.runlist)
   var navigate = useNavigate()
   const[model, setModel] = useState([...(Array(run_arr.length).fill('none'))])

   useEffect(() => {
      populate_runlist();
      populate_modellist();
   }, [populate_runlist,populate_modellist]);

   const handle_change = (event) => {
      var modelname = event.target.value
      var id = event.target.id
      var name_arr = model
      event.preventDefault();
      if (id) {
         name_arr[id] = modelname
      } else {
         id = 0
         name_arr[id] = modelname
      }
      setModel([...name_arr])
   }

   const AnalyseModel = ({elementid}) => {
      return (<Form.Select aria-label="Default select example" value={model[elementid]} id={elementid} onChange={handle_change}>
              {model_arr.map((item,index) =>
                 <option value={item.name} id={index} key={index}>{item.name}  </option>
              )}
             </Form.Select>)
   }

   const get_data_results = (token, runname, model) => {
      let form = {}
      form.token = token
      form.runname = runname
      form.model = model
      get_results_list(form)
   }

   const handleAnalyse = (e) => {
      e.preventDefault();
      var index = e.target.id
      var modelname=""
      var runname=""
      var token=""
      if ((!index) || (index==="")) {
         index=0
      }
      if ((index>0) && (index<run_arr.length)) {
         modelname = model[index]
         token = run_arr[index].contents
         runname = run_arr[index].name
         get_data_results(token, runname, modelname)
      }
     
    };

   const handleDelete = (e) => {
      console.log("***Delete:")
      e.preventDefault();
      var index = e.target.id
      var id = 0
      if ((!index) || (index==="")) {
         index=0
      }
      id = run_arr[index].id
      delete_document(id)
    };

   const AnalyseRuns = () => {
      return(<div>
              {run_arr.map((run, index) =>
                 <Row id={run.id} key={index}>
                  <Col xs={1}>
                    {run.name}
                  </Col>
                  <Col xs={2}>
                    {run.description}
                  </Col> 
                  <Col xs={2}>
                    {run.contents}
                  </Col> 
                  <Col xs={2}>
                    {run.label}
                  </Col>  
                  <Col xs={2}>
                    <AnalyseModel elementid={index} />
                  </Col>
                  <Col xs={1}>
                     <Button variant="primary" id={index} type="button" onClick={handleAnalyse} >Analyse</Button>
                  </Col>
                  <Col xs={1}>
                     <Button variant="primary" id={index} type="button" onClick={handleDelete} >Delete</Button>
                  </Col>
                 </Row>)}
               </div>)
   }

   const MainContent = () => {
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
                  <Col className="col-md-12">
                     <img src={words_img} width={"100%"} height={"250px"} alt='word puzzle'/>
                  </Col>
               </Row>
               <Row>
            <Col className="col-md-12">
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

  return (<div>
            <MainContent />
          </div>)
}

export default AnalyseRun;