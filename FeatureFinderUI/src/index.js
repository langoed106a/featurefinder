import React from 'react';
import { StoreProvider, createStore } from 'easy-peasy';
import storeModel from './components/service/featurestore';
import HomePage from './components/homepage/homepage';
import ReactDOM from 'react-dom/client';

const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(<StoreProvider store={storeModel}>
               <HomePage />
           </StoreProvider>)