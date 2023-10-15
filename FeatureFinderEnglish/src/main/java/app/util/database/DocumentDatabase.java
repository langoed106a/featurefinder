package app.util.database;

import java.util.List;
import java.util.ArrayList;

import java.net.URLDecoder;

import app.util.feature.DocumentStore;
import app.util.feature.Document;
import app.util.feature.FeatureDocument;
import app.util.feature.RegexDocument;
import app.util.feature.RemoteDatabase;

import org.springframework.stereotype.Component;

@Component
public class DocumentDatabase {
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

	public List<FeatureDocument> getDocumentByType(String type) {
	    List<FeatureDocument> featureDocumentList = this.remoteDatabase.getDocumentByType(type);
        return featureDocumentList;
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
	
}