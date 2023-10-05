package app.util.feature;

import java.util.ArrayList;
import java.util.List;

public class FeatureDocumentList {
    private List<FeatureDocument> featureDocumentList;

    public FeatureDocumentList() {
        featureDocumentList = new ArrayList<>();
    }

    public List<FeatureDocument> getFeatureDocumentList() {
        return featureDocumentList;
    }

    public void setFeatureDocumentList (List<FeatureDocument> featureDocumentList) {
        this.featureDocumentList = featureDocumentList;
    }
}