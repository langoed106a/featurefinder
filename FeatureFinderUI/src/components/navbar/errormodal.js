import React, {useState, useEffect} from "react";
import {Modal} from 'react-bootstrap';

function ErrorModal ({ handleClose }) {

  return ( 
    <Modal.Dialog onHide={handleClose}>
      <Modal className='modal fade' id='myModal'>
        <Modal.Header className='modal-header'>
          <h5 className='modal-title'>Problem with Regex Server</h5>
          <button type="button" className="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <Modal.Body className='modal-body'>
          <p>A problem arose whilst trying to call the regex server. </p>
        </Modal.Body>
        <Modal.Footer className='modal-footer'>
          <button className='btn btn-primary' data-dismiss='modal' onClick={handleClose}>Close</button>
        </Modal.Footer>
      </Modal>
     </Modal.Dialog>)
}
export default ErrorModal