import React, { useState, useEffect } from "react";
import { Button, Col, Container, Form, Row, Tab, Tabs } from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';

function FeatureExperiment() {
  const [spinner, setSpinner] = useState(false)
  const { get_match_results } = useStoreActions((actions) => actions)
  const { matchresults, searching } = useStoreState((state) => state);
  const regex = React.createRef()
  const granularity = React.createRef()
  const precondition = React.createRef()
  const postcondition = React.createRef()
  const invariant = React.createRef()
  const language = React.createRef()
  const textinput = React.createRef()
  const highlight = React.createRef()
  matchresults = {}

  const perform_search = (event) => {
    var form = {}
    var input_text = ""
    event.preventDefault();
    form.reg_input = regex.current.value
    form.gran_input = granularity.current.value
    form.pre_input = precondition.current.value
    form.post_input = postcondition.current.value
    form.inv_input = invariant.current.value
    form.lang_input = language.current.value
    input_text = textinput.current.value
    input_text = input_text.replace(/(\r\n|\n|\r)/gm, "")
    form.text_input = input_text
    form.high_input = highlight.current.value
    get_match_results(form)
  }

  const check_highlight = (matches, tokenindex) => {
    var index = 0
    var finish = false
    var check = false
    var start = 0, end = 0
    var match = {}
    var allmatches = matches.length
    while ((index < allmatches) && (!finish)) {
      match = matches[index]
      end = parseInt(match.end)
      start = parseInt(match.start)
      if ((tokenindex >= start) && (tokenindex < end)) {
        check = true
        finish = true
      } else if (tokenindex < start) {
        finish = true
      }
      index++
    }
    return check
  }

  const add_highlight = (part) => {
    var tokenindex = 0
    var text = "", token = "", item = ""
    var highlighter = false, check = false
    var firstmatch = {}
    var results = matchresults
    var matches = {}
    var tokens = {}
    var start = 0, tokenindex = 0, tokensize = 0
    let spanStartHighlightText = "<span style=\"background-color: violet; color: black;\">"
    let spanStartLowlightText = "<span style=\"background-color: white; color: black;\">"
    let spanEndText = "</span>"
    if ((results) && (results.tokens) && (results.tokens.length > 0)) {
      matches = results.matches
      tokens = results.tokens
      if ((matches) && (matches.length > 0)) {
        firstmatch = matches[0]
        start = parseInt(firstmatch.start)
      } else {
        start = -1
      }
      if (start == 0) {
        highlighter = true
        text = text + spanStartHighlightText
      } else {
        highlighter = false
        text = text + spanStartLowlightText
      }
      tokensize = tokens.length
      while (tokenindex < tokensize) {
        token = tokens[tokenindex]
        check = check_highlight(matches, tokenindex)
        if (part == 'token') {
          item = token.token
        } else {
          item = token.postag
        }
        if (check) {
          if (!highlighter) {
            highlighter = true
            text = text + spanEndText
            text = text + spanStartHighlightText + item + " "
          } else {
            text = text + item + " "
          }
        } else {
          if (highlighter) {
            highlighter = false
            text = text + spanEndText
            text = text + spanStartLowlightText + item + " "
          } else {
            text = text + item + " "
          }
        }
        tokenindex++
      }
      text = text + spanEndText
    }
    return text
  }

  const highlightTokens = () => ({
    __html: add_highlight('token')
  })

  const highlightPostags = () => ({
    __html: add_highlight('postag')
  })

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
              <Form onSubmit={perform_search} id="experiment">
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1" role="form">
                  <Form.Label>Regex</Form.Label>
                  <Form.Control as="textarea" rows={10} ref={regex} />
                  <Form.Label>Granularity</Form.Label>
                  <Form.Control type="text" placeholder="sentence" ref={granularity} />
                  <Form.Label>Precondition</Form.Label>
                  <Form.Control type="text" placeholder="<text=contains('comma')>" ref={precondition} />
                  <Form.Label>Postcondition</Form.Label>
                  <Form.Control type="text" placeholder="$matches=3" ref={postcondition} />
                  <Form.Label>Invariant</Form.Label>
                  <Form.Control type="text" placeholder="<token='word'>" ref={invariant} />
                  <Form.Select aria-label="Default select example" ref={language}>
                    <option>Language</option>
                    <option value="1">English</option>
                    <option value="2">Chinese</option>
                    <option value="3">Russian</option>
                  </Form.Select>
                  <Row>
                    <Col>
                      <Form.Label>Text Input</Form.Label>
                      <Form.Control as="textarea" rows={10} ref={textinput} />
                    </Col>
                    <Col>
                      <Tabs defaultActiveKey="tokens" transition={false} id="tokens" className="mb-3">
                        <Tab eventKey="tokens" title="Tokens">
                          <div
                            dangerouslySetInnerHTML={highlightTokens()}
                          />
                        </Tab>
                        <Tab eventKey="postags" title="Postags">
                          <div
                            dangerouslySetInnerHTML={highlightPostags()}
                          />
                        </Tab>
                      </Tabs>
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                      <Button variant="primary" type="submit">Submit</Button>
                    </Col>
                    <Col>
                      <Form.Check type="checkbox" label="Highlight match in text" ref={highlight} />
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

  return (<div>
    {searching ? <FeatureSpinner /> : <MainContent />}
  </div>)

}
export default FeatureExperiment;