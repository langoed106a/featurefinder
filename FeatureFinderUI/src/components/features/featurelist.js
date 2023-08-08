import React, {useState, useEffect} from "react";
import {Accordion, Col, Container, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import {Link} from 'react-router-dom';
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureList() {
    const[spinner, setSpinner] = useState(false)
    const populate_regexes = useStoreActions((actions) => actions.get_regex_feature_list)
    const populate_wordlists = useStoreActions((actions) => actions.get_word_list)
    const populate_functions = useStoreActions((actions) => actions.get_function_feature_list)
    const function_arr = useStoreState(state => state.featurefunctionlist)
    const regex_arr = useStoreState(state => state.featureregexlist)
    const word_arr = useStoreState(state => state.wordlist)

    const show_current_regexes =  regex_arr.map((func, i) =>
          <Row key={i}>
             <Col xs={2}>
               <Link to= {`/featuredetail?id=${func.id}`}>{func.name}</Link>
             </Col>
             <Col xs={6}>
               {func.description}
             </Col>  
             <Col xs={2}>
               {func.type}
             </Col>  
          </Row>);

    const show_functions =  function_arr.map((func, i) =>
          <Row key={i}>
             <Col xs={3}>
               {func.name}
             </Col>
             <Col xs={6}>
               {func.description}
             </Col>  
             <Col xs={2}>
               {func.type}
             </Col>  
          </Row>);

   const show_word_lists =  word_arr.map((func, i) =>
          <Row key={i}>
             <Col xs={2}>
               {func.name}
             </Col>
             <Col xs={6}>
               {func.description}
             </Col>  
             <Col xs={2}>
               {func.type}
             </Col>  
          </Row>);

   const MainContent = () => {
          return ( <div>
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
            <Col className="col-md-10">
         <Accordion defaultActiveKey="0">
            <Accordion.Item eventKey="0">
               <Accordion.Header>
                  Inbuilt feature finder functions
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     {show_functions}
                  </Container>
               </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="1">
               <Accordion.Header>
                  Pre-written feature finder regexes
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     {show_current_regexes}
                  </Container>
               </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="2">
               <Accordion.Header>
                  Existing feature finder word lists
               </Accordion.Header>
               <Accordion.Body>
                  <Container>
                     {show_word_lists}
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

   useEffect(() => {
      populate_regexes();
      populate_wordlists();
      populate_functions();
   }, [populate_regexes, populate_wordlists, populate_functions]);

    return (<div>
             {spinner ? <FeatureSpinner/>: <MainContent />}
            </div>)
}
export default FeatureList;