package app.util.feature;

import javax.annotation.PostConstruct;

import app.util.database.DocumentDatabase;
import app.util.database.FeatureDocument;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import io.swagger.annotations.ApiOperation;
 
@RestController
public class AnalysisController { 

        private static String PROPERTIES_NAME="server.properties";
        private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);
        private ServiceLocator serviceLocator;
        private WekaModelGenerator wekaModelGenerator;
        @Autowired
	private Resttemplate restTemplate;
	@Autowired
	private RemoteDatabase remoteDatabase;
        @Autowired
	private DocumentDatabase documentDatabase;
        @Autowired
	private WebApplicationContext applicationContext;
   
    @PostConstruct
    public void initialise() {
          String properties_location = System.getProperty(PROPERTIES_NAME);
          serviceLocator = new ServiceLocator(properties_location);
          remoteDatabase.setRestTemplate(restTemplate);
          remoteDatabase.setServiceLocator(serviceLocator);
	  documentDatabase.setRemoteDatabase(remoteDatabase);
          wekaModelGenerator = new WekaModelGenerator();
    }	

    @RequestMapping(value = "/buildclassifierfromfile", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Boolean> buildClassifierFromFile(@RequestParam String modellocation) {
	Boolean success = false;
	Model model = null;
        if ((modellocation!=null) && (modellocation.length()>0)) {
           try {
                model = this.readjsonModel(modellocation);
                success = this.createClassifierFromModel(model);
           } catch (Exception exception) {
                exception.printStackTrace();
           }
	  return ResponseEntity.ok(success);
	}

    @RequestMapping(value = "/buildclassifierfromfile", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Boolean> buildClassifierFromModel(@RequestParam String model) {
	Boolean success = false;
        FeatureDocument featureDocument=null;
        Integer intValue=0;
        JSONArray jsonArray = null, rowArray = null;
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();
        List<String> data = null, namesList = null;
	Model model = null;
        Object object=null;
        String contents="", classnames="", dataStr="", modelfilepath="", resultData="", row="", rowValue="", modelName="";
        if ((model!=null) && (model.length()>0)) {
           try {
                resultData = General.decode(model)
                object = jsonParser.parse(resultData);
                if (object != null) {
                   jsonObject = (JSONObject)object;
                   modelName = (String)jsonObject.get("modelname");
                   jsonArray = (JSONArray)jsonObject.get("resultdata");
                   if (jsonArray != null) {
                      data = new ArrayList<>();
                      for (int i=0; i<jsonArray.size(); i++) {
                          rowArray = (JSONArray)jsonArray.get(i);
                          row="";
                          for (int j=0; j<rowArray.size(); j++) {
                              object = rowArray.get(j);
                              rowValue = String.valueOf(object);
                              row = row + rowValue+",";
                          }
                          if (row.length()>0) {
                             row=row.substring(0, row.length()-1);
                          }
                          data.add(row);
                      }
                      featureDocument = documentDatabase.getDocumentByName(modelName, "model");
                      contents = featureDocument.getContents();
                      contents = URLDecoder.decode(contents);
                      object = jsonParser.parse(contents);
                      if (object!=null) {
                         namesList = new ArrayList<>();
                         jsonObject = (JSONObject)object;
                         modelfilepath = (String)jsonObject.get("file");
                         classnames = (String)jsonObject.get("classnames");
                         items = classnames.split(",");
                         for (int k=0; k<items.length; k++) {
                             namesList.add(items[k]);
                         }
                         dataStr = wekaModelGenerator.classify(modelfilepath, data, namesList, true);
                      }
                 }
           } catch (Exception exception) {
                exception.printStackTrace();
           }
	  return ResponseEntity.ok(dataStr);
	}



	@RequestMapping(value = "/adddocument", method = RequestMethod.GET)
	public String adddocument(@RequestParam String documentname, @RequestParam String documenttype, @RequestParam String documentcontents, @RequestParam String documentdescription) {
		String response = "";
		documentDatabase.addDocument(documentname, documenttype, documentcontents, documentdescription);
		return response;
	}

	@RequestMapping(value = "/getdocumentsbytype", method = RequestMethod.GET)
	public List<FeatureDocument> getDocumentsByType(@RequestParam String type) {
		List<FeatureDocument> documents = null;
		documents = documentDatabase.getDocuments(type);
		return documents;
	}

	@RequestMapping(value = "/getdocumentgroup", method = RequestMethod.GET)
	public List<FeatureDocument> getDocumentsByGroup(@RequestParam String groupname) {
		List<FeatureDocument> documents = null;
		documents = documentDatabase.getDocumentByGroup(groupname);
		return documents;
	}

	@RequestMapping(value = "/updatedocument", method = RequestMethod.GET)
	public String updatedocument(@RequestParam String id, @RequestParam String name, @RequestParam String type, @RequestParam String contents, @RequestParam String description) {
		String reply = null;
		reply = documentDatabase.updateDocument(Integer.valueOf(id), name, type, contents, description);
		return reply;
	}

	@RequestMapping(value = "/deletedocument", method = RequestMethod.GET)
	public String deletedocument(@RequestParam String documentid) {
		String reply = null;
		reply = documentDatabase.deleteDocument(Integer.valueOf(documentid));
		return reply;
	}

	@RequestMapping(value = "/getdocumentbyid", method = RequestMethod.GET)
    public FeatureDocument getDocumentById(@RequestParam String documentid) {
        FeatureDocument document = null;
		document = documentDatabase.getDocumentById(Integer.valueOf(documentid));
		return document;
    }
	 
}