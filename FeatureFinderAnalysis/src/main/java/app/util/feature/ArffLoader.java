package app.util.feature;

import java.io.InputStream;

import weka.core.Instances;

public class ArffLoader {
    Instances instances;
    
    public ArffLoader() {

    }

    public void setSource(InputStream stream) {

    }

    public Instances getDataSet() {
        return instances;

    }

}