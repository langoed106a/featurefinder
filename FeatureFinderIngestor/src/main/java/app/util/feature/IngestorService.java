package app.util.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import java.net.URLDecoder;

import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import app.util.feature.Document;
import app.util.feature.RegexDocument;
import app.util.feature.RegexDocumentList;

import app.util.database.DocumentDatabase;

@CrossOrigin
@RestController
public class IngestorService {
   private static String PROPERTIES_NAME="server.properties";
   private ServiceLocator serviceLocator;
   private FileProcessor fileProcessor;

   @Autowired
   private DocumentDatabase documentDatabase;

   @Autowired
   private RemoteDatabase remoteDatabase;

   @Autowired
   private RemoteProcessor remoteProcessor;

   @Autowired
	private RestTemplate restTemplate;

   public IngestorService() {
      String properties_location = System.getProperty(PROPERTIES_NAME);
	   serviceLocator = new ServiceLocator(properties_location);
   }

   @PostConstruct
   public void initialise() {
      documentDatabase.setRemoteDatabase(remoteDatabase);
      fileProcessor = new FileProcessor(serviceLocator);
      remoteDatabase.setServiceLocator(serviceLocator);
      remoteDatabase.setRestTemplate(restTemplate);
      remoteProcessor.setServiceLocator(serviceLocator);
      remoteProcessor.setRestTemplate(restTemplate);
   }

   @RequestMapping(value = "/processdocuments", method = RequestMethod.GET)
   public void doAsyncProcessDocuments(@RequestParam List<String> documentgrouplist, @RequestParam List<String> featuregrouplist) {
      Document document = null;
      HashMap<String, String> fieldValues = null;
      HashMap<String, String> contentValues = null;
      List<Document> documentList = new ArrayList<>();
      List<Document> regexList = new ArrayList<>();
      List<Document> regexFeatureList = new ArrayList<>();
      List<RegexDocument> regexDocuments = new ArrayList<>();
      RegexDocument regexDocument = null;
      RegexDocumentList regexDocumentList = new RegexDocumentList();
      String contents = "", tokenid="";
      if (((documentgrouplist!=null) && (documentgrouplist.size()>0)) && ((featuregrouplist!=null) && (featuregrouplist.size()>0))) {
         for (String documentStr:documentgrouplist) {
             document = documentDatabase.getDocumentByName(documentStr);
             documentList.add(document);
         } 
         for (String featureStr:featuregrouplist) {
             regexList = documentDatabase.getDocumentByGroup(featureStr);
             for (Document doc:regexList) {
                try {
                     fieldValues = new ObjectMapper().readValue(doc.toJson(), new TypeReference<HashMap<String,String>>() {});
                     contents = fieldValues.get("contents");
                     contents = URLDecoder.decode(contents);
                     contentValues = new ObjectMapper().readValue(contents, new TypeReference<HashMap<String,String>>() {});
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                for (String key:contentValues.keySet()) {
                     fieldValues.put(key, contentValues.get(key));
                }
                regexDocument = new RegexDocument(fieldValues.get("name"), 
                                                  fieldValues.get("group"), 
                                                  fieldValues.get("description"),
                                                  "",
                                                  fieldValues.get("granularity"),
                                                  fieldValues.get("precondition"),
                                                  fieldValues.get("postcondition"),
                                                  fieldValues.get("Invariant"));
                regexDocuments.add(regexDocument);
             }
         }
         regexDocumentList.setRegexDocumentList(regexDocuments);
         tokenid = UUID.randomUUID().toString();
         remoteProcessor.processFeature(regexDocumentList, tokenid);
         fileProcessor.processDocuments(documentList, tokenid);
      }
   }

   @RequestMapping(value = "/processtext", method = RequestMethod.GET)
   public void doAsyncProcessText(@RequestParam List<String> folderlist, @RequestParam String featurelist) {
      Document document = null;
      List<Document> documentList = new ArrayList<>();
      RegexDocumentList regexDocumentList = new RegexDocumentList();
      String contents = "", tokenid="";
      if ((folderlist!=null) && (featurelist!=null)) {
         for (String folderStr:folderlist) {
             document = new Document("",folderStr,"folder",folderStr,"folder");
             documentList.add(document);
         } 
         regexDocumentList.fromJson(featurelist);
         tokenid = UUID.randomUUID().toString();
         remoteProcessor.processFeature(regexDocumentList, tokenid);
         fileProcessor.processDocuments(documentList, tokenid);
      }
   }

    
}