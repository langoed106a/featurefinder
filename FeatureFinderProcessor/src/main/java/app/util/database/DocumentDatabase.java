package app.util.database;

import java.util.List;
import java.util.ArrayList;

import java.net.URLDecoder;

import app.util.feature.Document;
import app.util.feature.DocumentStore;
import app.util.feature.RemoteDatabase;

import org.springframework.stereotype.Component;

@Component
public class DocumentDatabase implements DocumentStore {
	RemoteDatabase remoteDatabase;
	
	public DocumentDatabase() {
		
	}
	
	public void setRemoteDatabase(RemoteDatabase remoteDatabase) {
		this.remoteDatabase = remoteDatabase;
	}

	@Override
	public Document getDocumentById(Integer id) {
	    Document document = this.remoteDatabase.getDocumentById(id);
        return document;
	}

	public Document getDocumentById(String id) {
	    Document document = this.getDocumentById(Integer.valueOf(id));
        return document;
	}
	
	@Override
	public List<Document> getDocumentByGroup(String groupname) {
	    List<Document> documentList = this.remoteDatabase.getDocumentByGroup(groupname);
        return documentList;
	}

	@Override
	public Document getDocumentByName(String name) {
	    Document document = this.remoteDatabase.getDocumentByName(name);
        return document;
	}

	public String deleteDocument(String id) {
	   String reply="";
       reply = this.remoteDatabase.deleteDocument(id);
       return reply;
	}
	
	public String updateDocument(Document document) {
	   String reply = "";
       reply = this.remoteDatabase.updateDocument(document);
       return reply;
	}
	
	public String addDocument(Document document) {
	   String reply="";
       reply = this.remoteDatabase.addDocument(document);
       return reply;
	}
	
	@Override
	public List<Document> getDocumentByType(String type) {
	    List<Document> documents = null;
	    documents = this.remoteDatabase.getDocumentByType(type);
		return documents;
	}
	
}