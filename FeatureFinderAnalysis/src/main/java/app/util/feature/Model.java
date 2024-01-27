package app.util.feature;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONValue;

public class Model {
     Map<String, String> functional;
     Map<String, String> structural;
     String name;

    public Model(String name) {
        functional = new HashMap<>();
        structural = new HashMap<>();
        functional.put("acount","2.55,3.47");
        functional.put("atcount","0.0,0.66");
        functional.put("conjunctioncount","2.25,5.0");
        functional.put("iscount","1,20");
        functional.put("infinitive","1,20");
        functional.put("punctuationcount","1,20");
        functional.put("pronouns","1.5,12.0");
        functional.put("fullstopcount","1,20");
        functional.put("forcount","0.9,1.39");
        functional.put("oncount","1,20");
        functional.put("thatwhichcount","0.86,2.63");
        functional.put("thiscount","0.6,1.47");
        functional.put("thecount","4.25,7.36");
        functional.put("tocount","2.4,3.47");
        functional.put("suchcount","1,20");
        functional.put("presentparticiple","1,20");
        functional.put("ofcount","2.99,4.");
        functional.put("althoughcount","1,20");
        functional.put("presentpassive","1,20");
        functional.put("butcount","1,20");
        functional.put("becausecount","1,20");
        functional.put("andcount","1,20");
        functional.put("forcount","1,20");
        functional.put("orcount","1,20");
        functional.put("thatcount","1,20");
        functional.put("fromcount","1,20");
        functional.put("ascount","1,20");
        functional.put("semicoloncount","1,20");
        functional.put("withcount","1,20");
        functional.put("whichcount","1,20");
        functional.put("withfrominoncount","2.01,4.52");
        functional.put("incount","1,20");
        structural.put("infinitive","1,20");     
        structural.put("ofcount","1,20");
        structural.put("complexsentences","15.0,40.0");
        structural.put("infinitive","14.0,25.0");
        structural.put("impersonal","0.0,8.0");
        structural.put("meansentencelength","10.0,25.0");
        structural.put("mainverb","50.0,80.0");
        structural.put("multiclause","20.0,40.0");
        structural.put("misspeltcount","0.0,5.0");
        structural.put("multipasttense","0.1,20.0");
        structural.put("numberregularword","1,20");
        structural.put("nonnativecomma","10.0,25.0");
        structural.put("nomainverb","0.0,0.0");
        structural.put("numberregularword","25.0,45.0");
        structural.put()"participle","10,25");
        structural.put("passives","5.0,17.5");
        structural.put("presentparticiple","1,20");
        structural.put("presentonly","0.0,50.0");
        structural.put("reportedspeech","0.0,1000.0");
        structural.put("reportedspeechcheck","0.0,2000.0");
        structural.put("sentencecount","0.0,999.99");
        structural.put("andcount","1,20");
        structural.put("tocount","1,20");
        structural.put("forcount","1,20");
        structural.put("orcount","1,20");
        structural.put("thatcount","1,20");
        structural.put("fromcount","1,20");
        structural.put("atcount","1,20");
        structural.put("ascount","1,20");
        structural.put("semicoloncount","1,20");
        structural.put("singleverb","20.0,50.0");
        structural.put("wordcount","0.0,9999.99")
        structural.put("whichcount","1,20");
        structural.put("incount","1,20");
        structural.put("uncommonwords","28.0,45.0");
        structural.put("uniquewords","30.0,55.0");
        structural.put("uniqueuncommon","17.0,30.0"); 
    }

    public void fromJson(JSONValue jsonValue) {

    }

    public List<String> getFeatureList() {
        List<String> featureList = null;
        return featureList;
    }

    public String getThreshold(String type, String name) {
        String threshold="";
        if (type.equalsIgnoreCase("functional")) {
            threshold = functional.get(name);
        } else if (type.equalsIgnoreCase("structural")) {
            threshold = structural.get(name);
        } 
      return threshold;
    }
}