package app.util.database;

import java.util.List;
import java.util.ArrayList;

import java.net.URLDecoder;

import app.util.feature.FeatureStore;
import app.util.feature.Feature;
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

        public Integer getNumberOfDocumentByName(String name, String type) {
	    Integer number=0;
	    number = this.remoteDatabase.getNumberOfDocumentByName(name, type);
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

        public Integer getNumberOfDocumentByType(String type) {
	    Integer number=0;
	    number = this.remoteDatabase.getNumberOfDocumentByType(type);
	}
	
	public List<FeatureDocument> getDocumentByType(String type) {
	    List<FeatureDocument> documents = null;
	    documents = this.remoteDatabase.getDocumentByType(type);
	}
	
}
