import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureGroupNew() {
    const[spinner, setSpinner] = useState(false)
    const match_results = useStoreActions(actions => actions.get_match_results)
    const feature_arr = useStoreState(state => state.featureregexlist)
    const matchreply = useStoreState(state => state.matchreply);
    const name = React.createRef()
    const description = React.createRef()
    const feature = React.createRef()
    const features = React.createRef()

    const show_feature_list =  feature_arr.map((feature, i) =>
           <option>feature.name</option>);

   const perform_add = () => {
       var form={}
       if (feature.current.value) {
          features.append(feature)
        }
   }

   const perform_submit = () => {
      var form={}
      if (name.current.value) {
         form.name_input = name.current.value
       }
  }
        
    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <FeatureSideBar />
            </Col>
            <Col>
              <Container>
                <Row>
                  <Col className="col-md-10">
                     <img src={words_img} width={"100%"} height={"250px"} alt='word puzzle'/>
                  </Col>
                </Row>
                <Row>
                  <Form id="featuregroupnew">
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                      <Form.Label>Feature Group Name</Form.Label>
                    <Form.Control type="text" placeholder="name" ref={name}/>
                   </Form.Group>
                   <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                     <Form.Label>Group Description</Form.Label>
                     <Form.Control type="text" placeholder="name" ref={description}/>
                   </Form.Group>
                   <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                     <Form.Label>Existing Features</Form.Label>
                     <Form.Select aria-label="Default select example" ref={features}>
                       {show_feature_list}
                     </Form.Select>
                  </Form.Group>
                 </Form>
                </Row>
                <Row>
                  <Col>
                     <Row>
                        <p>Features To Be Added</p>
                     </Row>
                     <Row>
                       <Col>
                         <Form.Select aria-label="Default select example" ref={feature}>
                            {show_feature_list}
                         </Form.Select>
                       </Col>
                       <Col>
                        <Button variant="primary" onClick={() => perform_add()}>Add</Button>
                       </Col>
                    </Row>
                </Col>
                </Row>
                <Row>
                  <Col>
                    <Button variant="primary" onClick={() => perform_submit()}>Submit</Button>
                  </Col>
                </Row>   
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default FeatureGroupNew;