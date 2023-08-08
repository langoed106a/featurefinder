package app.util.feature;

import java.util.List;

public interface FeatureStore {
    public Feature getFeatureById(Integer id);
    public Feature getFeatureByName(String name);
    public List<Feature> getFeaturesByType(String type);
}