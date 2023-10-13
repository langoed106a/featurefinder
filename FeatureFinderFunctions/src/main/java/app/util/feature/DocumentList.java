package app.util.feature;

import java.util.ArrayList;
import java.util.List;

public class DocumentList {
    private List<Document> documentList;

    public DocumentList() {
        documentList = new ArrayList<>();
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList (List<Document> documentList) {
        this.documentList = documentList;
    }
}