package app.util.database;

import java.util.List;
import java.util.ArrayList;

import java.net.URLDecoder;

import app.util.feature.FeatureStore;
import app.util.feature.Feature;
import app.util.feature.RegexFeature;
import app.util.feature.RemoteDatabase;

import org.springframework.stereotype.Component;

@Component
public class DocumentDatabase implements FeatureStore {
	RemoteDatabase remoteDatabase;
	
	public DocumentDatabase() {
		
	}
	
	public void setRemoteDatabase(RemoteDatabase remoteDatabase) {
		this.remoteDatabase = remoteDatabase;
	}

	
	public FeatureDocument getDocumentById(String id) {
	    FeatureDocument featureDocument = this.remoteDatabase.getDocumentById(id);
            return featureDocument;
	}
	
	public FeatureDocument getDocumentByName(String name, String type) {
	    FeatureDocument featureDocument = this.remoteDatabase.getDocumentByName(name, type);
            return featureDocument;
	}

	public String deleteDocument(String id) {
	   String reply="";
           reply = this.remoteDatabase.deleteDocument(id);
           return reply;
	}
	
	public String updateDocument(Integer id, String name, String type, String contents, String description) {
	   String reply = "";
           reply = this.remoteDatabase.updateDocument(id, name, type, contents, description);
           return reply;
	}
	
	public String addDocument(String name, String type, String contents, String description) {
	   String reply="";
           reply = this.remoteDatabase.addDocument(name, type, contents, description);
           return reply;
	}
	
	public List<FeatureDocument> getDocumentByType(String type) {
	    List<FeatureDocument> documents = null;
	    documents = this.remoteDatabase.getDocumentByType(type);
		return documents;
	}

	public Feature getFeatureById(Integer id) {
		FeatureDocument featureDocument = this.getDocumentById(id.toString());
		RegexFeature regexFeature = null;
		return regexFeature;
	}

    public Feature getFeatureByName(String name) {
		FeatureDocument featureDocument = this.getDocumentByName(name, "regex");
		RegexFeature regexFeature = null;
		return regexFeature;
	}

    public List<Feature> getFeaturesByType(String type) {
		List<FeatureDocument> featureDocumentList = this.getDocumentByType(type);
		RegexFeature regexFeature = null;
		List<Feature> featureList = null;
		return featureList;
	}
	
}
