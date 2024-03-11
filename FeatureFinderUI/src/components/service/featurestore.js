import axios from 'axios'
import cors from 'cors'
import {useState} from 'react';
import { action, createStore, persist, thunk } from "easy-peasy";
import regeneratorRuntime from "regenerator-runtime";

const service_url=""
const delay = ms => new Promise(res => setTimeout(res, ms));
const featureheaders = {
  'Content-Type': 'application/json'
}
const storeModel = createStore (
  persist({
           featurefunctionlist: [],
           featureregexlist: [],
           wordlist: [],
           modellist: [],
           documentlist: [],
           documentresultlist: [],
           document: {},
           featuregrouplist: [],
           documentgrouplist: [],
           runlist: [],
           matchresults:{},
           bulkresults:{},
           runresults: [],
           settinglist: [],
           confusionmatrix:"",
           loadingmatrix: false,
           searching:false,
           addFeature: action((state, feature) => {
               state.featurefunctionlist.push(feature)
           }),
           get_regex_feature_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=regex')
              actions.set_regex_feature_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_regex_feature_list: action((state, regexlist) => {
             state.featureregexlist = regexlist
           }),
           get_function_feature_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=function');
              actions.set_function_feature_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_function_feature_list: action((state, featurelist) => {
               state.featurefunctionlist = featurelist
           }),
           get_model_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=model');
              actions.set_model_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_model_list: action((state, modellist) => {
               state.modellist = modellist
           }),
           get_run_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=run');
              actions.set_run_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_run_list: action((state, runlist) => {
               state.runlist = runlist
           }),
           adjust_run_list: action((state, index) => {
            let temp_arr= state.runlist.filter(item => item.id !== index )
            state.runlist = temp_arr
           }),
           get_results_list: thunk(async (actions, form) => {
            try {
              fetch(service_url+'/modelresults?token='+form.token+"&runname="+form.runname+"&model="+form.model).
                 then(response => {const filename =  response.headers.get('Content-Disposition').split('filename=')[1];
                       response.blob().then(blob => {
                       let url = window.URL.createObjectURL(blob);
                       let a = document.createElement('a');
                       a.href = url;
                       a.download = filename;
                       a.click()
                       })
                      });
            } catch (error) {
              console.log(error);
            }
           }),
           set_results_list: action((state, runresults) => {
               state.runresults = runresults
           }),
           get_word_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=list');
              actions.set_word_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_word_list: action((state, wordlist) => {
               state.wordlist = wordlist
            }),
           addWord: action((state, word) => {
            state.wordlist.push(word)
           }),
           get_document_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=text,file,folder,model');
              actions.set_document_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           get_result_document_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=file,folder');
              actions.set_result_document_list(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_result_document_list: action((state, documentlist) => {
            state.documentresultlist = ['none',...documentlist]
           }),
           get_document: thunk(async (actions,id) => {
            try {
              const res = await axios.get(service_url+'/getdocument?documentid='+id);
              actions.set_document(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           delete_document: thunk(async (actions,id) => {
            try {
              const res = await axios.get(service_url+'/deletedocument?documentid='+id);
              actions.adjust_run_list(id)
            } catch (error) {
              console.log(error);
            }
           }),
           get_regex_document: thunk(async (actions,id) => {
            try {
              const res = await axios.get(service_url+'/getregexdocument?documentid='+id);
              actions.set_document(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           get_list_document: thunk(async (actions,id) => {
            try {
              const res = await axios.get(service_url+'/getlistdocument?documentid='+id);
              actions.set_document(res?.data);
            } catch (error) {
              console.log(error);
            }
           }),
           set_document_list: action((state, documentlist) => {
               state.documentlist = documentlist
            }),
           set_document: action((state, document) => {
              state.document = document
           }),
           get_featuregroup_list: thunk(async (actions) => {
             try {
               const res = await axios.get(service_url+'/getdocuments?type=featuregroup');
               actions.set_featuregroup_list(res?.data);
             } catch (error) {
              console.log(error);
            }
           }),
           set_featuregroup_list: action((state, featuregrouplist) => {
             state.featuregrouplist = featuregrouplist
           }),
           addFeatureGroup: action((state, featuregroup) => {
             state.featuregrouplist.push(featuregroup)
           }),
           get_documentgroup_list: thunk(async (actions) => {
            try {
              const res = await axios.get(service_url+'/getdocuments?type=documentgroup');
              actions.set_documentgroup_list(res?.data);
            } catch (error) {
             console.log(error);
           }
          }),
          set_documentgroup_list: action((state, documentgrouplist) => {
            state.documentgrouplist = documentgrouplist
          }),
          addDocumentGroup: action((state, documentgroup) => {
            state.documentgrouplist.push(documentgroup)
          }),
          add_document: thunk(async (actions, form) => {
            try {
                const res = await axios.post(service_url+'/adddocument',{'id':'','name':form.name_input,'type':form.type_input,'label':'','origin':'','contents':form.content_input,'description':form.description_input},{headers:featureheaders});
            } catch (error) {
             console.log(error);
           }
          }),
          get_match_results: thunk(async (actions, form) => {
            actions.set_searching();
            try {
              const res = await axios.post(service_url+'/regexmatches',{'granularity':form.gran_input,'regex':form.reg_input,'language':form.lang_input,'precondition':form.pre_input,'postcondition':form.post_input,'text':form.text_input,'highlight':form.high_input});
              actions.set_match_results(res?.data);
            } catch (error) {
             console.log(error);
           }
            actions.set_searching();
          }),
          set_searching: action((state) => {
            state.searching = !state.searching
          }),
          set_match_results: action((state, matchresults) => {
            state.matchresults = matchresults
          }),
          get_confusion_matrix: thunk(async (actions, form) => {
            actions.set_loading_matrix();
            try {
                  
                   const res = await axios.post(service_url+'/confusionmatrix',form.data_input); 
                   actions.set_confusion_matrix(res?.data);
            } catch (error) {
              actions.set_confusion_matrix("Error in retrieving confusion matrix");
           }
           actions.set_loading_matrix();
          }),
          set_confusion_matrix: action((state, confusionmatrix) => {
            state.confusionmatrix = confusionmatrix
          }),
          set_loading_matrix: action((state) => {
            state.loadingmatrix = !state.loadingmatrix
          }),
          create_model: thunk(async (actions, form) => {
            try {
              const res = await axios.post(service_url+'/buildclassifierfrommodel',form.data_input);
            } catch (error) {
             console.log(error);
           }
          }),
          start_async_bulk_run: thunk(async (actions, form) => {
            try {
              actions.set_searching();
              const res = await axios.get(service_url+'/runasyncgroupagainstdocument?runname='+form.name_input+'&description='+form.description_input+'&language='+form.lang_input+'&featuregroupname='+form.featuregroupname_input+'&documentgroupname='+form.documentgroupname_input+'&outputlocation='+form.outputlocation_input);
              actions.set_bulk_results(res?.data);
              actions.set_searching();
            } catch (error) {
             console.log(error);
           }
          }),
          set_bulk_results: action((state, bulkresults) => {
            state.bulkresults = bulkresults
          }),
  })
)

export default storeModel;