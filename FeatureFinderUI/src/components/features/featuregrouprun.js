import React, {useState, useEffect} from "react";
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import {Link, useNavigate} from 'react-router-dom';
import { useStoreState, useStoreActions } from 'easy-peasy'
import NavBar from '../navbar/navbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../service/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';

const groupfeature = React.createRef()
const documentgroup = React.createRef()
const runname = React.createRef()
const rundescription = React.createRef()

function FeatureGroupRun() {
    const[spinner, setSpinner] = useState(false)
    const[buttonshow, setButtonShow] = useState(true)
    const[group_added_list, add_group] = useState([])
    const populate_featurelist = useStoreActions((actions) => actions.get_featuregroup_list)
    const[group_tobeadded_list, alter_group] = useState(useStoreState(state => state.featuregrouplist))
    const populate_documentlist = useStoreActions((actions) => actions.get_documentgroup_list)
    const document_group_list = useStoreState(state => state.documentgrouplist)
    const bulk_run = useStoreActions((actions) => actions.start_async_bulk_run)
    const navigate=useNavigate();

    const get_group_tobeadded_list = () => {
        return group_tobeadded_list
    }

    const get_group_added_list = () => {
        return group_added_list
    }
    const get_document_group_list = () => {
      return document_group_list
  }

    const get_group_list = () => {
      var group_list = []
      var item = ""
      var name = ""
      var pos=0
      var document_group = get_document_group_list();
      if (document_group) {
          for (let i=0; i<document_group.length; i++) {
              item = document_group_list[i]
              name = item.name
              pos = name.indexOf(":")
              if (pos>0) {
                name = name.substring(0, pos);
              }
              group_list.push(name)
          }
        }
      return group_list
    }

    const NewGroupAdded = () => {
        var group_added_list = get_group_added_list()
        return(<Form.Select required aria-label="Default select example" readOnly>
                 {group_added_list.map((item,index) =>
                   <option value={index+1} key={index}>{item}</option>
                  )}
        </Form.Select>)
    }

    const NewGroupToBeAdded = () => {
        var group_tobeadded_list = get_group_tobeadded_list()
        return(<Form.Select aria-label="Default select example" as="select" ref={groupfeature} >
           {group_tobeadded_list.map((item,index) =>
              <option value={item.name} key={index}>{item.name}</option>
            )}
        </Form.Select>)
    }

    const DocumentsAvailable = () => {
        var document_list = get_group_list()
        return(<Form.Select aria-label="Default select example" as="select" ref={documentgroup} >
           {document_list.map((item,index) =>
              <option value={item} key={index}>{item}</option>
            )}
        </Form.Select>)
    }

    const addGroup = () => {
        var newgroup = groupfeature.current.value
        add_group(group_added_list => group_added_list.concat(newgroup));
        alter_group(group_tobeadded_list.filter(item => item !== newgroup))
        setButtonShow(false)
     }

    const perform_run = () => {
        var size = group_added_list.length
        var index=0
        var group_list=""
        var group_name=""
        var form={}
        if (size>0) {
            group_list = ""
            while (index<size) {
                group_name = group_added_list[index]
                group_list = group_list+group_name+","
                index++
            }
            group_list = group_list.substring(0, group_list.length-1);
            form.name_input = runname.current.value
            form.lang_input = "english"
            form.description_input = rundescription.current.value
            form.groupname_input = group_list
            form.documentgroupname_input = documentgroup.current.value
            bulk_run(form)
            navigate('/')
        }
    }

    useEffect(() => {
      populate_documentlist();
      populate_featurelist();
    }, [populate_documentlist, populate_featurelist]);

    return (<div>
      <NavBar />
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
                  <Form.Control type="text" placeholder="name" ref={runname} required/>
                  <Form.Label>Run Description</Form.Label>
                  <Form.Control type="text" placeholder="description" ref={rundescription} required/>
                  <Row>
                      <Col>
                         <Form.Label>Feature Group List</Form.Label>
                         <NewGroupAdded />
                      </Col>
                  </Row>
                  <Row>
                      <Col>
                        <Form.Label>All Available Feature Groups</Form.Label>
                      </Col>
                  </Row>
                  <Row>
                    <Col>
                       <NewGroupToBeAdded />
                    </Col>
                    <Col>
                       <Button variant="primary" type="button" onClick={addGroup} >Add</Button>
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                       <Form.Label>Document Group List</Form.Label>
                          <DocumentsAvailable />
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                    </Col>
                  </Row>
                  <Row>
                   <Col xs={1}>
                      <Button disabled={buttonshow} variant="primary" type="submit">Submit</Button>
                   </Col>
                   <Col>
                      <Link to="/featurelist">
                         <Button variant="primary" type="button">Quit</Button>
                      </Link>  
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
export default FeatureGroupRun;