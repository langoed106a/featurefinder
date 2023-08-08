package app.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class FeatureDocumentPreparedStatement implements PreparedStatementCallback<Boolean> {
	FeatureDocument featureDocument;
	
	public FeatureDocumentPreparedStatement(FeatureDocument featureDocument) {
		this.featureDocument = featureDocument;
	}
	
	public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
		Integer paramId = 1;
		Integer id = featureDocument.getId();
		preparedStatement.setString(paramId,featureDocument.getName());
	    preparedStatement.setString(paramId+1,featureDocument.getType());
		preparedStatement.setBytes(paramId+2,featureDocument.getContents().getBytes());
		preparedStatement.setString(paramId+3,featureDocument.getDescription());
		if (id !=null ) {
			preparedStatement.setInt(paramId+4,featureDocument.getId());
		}
		return preparedStatement.execute();
	}

}
