import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import {useNavigate} from 'react-router-dom';
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';
import words_img from '../../images/featurefinder.jpg';

function FeatureGroupNew() {
    const [spinner, setSpinner] = useState(false)
    const [group, setGroup] = useState({name:"", description:""})
    const [featuredescription, setFeaturedescription] = useState("")
    const [current_feature_arr, setCurrentArr] = useState([])
    const { matchresults, searching } = useStoreState((state) => state);
    const match_results = useStoreActions(actions => actions.get_match_results)
    const add_document = useStoreActions(actions => actions.add_document)
    const feature_regex_arr = useStoreState(state => state.featureregexlist) 
    const matchreply = useStoreState(state => state.matchreply);
    const name = React.createRef()
    const description = React.createRef()
  
    var newfeature = ""
    var feature_arr = feature_regex_arr 
    const navigate=useNavigate()

    const show_feature_list =  feature_arr.map((feature, i) =>
           <option key={i} value={feature.name}>{feature.name}</option>);

    const show_current_feature_list =  current_feature_arr.map((name, i) =>
           <option key={i} value={name}>{name}</option>);

    const perform_add = () => {
      var feature_to_add
      var found=false
      var index=0, arr_index=0
      var item={}
      if ((newfeature.length==0) && (feature_arr.length>0)) {
          newfeature = feature_arr[0].name
      }
      if ((newfeature) && (feature_arr.length>0)) {
         setGroup({name:name.current.value, description: description.current.value})
         feature_to_add = newfeature
         setCurrentArr(current_feature_arr => [...current_feature_arr, feature_to_add] );
         while ((!found) && (index<feature_arr.length)) {
            item = feature_arr[index]
            if (feature_to_add==item.name) {
                arr_index = index
               found = true
            }
            index++
         }
        if (arr_index>-1) {
           feature_arr = feature_arr.splice(arr_index, 1)
        }
      }
     newfeature=""
   }

   const handleChange = (event) => {
      newfeature = event.target.value
   }

   const perform_submit = (event) => {
      var form={}
      console.log("Length of array:"+current_feature_arr.length)
      console.log("Name:"+group.name)
      event.preventDefault();
      if ((current_feature_arr.length>0) && (group.name.length>0)) {
         form.name_input = group.name
         form.type_input = "featuregroup"
         form.content_input = encodeURIComponent(current_feature_arr)
         form.description_input = group.description
         console.log("Submitting")
         add_document(form)
         navigate("/");
       } 
   }
  
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
                <Row>
                  <Col className="col-md-10">
                     <img src={words_img} width={"100%"} height={"250px"} alt='word puzzle'/>
                  </Col>
                </Row>
                  <Form onSubmit={perform_submit} id="featuregroupnew">
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                      <Form.Label>Feature Group Name</Form.Label>
                    <Form.Control type="text" defaultValue={group.name} placeholder="name" ref={name}/>
                   </Form.Group>
                   <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                     <Form.Label>Group Description</Form.Label>
                     <Form.Control type="text" defaultValue={group.description} placeholder="description" ref={description}/>
                   </Form.Group>
                   <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                     <Form.Label>Existing Features</Form.Label>
                     <Form.Select aria-label="Default select example">
                       {show_current_feature_list}
                     </Form.Select>
                  </Form.Group>
                   <Row>
                      <Col>
                         <Row>
                           <p>Features To Be Added</p>
                         </Row>
                         <Row>
                           <Col>
                             <Form.Select aria-label="Default select example" onChange={handleChange}>
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
                        <Button variant="primary" type="submit">Submit</Button>
                      </Col>
                   </Row>   
               </Form>
              </Container> 
            </Col>
         </Row>
     </Container>
   </div>)
  }
 
   return (<div>
            {searching ? <FeatureSpinner /> : <MainContent />}
          </div>)

}
export default FeatureGroupNew;