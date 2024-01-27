package app.util.database;

import java.util.List;

import org.springframework.stereotype.Component;

import app.util.feature.Document;
import app.util.feature.RemoteDatabase;

@Component
public class DocumentDatabase {
    RemoteDatabase remoteDatabase;

    public DocumentDatabase() {

    }

    public void setRemoteDatabase(RemoteDatabase remoteDatabase) {
        this.remoteDatabase = remoteDatabase;
    }

    public Document getDocumentById(String id) {
       Document document=null;
       document = remoteDatabase.getDocumentById(Integer.valueOf(id));
       return document;
    }

    public String deleteDocument(String id) {
       String reply;
       reply = remoteDatabase.deleteDocument(id);
       return reply;
    }

    public List<Document> getDocumentByGroup(String groupname) {
       List<Document> documentList=null;
       documentList = remoteDatabase.getDocumentByGroup(groupname);
       return documentList;
    }

    public List<Document> getDocumentByType(String type) {
       List<Document> documentList=null;
       documentList = remoteDatabase.getDocumentByType(type);
       return documentList;
    }

    public Document getDocumentByName(String name) {
       Document document=null;
       document = remoteDatabase.getDocumentByName(name);
       return document;
    }

    public String updateDocument(Document document) {
       String reply="";
       reply = remoteDatabase.updateDocument(document);
       return reply;
    }
   
    public String addDocument(Document document) {
       String reply="";
       reply = remoteDatabase.addDocument(document);
       return reply;
    }

}
