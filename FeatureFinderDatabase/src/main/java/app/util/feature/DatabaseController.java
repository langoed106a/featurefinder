package app.util.feature;

import javax.annotation.PostConstruct;

import app.util.database.DocumentDatabase;
import app.util.feature.Document;
import app.util.feature.DocumentList;
import app.util.feature.FeatureFunction;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import io.swagger.annotations.ApiOperation;
 
@RestController
public class DatabaseController { 
	private String[] functionList;
	private static String FUNCTION_TYPE="function";
	@Autowired
	private DocumentDatabase documentDatabase;
	@Autowired
	private JdbcTemplate jdbcTemplate;
    @Autowired
	private WebApplicationContext applicationContext;
   
    @PostConstruct
    public void initialise() {
	  documentDatabase.setJdbcTemplate(jdbcTemplate);
	  functionList = FeatureFunction.FUNCTION_FEATURES;
    }	

	@RequestMapping(value = "/adddocument", method = RequestMethod.POST)
	public String adddocument(@RequestBody Document document) {
		String response = "";
		documentDatabase.addDocument(document.getName(), document.getType(), document.getContents(), document.getDescription());
		return response;
	}

	@RequestMapping(value = "/getdocumentsbytype", method = RequestMethod.GET)
	public DocumentList getDocumentsByType(@RequestParam String type) {
		DocumentList documentList = new DocumentList();
		Document document = null;
		List<Document> documents = new ArrayList<>();
		String[] parts=null;
		String function="";
		if (type.equalsIgnoreCase(FUNCTION_TYPE)) {
            for (int i=0; i<functionList.length; i++) {
			   function = functionList[i];
			   parts = function.split(":");
               document = new Document(null, parts[0], FUNCTION_TYPE, parts[3], parts[1]);
               documents.add(document);
			}
		} else {
		      documents = documentDatabase.getDocumentByType(type);
		}
		documentList.setDocumentList(documents);
		return documentList;
	}

	@RequestMapping(value = "/getdocumentgroup", method = RequestMethod.GET)
	public DocumentList getDocumentsByGroup(@RequestParam String groupname) {
		DocumentList documentList = new DocumentList();
		List<Document> documents = null;
		documents = documentDatabase.getDocumentByGroup(groupname);
		documentList.setDocumentList(documents);
		return documentList;
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
    public Document getDocumentById(@RequestParam String documentid) {
        Document document = null;
		document = documentDatabase.getDocumentById(Integer.valueOf(documentid));
		return document;
    }

	@RequestMapping(value = "/getdocumentbyname", method = RequestMethod.GET)
    public Document getDocumentByName(@RequestParam String name) {
        Document document = null;
		document = documentDatabase.getDocumentByName(name);
		return document;
    }
	 
}