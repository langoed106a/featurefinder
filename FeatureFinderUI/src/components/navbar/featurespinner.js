import React from 'react';
import spinner from '../../images/spinner.gif';
import './featurespinner.css'; 

function FeatureSpinner() {
  return (
    <div>
      <div>
         <img
           src={spinner}
           style={{ width: '100px', margin: 'auto', display: 'block' }}
           alt="Loading..."
         />
      </div>
      <div>
        <span className="text"> Please wait ...</span>
      </div>
    </div>
  );
};

export default FeatureSpinner;