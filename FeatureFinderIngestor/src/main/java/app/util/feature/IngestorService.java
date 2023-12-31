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
   public void doAsyncProcessDocuments(@RequestParam String documentgrouplist, @RequestParam String featuregrouplist, String runname) {
      Document document = null;
      HashMap<String, String> fieldValues = null;
      HashMap<String, String> contentValues = null;
      List<Document> documentList = new ArrayList<>();
      List<Document> regexList = new ArrayList<>();
      List<Document> regexFeatureList = new ArrayList<>();
      List<String> documentgroup=null;
      List<String> featuregroup=null;
      List<RegexDocument> regexDocuments = new ArrayList<>();
      RegexDocument regexDocument = null;
      RegexDocumentList regexDocumentList = new RegexDocumentList();
      String contents = "";
      String[] regexArray = null;
      documentgroup = this.getGroupList(documentgrouplist);
      featuregroup = this.getGroupList(featuregrouplist);
      if (((documentgroup!=null) && (documentgroup.size()>0)) && ((featuregroup!=null) && (featuregroup.size()>0))) {
         for (String documentStr:documentgroup) {
             document = documentDatabase.getDocumentByName(documentStr);
             contents = document.getContents();
             contents = URLDecoder.decode(contents);
             document.setContents(URLDecoder.decode(contents));
             documentList.add(document);
         } 
         for (String featureStr:featuregroup) {
             document = documentDatabase.getDocumentByName(featureStr);
             contents = document.getContents();
             contents = URLDecoder.decode(contents);
             if ((contents != null) && (contents.length()>0)) {
                 regexArray = contents.split(",");
                 for (int k=0; k<regexArray.length; k++) {
                     document = documentDatabase.getDocumentByName(regexArray[k]);
                     regexList.add(document);
                 }
                 for (Document doc:regexList) {
                    try {
                        regexDocument = new RegexDocument();
                        regexDocument.fromDocument(doc);
                        regexDocuments.add(regexDocument);
                     } catch (Exception exception) {
                            exception.printStackTrace();
                     }
                 }
             }
         }
         regexDocumentList.setRegexDocumentList(regexDocuments);
         regexDocumentList.setMessageType("add");
         remoteProcessor.processFeature(regexDocumentList, runname);
         fileProcessor.processDocuments(documentList, runname);
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

   private List<String> getGroupList(String groupList) {
      String[] strList=null;
      List<String> grpList = new ArrayList<>();
      if (groupList !=null) {
         strList = groupList.split(",");
         for (int i=0; i<strList.length; i++) {
            grpList.add(strList[i]);
         }
      }
      return grpList;
   }

    
}