package app.util.database;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentDatabase {
    JdbcTemplate jdbcTemplate;

    public DocumentDatabase() {

    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FeatureDocument getDocument(Integer id) {
        List<FeatureDocument> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE id="+id,(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>=0)) {
            return documents.get(0);
        } else {
            return null;
        }
    }

    public FeatureDocument getDocumentById(Integer id) {
        List<FeatureDocument> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE id="+id,(resultSet, rowNum)-> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>=0)) {
            return documents.get(0);
        } else {
            return null;
        }
    }

    public String deleteDocument(Integer id) {
        Integer row = jdbcTemplate.update("DELETE FROM featuredocumentstore WHERE id="+id);
        String result = "Document has been deleted from row:"+row;
        return result;
    }

    public String updateDocument(Integer id, String name, String type, String contents, String description) {
        FeatureDocument featureDocument = new FeatureDocument(id, name, type, contents, description);
        String query = "UPDATE featuredocumentstore SET name=?,type=?,contents=?,description=? WHERE id=?";
        Boolean result = jdbcTemplate.execute(query, new FeatureDocumentPreparedStatement(featureDocument));
        String reply = "Document has been updated";
        return reply;
    }

    public String addDocument(String name, String type, String contents, String description) {
        byte[] contentBlob;
        FeatureDocument featureDocument = new FeatureDocument(null, name, type, contents, description);
        String query = "INSERT INTO featuredocumentstore (name, type, contents, description) VALUES (?,?,?,?)";
        Boolean result = jdbcTemplate.execute(query, new FeatureDocumentPreparedStatement(featureDocument));
        String reply = "Document has been stored";
        return reply;
    }

    public List<FeatureDocument> getDocumentByGroup(String groupname) {
        List<FeatureDocument> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE type=\""+type+"\"",(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>=0)) {
            return documents;
        } else {
            return null;
        }
    }

    public List<FeatureDocument> getDocuments(String type) {
        List<FeatureDocument> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE type=\""+type+"\"",(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>=0)) {
            return documents;
        } else {
            return null;
        }
    }


}
