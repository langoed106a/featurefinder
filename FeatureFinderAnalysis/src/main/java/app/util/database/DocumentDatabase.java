package app.util.database;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
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
       FeatureDocument featureDocument=null;
       featureDocument = remoteDatabase.getDocumentById(id);
       return featureDocument;
    }

    public String deleteDocument(String id) {
       String reply;
       reply = remoteDatabase.deleteDocument(id);
       return reply;
    }

    public List<FeatureDocument> getDocumentByGroup(String groupname) {
       List<FeatureDocument> featureDocumentList=null;
       featureDocumentList = remoteDatabase.getDocumentByGroup(groupname);
       return featureDocumentList;
    }

    public List<FeatureDocument> getDocumentByType(String type) {
       List<FeatureDocument> featureDocumentList=null;
       featureDocumentList = remoteDatabase.getDocumentByType(type);
       return featureDocumentList;
    }

    public FeatureDocument getDocumentByName(String type, String name) {
       FeatureDocument featureDocument=null;
       featureDocument = remoteDatabase.getDocumentByName(type, name);
       return featureDocument;
    }

    public String updateDocument(Integer id, String name, String type, String contents, String description) {
       String reply="";
       reply = remoteDatabase.updateDocument(id, name, type, contents, description);
       return reply;
    }
   
    public String addDocument(String name, String type, String contents, String description) {
       String reply="";
       reply = remoteDatabase.addDocument(name, type, contents, description);
       return reply;
    }

}
