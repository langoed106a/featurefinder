import React, { useState, useEffect } from "react";
import { Button, Col, Container, Form, Row, Tab, Tabs } from 'react-bootstrap';
import { useStoreState, useStoreActions } from 'easy-peasy'
import TopNavBar from '../navbar/topnavbar';
import FeatureSideBar from '../navbar/featuresidebar';
import FeatureSpinner from '../navbar/featurespinner';
import 'bootstrap/dist/css/bootstrap.min.css';

function FeatureExperiment() {
  const [spinner, setSpinner] = useState(false)
  const [form, setForm] = useState({reg_input:"", gran_input:"sentence", text_input:"", lang_input:"english", pre_input:"", post_input:"", inv_input:""})
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

  const perform_search = (event) => {
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
    var allmatches = []
    if (matches) {
        allmatches = matches.length
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
    }
    return check
  }

  const add_highlight = (part) => {
    var tokenindex = 0
    var text = "", item = ""
    var highlighter = false, check = false
    var firstmatch = {}
    var results = matchresults
    var matches = {}
    var tokens = {}
    var token = {}
    var sentencetokens = {}
    var start = 0, tokenindex = 0, tokensize = 0, sentencesize=0, sentenceindex=0, wordposition=0, wordsize=0, wordcount=0
    let spanStartHighlightText = "<span style=\"background-color: violet; color: black;\">"
    let spanStartLowlightText = "<span style=\"background-color: white; color: black;\">"
    let spanEndText = "</span>"
    if ((results) && (results.sentencelist)) {
      matches = results.matches
      tokens = results.sentencelist
      start = -1
      if ((matches) && (matches.length > 0)) {
           firstmatch = matches[0]
           start = parseInt(firstmatch.start)
      }
      if (start == 0) {
        highlighter = true
        text = text + spanStartHighlightText
      } else {
        highlighter = false
        text = text + spanStartLowlightText
      }
      sentencesize = tokens.length
      sentenceindex = 0
      wordposition = 0
      while (sentenceindex < sentencesize) {
          sentencetokens = tokens[sentenceindex]
          sentencetokens = sentencetokens.sentence
          wordsize = sentencetokens.length
          wordcount = 0
          while (wordcount<wordsize) {
              token = sentencetokens[wordcount]
              check = check_highlight(matches, wordposition)
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
            wordcount++
            wordposition++
            }
          sentenceindex++
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

  const highlightPinyin = () => ({
    __html: add_highlight('pinyin')
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
                  <Form.Control as="textarea" rows={10}  defaultValue={form.reg_input} ref={regex} />
                  <Form.Label>Granularity</Form.Label>
                  <Form.Control type="text" placeholder="sentence" defaultValue={form.gran_input} ref={granularity} />
                  <Form.Label>Precondition</Form.Label>
                  <Form.Control type="text" placeholder="<text=contains('comma')>" defaultValue={form.pre_input} ref={precondition} />
                  <Form.Label>Postcondition</Form.Label>
                  <Form.Control type="text" placeholder="$matches=3" defaultValue={form.post_input} ref={postcondition} />
                  <Form.Label>Invariant</Form.Label>
                  <Form.Control type="text" placeholder="<token='word'>" defaultValue={form.inv_input}  ref={invariant} />
                  <Form.Label>Language</Form.Label>
                  <Form.Select aria-label="Default select example" defaultValue={form.lang_input} ref={language}>
                    <option value="1">English</option>
                    <option value="2">Chinese</option>
                    <option value="3">Russian</option>
                    <option value="4">Pinyin</option>
                  </Form.Select>
                  <Row>
                    <Col>
                      <Form.Label>Text Input</Form.Label>
                      <Form.Control as="textarea" rows={10} defaultValue={form.text_input}  ref={textinput} />
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
                        <Tab eventKey="pinyin" title="Pinyin">
                          <div
                            dangerouslySetInnerHTML={highlightPinyin()}
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