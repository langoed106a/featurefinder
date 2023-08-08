package app.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class FeatureDocumentUpdateStatement implements PreparedStatementCallback<Boolean> {
	FeatureDocument featureDocument;
	
	public FeatureDocumentUpdateStatement(FeatureDocument featureDocument) {
		this.featureDocument = featureDocument;
	}
	
	public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
		Integer paramId = 1;
		Integer id = featureDocument.getId();
		preparedStatement.setBytes(paramId,featureDocument.getContents().getBytes());
		preparedStatement.setString(paramId+1,featureDocument.getDescription());
		if (id !=null ) {
			preparedStatement.setInt(paramId+2,featureDocument.getId());
		}
		return preparedStatement.execute();
	}

}