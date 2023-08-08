package app.util.feature;


import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    
    import org.json.simple.JSONArray;
    import org.json.simple.JSONObject;
    
    public class PostagSearch {
        Map<String,String> postagMap;
       
    public PostagSearch() {
    }
    public void setPostags(List<Object> postags) {
    JSONArray jsonArray = null;
    JSONObject jsonObject = (JSONObject)postags.get(0);
    String jsonType, jsonPostag, jsonDescription;
    jsonArray = (JSONArray)jsonObject.get("penntreebank");
    postagMap = new HashMap<>();
    if (jsonArray!=null) {
    for (Object jsonItem:jsonArray) {
    jsonObject = (JSONObject) jsonItem;
    jsonType = (String)jsonObject.get("type");
           jsonPostag = (String)jsonObject.get("postag");
           jsonDescription = (String)jsonObject.get("description");
           postagMap.put(jsonPostag, jsonType+":"+jsonDescription);
    }
    }
    }
    public boolean isGerund(String name, String postag) {
    Boolean isgorund=false;
    if (name.endsWith("ing")) {
    isgorund = this.isPostag(postag, "adjective");
    }    
    return isgorund;
    }
    public boolean isPostag(String name, String postag, String type) {
    Boolean ispostag=false;
    String postagType="", postagDescription="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(name);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       postagDescription = parts[1];
       if ((postagType!=null) && (postagType.equalsIgnoreCase(postag)) && (postagDescription.equalsIgnoreCase(type))) {
      ispostag = true;
       }
    }    
    return ispostag;
    }
    public boolean isPostag(String postag, String name) {
    Boolean ispostag=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       if ((postagType!=null) && (postagType.equalsIgnoreCase(name))) {
      ispostag = true;
       }
    }    
    return ispostag;
    }
    public boolean isPronoun(String postag) {
    Boolean ispostag=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       if ((postagType!=null) && (postagType.equalsIgnoreCase("pronoun"))) {
       ispostag = true;
       }    
    }
    return ispostag;
    }
    public boolean isNoun(String postag) {
    Boolean ispostag=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       if ((postagType!=null) && (postagType.equalsIgnoreCase("noun"))) {
       ispostag = true;
       }    
    }
    return ispostag;
    }
    public boolean isTo(String postag) {
    Boolean ispostag=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       if ((postagType!=null) && (postagType.equalsIgnoreCase("to"))) {
       ispostag = true;
       }    
    }
    return ispostag;
    }
    public boolean isAdjective(String postag) {
    Boolean isverb=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
    parts = postagItem.split(":");
    postagType = parts[0];
    if ((postagType!=null) && (postagType.equalsIgnoreCase("adjective"))) {
    isverb= true;
    }
    }
    return isverb;
    }
    public boolean isDeterminer(String postag) {
    Boolean isverb=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
    parts = postagItem.split(":");
    postagType = parts[0];
    if ((postagType!=null) && (postagType.equalsIgnoreCase("determiner"))) {
    isverb= true;
    }
    }
    return isverb;
    }
    public boolean isVerb(String postag) {
    Boolean isverb=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
    parts = postagItem.split(":");
    postagType = parts[0];
    if ((postagType!=null) && (postagType.equalsIgnoreCase("verb"))) {
    isverb= true;
    }
    }
    return isverb;
    }
    public boolean isPastParticiple(String postag) {
    Boolean ispastParticiple=false;
    String[] parts = null;
    String postagItem="", postagType="", postagDescription="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       postagDescription = parts[1];
       if ((postagType!=null) && (postagType.equalsIgnoreCase("verb")) && (postagDescription.equalsIgnoreCase("past"))) {
       ispastParticiple= true;
       }
    }
    return ispastParticiple;
    }
    public boolean isPresentVerb(String postag) {
    Boolean ispastParticiple=false;
    String[] parts = null;
    String postagItem="", postagType="", postagDescription="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
       parts = postagItem.split(":");
       postagType = parts[0];
       postagDescription = parts[1];
       if ((postagType!=null) && (postagType.equalsIgnoreCase("verb")) && (postagDescription.equalsIgnoreCase("base"))) {
       ispastParticiple= true;
       }
    }
    return ispastParticiple;
    }
    public boolean isAdverb(String postag) {
    Boolean isverb=false;
    String postagType="";
    String[] parts = null;
    String postagItem="";
    postagItem = postagMap.get(postag);
    if (postagItem != null) {
    parts = postagItem.split(":");
    postagType = parts[0];
    if ((postagType!=null) && (postagType.equalsIgnoreCase("adverb"))) {
    isverb= true;
    }
    }
    return isverb;
    }
    }
