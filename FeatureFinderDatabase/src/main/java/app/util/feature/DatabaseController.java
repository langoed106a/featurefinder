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
		response = documentDatabase.addDocument(document);
		return response;
	}

	@RequestMapping(value = "/getdocumentsbytype", method = RequestMethod.GET)
	public DocumentList getDocumentsByType(@RequestParam String type) {
		DocumentList documentList = new DocumentList();
		Document document = null;
		List<Document> documents = new ArrayList<>();
		String[] parts=null;
		String function="";
        documents = documentDatabase.getDocumentByType(type);
		if (documents!=null) {
		    documentList.setDocumentList(documents);
		}
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

    @RequestMapping(value = "/updatedocument", method = RequestMethod.POST)
	public String updatedocument(@RequestBody Document document) {
		String response = "";
		response = documentDatabase.updateDocument(document);
		return response;
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