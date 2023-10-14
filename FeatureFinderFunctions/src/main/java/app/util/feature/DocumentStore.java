package app.util.feature;

import java.util.List;

public interface DocumentStore {
    public Document getDocumentById(Integer id);
    public Document getDocumentByName(String name);
    public List<Document> getDocumentByType(String type);
}