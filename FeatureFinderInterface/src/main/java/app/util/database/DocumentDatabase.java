package app.util.database;

import java.util.List;
import java.util.ArrayList;

import java.net.URLDecoder;

import app.util.feature.FeatureStore;
import app.util.feature.Feature;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentDatabase implements FeatureStore {
	JdbcTemplate jdbcTemplate;
	
	public DocumentDatabase() {
		
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Feature getFeatureById(Integer id) {
		return this.getDocumentById(id);
	}
	
	public FeatureDocument getDocumentById(Integer id) {
		List<FeatureDocument> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE id="+id,(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>=0)) {
        	return documents.get(0);
        } else {
        	return null;
        }
	}

    @Override
	public Feature getFeatureByName(String name) {
		String query = "SELECT * FROM featuredocumentstore WHERE name=\""+name+"\"";
		String contents="";
		FeatureDocument featureDocument = null;
		List<FeatureDocument> documents = jdbcTemplate.query(query,(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>0)) {
			featureDocument = documents.get(0);
			contents = featureDocument.getContents();
			try {
				contents = URLDecoder.decode( contents, "UTF-8" );
				featureDocument.setContents(contents);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
        } 
	   return (Feature)featureDocument;	
	}

	@Override
	public List<Feature> getFeaturesByType(String type) {
		String query = "SELECT * FROM featuredocumentstore WHERE type=\""+type+"\"";
		System.out.println("***Query:"+query);
		List<FeatureDocument> documents = jdbcTemplate.query(query,(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));  
		List<Feature> featureList = new ArrayList<>();
		for (FeatureDocument featureDocument:documents) {
			  featureList.add((Feature)featureDocument);
		}
		return featureList;
	}
	
	public FeatureDocument getDocument(String name, String type) {
		String query = "SELECT * FROM featuredocumentstore WHERE name=\""+name+"\" and type=\""+type+"\"";
		System.out.println("***Query:"+query);
		List<FeatureDocument> documents = jdbcTemplate.query(query,(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
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
	
	public String updateDocument(Integer id, String name, String type, String contents, String description) {
		FeatureDocument featureDocument = new FeatureDocument(id, name, type, contents, description);
		String query = "UPDATE featuredocumentstore SET name=?,type=?,contents=?,description=? WHERE id=?";
		Boolean result = jdbcTemplate.execute(query, new FeatureDocumentPreparedStatement(featureDocument));
		String reply = "Document has been updated";
        return reply;
	}
	
	public synchronized String addDocument(String name, String type, String contents, String description) {
        byte[] contentBlob;
		Boolean result = false;
		FeatureDocument featureDocument = null;
		String query = "";
		featureDocument = this.getDocument(name,type);
		if (featureDocument==null) {
              featureDocument = new FeatureDocument(null, name, type, contents, description);
		      query = "INSERT INTO featuredocumentstore (name, type, contents, description) VALUES (?,?,?,?)";
		      result = jdbcTemplate.execute(query, new FeatureDocumentPreparedStatement(featureDocument));
		} else {
			  featureDocument.setContents(contents);
			  featureDocument.setDescription(description);
			  query = "UPDATE featuredocumentstore SET contents=?,description=? where id=?";
              result = jdbcTemplate.execute(query, new FeatureDocumentUpdateStatement(featureDocument));
		}	  
		String reply = "Document has been stored";
        return reply;
	}
	
	public List<FeatureDocument> getDocuments(String type) {
		List<FeatureDocument> documents = null;
		String clause = "", part = "";
		String[] parts = type.split(",");
		Integer index=0;
		while (index<(parts.length-1)) {
			part = parts[index];
		    clause = clause + "\""+part+"\",";
			index = index + 1;	
		}	
		clause = clause + "\""+parts[parts.length-1]+"\"";
		documents =	jdbcTemplate.query("SELECT * FROM featuredocumentstore WHERE type in ("+clause+")",(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getBinaryStream("contents"),resultSet.getString("description")));
        if ((documents!=null) && (documents.size()>=0)) {
        	return documents;
        } else {
        	return null;
        }
	}
	

}
