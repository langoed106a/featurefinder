package app.util.feature;

import java.util.HashMap;
import java.util.Map;

public class FeatureResult {
    String documentname;
    Map<String, Integer> matches;

    public FeatureResult() {

    }

    public FeatureResult(String documentName) {
        this.documentname=documentName;
        this.matches = new HashMap<>();
    }

    public String getDocumentName() {
        return documentname;
    }

    public Map<String, Integer> getMatches() {
        return matches;
    }

    public void addMatch(String featurename, Integer match) {
        matches.put(featurename, match);
    }

    public String toString() {
        Integer count=0;
        String reply="";
        String matchesReply="";
        String feature="";
        reply = "{\"documentname\":\""+ documentname+"\",";
        matchesReply="[";
        for (String key:matches.keySet()) {
            feature = key;
            count = (Integer) matches.get(key);
            matchesReply = matchesReply + "{\"featurename\":\""+feature+"\",\"matches\":\""+count+"\"},";
        }
        matchesReply = matchesReply + matchesReply.substring(0,matchesReply.length()-1);
        reply = reply + "[" + matchesReply + "]}";
        return reply;
    }

}