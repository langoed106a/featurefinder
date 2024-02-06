package app.util.database;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import app.util.feature.Document;

@Component
public class DocumentDatabase {
    JdbcTemplate jdbcTemplate;

    public DocumentDatabase() {

    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Document getDocument(Integer id) {
        List<Document> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE id="+id,(resultSet, rowNum) -> new Document(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description"),resultSet.getString("label"),resultSet.getString("origin")));
        if ((documents!=null) && (documents.size()>0)) {
            return documents.get(0);
        } else {
            return null;
        }
    }

    public Document getDocumentById(Integer id) {
        List<Document> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE id="+id,(resultSet, rowNum)-> new Document(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description"),resultSet.getString("label"),resultSet.getString("origin")));
        if ((documents!=null) && (documents.size()>0)) {
            return documents.get(0);
        } else {
            return null;
        }
    }

    public Document getDocumentByName(String name) {
        List<Document> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE name=\""+name+"\"",(resultSet, rowNum)-> new Document(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description"),resultSet.getString("label"),resultSet.getString("origin")));
        if ((documents!=null) && (documents.size()>0)) {
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

    public String updateDocument(Document document) {
        String query = "UPDATE featuredocumentstore SET name=?,type=?,contents=?,description=?,label=?,origin=? WHERE id=?";
        Boolean result = jdbcTemplate.execute(query, new FeatureDocumentPreparedStatement(document));
        String reply = "Document has been updated";
        return reply;
    }

    public String addDocument(Document document) {
        byte[] contentBlob;
        String query = "INSERT OR REPLACE INTO featuredocumentstore (name, type, contents, description, label, origin) VALUES (?,?,?,?,?,?)";
        Boolean result = jdbcTemplate.execute(query, new FeatureDocumentPreparedStatement(document));
        String reply = "{\"message\":\"Document has been stored\"}";
        return reply;
    }

    public List<Document> getDocumentByGroup(String groupname) {
        List<Document> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE type=\""+groupname+"\"",(resultSet, rowNum) -> new Document(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description"),resultSet.getString("label"),resultSet.getString("origin")));
        if ((documents!=null) && (documents.size()>0)) {
            return documents;
        } else {
            return null;
        }
    }

    public List<Document> getDocumentByType(String type) {
        List<Document> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE type=\""+type+"\"",(resultSet, rowNum) -> new Document(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description"),resultSet.getString("label"),resultSet.getString("origin")));
        if ((documents!=null) && (documents.size()>0)) {
            return documents;
        } else {
            return null;
        }
    }


}
