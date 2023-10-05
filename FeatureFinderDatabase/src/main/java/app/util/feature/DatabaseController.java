package app.util.feature;

import javax.annotation.PostConstruct;

import app.util.database.DocumentDatabase;
import app.util.feature.FeatureDocument;
import app.util.feature.FeatureDocumentList;

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
public class DatabaseController { 

	@Autowired
	private DocumentDatabase documentDatabase;
	@Autowired
	private JdbcTemplate jdbcTemplate;
    @Autowired
	private WebApplicationContext applicationContext;
   
    @PostConstruct
    public void initialise() {
	  documentDatabase.setJdbcTemplate(jdbcTemplate);
    }	

	@RequestMapping(value = "/adddocument", method = RequestMethod.GET)
	public String adddocument(@RequestParam String documentname, @RequestParam String documenttype, @RequestParam String documentcontents, @RequestParam String documentdescription) {
		String response = "";
		documentDatabase.addDocument(documentname, documenttype, documentcontents, documentdescription);
		return response;
	}

	@RequestMapping(value = "/getdocumentsbytype", method = RequestMethod.GET)
	public FeatureDocumentList getDocumentsByType(@RequestParam String type) {
		FeatureDocumentList featureDocumentList = new FeatureDocumentList();
		List<FeatureDocument> documents = null;
		documents = documentDatabase.getDocuments(type);
		featureDocumentList.setFeatureDocumentList(documents);
		return featureDocumentList;
	}

	@RequestMapping(value = "/getdocumentgroup", method = RequestMethod.GET)
	public FeatureDocumentList getDocumentsByGroup(@RequestParam String groupname) {
		FeatureDocumentList featureDocumentList = new FeatureDocumentList();
		List<FeatureDocument> documents = null;
		documents = documentDatabase.getDocumentByGroup(groupname);
		featureDocumentList.setFeatureDocumentList(documents);
		return featureDocumentList;
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