package app.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

import app.util.feature.Document;

public class FeatureDocumentPreparedStatement implements PreparedStatementCallback<Boolean> {
    Document document;

    public FeatureDocumentPreparedStatement(Document document) {
        this.document = document;
    }

    public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
        Integer paramId = 1;
        String idStr = document.getId();
        preparedStatement.setString(paramId,document.getName());
        preparedStatement.setString(paramId+1,document.getType());
        preparedStatement.setBytes(paramId+2,document.getContents().getBytes());
        preparedStatement.setString(paramId+3,document.getDescription());
        preparedStatement.setString(paramId+4,document.getLabel());
        preparedStatement.setString(paramId+5,document.getOrigin());
        if (idStr !=null ) {
            preparedStatement.setInt(paramId+6,Integer.valueOf(document.getId()));
        }
        return preparedStatement.execute();
    }

}

