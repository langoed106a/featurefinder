import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import AnalysisSideBar from '../navbar/analysissidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function ConfusionMatrix() {
    const[modelname, setModelname] = useState("")
    const populate_modellist = useStoreActions((actions) => actions.get_model_list)
    const get_confusion_matrix = useStoreActions((actions) => actions.get_confusion_matrix)
    const get_spinner_matrix = useStoreActions((actions) => actions.get_spinner_matrix)
    const get_spinner = useStoreActions((actions) => actions.get_spinner)
    const { modellist, confusionmatrix, loadingmatrix } = useStoreState(state => state)
    const features = React.createRef()
    const results = React.createRef()
    const matrix = React.createRef()

    const perform_create = (event) => {
       var form={}
       get_confusion_matrix(form)
    }

    const handle_change = (event) => {
        var model = event.target.value
        setModelname(model)
     }

    const get_model_list = () => {
        return modellist
     }

    const ModelList = () => {
        var model_list = get_model_list()
        return (<Form.Select aria-label="Default select example" onChange={handle_change}>
                {model_list.map((item,index) =>
                   <option value={item.name} key={index}>{item.name}  </option>
                )}
               </Form.Select>)
     }

    const MainContent = () => {
          return (
           <div>
           <TopNavBar />
            <Container fluid>
            <Row>
             <Col className="col-md-2">
               <AnalysisSideBar />
             </Col>
             <Col>
             <Container>
              <Form onSubmit={perform_create} id="confusionmatrix">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1" role="form">
                  <Form.Label>Feature Names ( separated by commas )</Form.Label>
                  <Form.Control as="textarea" rows={10} ref={features} />
                  <Form.Label>Results list ( matrix )</Form.Label>
                  <Form.Control as="textarea" rows={10} ref={results} />
                  <ModelList />
                  <Form.Label>Derived Confusion Matrix</Form.Label>
                  <Form.Control as="textarea" rows={10} ref={matrix}/>
                  <Row>
                   <Col>
                    <Button variant="primary" type="submit">Submit</Button>
                   </Col>
                 </Row>
                </Form.Group>
               </Form>    
              </Container>
             </Col>
            </Row>
           </Container>
          </div>)
    }

    useEffect(() => {
        populate_modellist();
    },[populate_modellist]);
   
    return (<div>
             { loadingmatrix ? <FeatureSpinner />: <MainContent /> }
            </div>)
}
export default ConfusionMatrix;