import React from 'react';
import {Route, Routes, Switch, BrowserRouter} from 'react-router-dom';
import AnalyseRun from '../analysis/analyserun';
import FeatureList from '../features/featurelist';
import FeatureExperiment from '../features/featureexperiment';
import FeatureNew from '../features/featurenew';
import FeatureGroup from '../features/featuregroup';
import FeatureGroupNew from '../features/featuregroupnew';
import FeatureSearch from '../features/featuresearch';
import FeatureSpinner from '../navbar/featurespinner';
import DocumentList from '../documents/documentlist';
import DocumentGroup from '../documents/documentgroup';
import Settings from '../configuration/settings';
import DocumentNew from '../documents/documentnew';
import DocumentGroupNew from '../documents/documentgroupnew';
import ModelNew from '../analysis/modelnew';
import ConfusionMatrix from '../analysis/confusionmatrix';

function HomePage() {
   
        return (
            <div>
                <div>
                  <BrowserRouter>
                    <Routes>
                       <Route path="/" element={<FeatureList />} exact />
                         <Route path="/featureanalysis" element={<AnalyseRun />} exact />
                         <Route path="/featurenew" element={<FeatureNew />} exact />
                         <Route path="/featurelist" element={<FeatureList />} exact />
                         <Route path="/featuresearch" element={<FeatureSearch />} exact />
                         <Route path="/featuregroup" element={<FeatureGroup />} exact />
                         <Route path="/featuregroupnew" element={<FeatureGroupNew />} exact />
                         <Route path="/featureexperiment" element={<FeatureExperiment />} exact />
                         <Route path="/documentlist" element={<DocumentList />} exact />
                         <Route path="/documentnew" element={<DocumentNew />} exact />
                         <Route path="/documentgroup" element={<DocumentGroup />} exact />
                         <Route path="/documentgroupnew" element={<DocumentGroupNew />} exact />
                         <Route path="/modelnew" element={<ModelNew />} exact />
                         <Route path="/confusionmatrix" element={<ConfusionMatrix />} exact />
                         <Route path="/settings" element={<Settings />} exact />
                         <Route path="/spinner" element={<FeatureSpinner />} exact />
                    </Routes>
                   </BrowserRouter> 
                </div>
            </div>
        );
    }

export default HomePage; 