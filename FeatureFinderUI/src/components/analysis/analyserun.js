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
   const current_runs=(run_arr, model_arr)=> {
    var contents = ""
    var featurename = ""
    var documentname = ""
    var run = ""
    var jsonRun = {}
    var run_list = []
    for (let i=0;i<run_arr.length;i++) {
      run = run_arr[i]
      contents = decodeURIComponent(run.contents)
      jsonRun = JSON.parse(contents)
      featurename = jsonRun.featuregroupname
      documentname = jsonRun.documentgroupname
      run.description = featurename+" over "+documentname
      run.modelname=""
      if ((model_arr) && (model_arr.length>0)) {
           run.modelname = model_arr[0].name
      }
      run_list.push(run)
    }
      return run_list
    }
    const[spinner, setSpinner] = useState(false)
    const[modelname, setModelname] = useState("")
    const populate_runlist = useStoreActions((actions) => actions.get_run_list)
    const populate_modellist = useStoreActions((actions) => actions.get_model_list)
    const model_arr = useStoreState(state => state.modellist)
    const run_arr = useStoreState(state => state.runlist)
    var run_list = current_runs(run_arr, model_arr)
    var navigate = useNavigate()

   const get_model_list = () => {
       return model_arr
    }

   const get_run_list = () => {
      return run_list
   }

   const handle_change = (event) => {
      var model = event.target.value
      setModelname(model)
   }

   const AnalyseModel = ({elementid}) => {
      var model_list = get_model_list()
      return (<Form.Select aria-label="Default select example" id={elementid} onChange={handle_change}>
              {model_list.map((item,index) =>
                 <option value={item.name} key={index}>{item.name}  </option>
              )}
             </Form.Select>)
   }

   const handleAnalyse = (e) => {
      var run_list = get_run_list()
      e.preventDefault();
      var chosen_model=""
      var run = ""
      var runname = ""
      var run_id = e.target.id
      if ((run_id) && (run_id>0)) {
         run = run_list[run_id-1]
         runname = run.name
      } else {
         runname = run_list[0].name
      }
      if (modelname) {
         chosen_model = modelname
      } else {
         chosen_model = model_arr[0].name
      }
      navigate({
         pathname: '/modelresults',
         search: `?id=${runname}&model=${chosen_model}`,
       });
    };

   const AnalyseRuns = () => {
      var run_list = get_run_list()
      return(<div>
              {run_list.map((run,index) =>
                 <Row id={index+1} key={index}>
                  <Col xs={2}>
                    {run.name}
                  </Col>
                  <Col xs={4}>
                    {run.description}
                  </Col>  
                  <Col xs={2}>
                    <AnalyseModel elementid={index+1} />
                  </Col>
                  <Col xs={2}>
                     <Button variant="primary" id={index+1} type="button" onClick={handleAnalyse} >Analyse</Button>
                  </Col>
                 </Row>)}
               </div>)
    }

   useEffect(() => {
      populate_runlist();
      populate_modellist();
   }, [populate_runlist,populate_modellist]);

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