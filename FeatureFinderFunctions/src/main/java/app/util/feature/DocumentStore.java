package app.util.feature;

import java.util.List;

public interface DocumentStore {
    public Document getFeatureById(Integer id);
    public Document getFeatureByName(String name);
    public List<Document> getFeaturesByType(String type);
}