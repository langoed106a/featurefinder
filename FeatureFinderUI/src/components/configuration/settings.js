import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import DocumentSideBar from '../navbar/documentsidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function Settings() {
    const[spinner, setSpinner] = useState(false)
    const add_document = useStoreActions(actions => actions.add_document)
    const setting_list = useStoreState(state => state.settinglist)
    const name = React.createRef()
    const value = React.createRef()

    const perform_add = () => {
        var form={}
        if (name.current.value) {
            form.name_input = name.current.value
            if (value.current.value) {
                form.value_input = value.current.value
             }
         }
    }

    const show_setting_list =  setting_list.map((setting, i) =>
      <Row key={i}>
       <Col>
          {setting.name}
       </Col> 
       <Col>
          {setting.value}
       </Col>  
       <Col>
          <Button variant="primary" onClick={() => perform_edit()}>Edit</Button>
       </Col> 
      </Row>);
      
    return (<div>
      <TopNavBar />
      <Container fluid>
         <Row>
            <Col className="col-md-2">
               <DocumentSideBar />
            </Col>
            <Col>
              <Container>
                 <Row>
                  <Col className="col-md-10">
                     <img src={words_img} width={"100%"} height={"250px"} alt='word puzzle'/>
                  </Col>
                 </Row>
                 {show_setting_list}
              </Container> 
            </Col>
         </Row>
     </Container>
 </div>)
}
export default Settings;