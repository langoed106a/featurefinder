package app.util.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.net.URLDecoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import app.util.feature.FeatureFunction;
import app.util.database.DocumentDatabase;
import app.util.database.FeatureDocument;

@Component
public class RegexLibrary {
   private Map<String, String> featureregexes;
   private DocumentDatabase documentDatabase;
   
   public RegexLibrary() {
	 
   }
   
   public void setDependencies(DocumentDatabase documentDatabase) {
	   this.documentDatabase = documentDatabase;
   }
   
   public String getFeatureRegex(String featurename) {
	  String featureRegex="";
	  String itemStr="";
	  if (featurename!=null) {
		  itemStr = (String)featureregexes.get(featurename);
		  featureRegex = itemStr;
	  }
	  return featureRegex;
   }

   public String getFeatureRegexContents(String featurename) {
	  Integer position=0; 
	  String featureRegex="";
	  String itemStr="";
	  if (featurename!=null) {
		  itemStr = (String)featureregexes.get(featurename);
		  if (itemStr!=null) {
			  position=itemStr.indexOf("\"contents\":");
			  if (position>0) {
				  itemStr = itemStr.substring(position+11,itemStr.length());
				  itemStr = itemStr.replace("\"","");
				  itemStr = itemStr.replace("}","");
				  try {
				        itemStr = URLDecoder.decode(itemStr,"UTF-8");
						featureRegex = itemStr;
				  } catch (Exception exception) {
					  itemStr="";
				  }		
			  }
		  }
	  }
	  return featureRegex;
   }
   
   public Map<String, String> getFeatures(String type, WordStorage wordStorage) {
	   FeatureDocument listDocument = null;
	   Integer listId=500;
	   List<FeatureDocument> featureDocuments = null;
	   Map<String, String> featuresMap = new HashMap<>();
	   Map<String, String> listMap = null;
	   String jsonStr="";
	   String[] functionList=null, parts=null; 
	   functionList = FeatureFunction.FUNCTION_FEATURES;
	   for (String functionStr:functionList) {
		   parts = functionStr.split(":");
		   jsonStr = "{\"name\":\""+parts[0]+"\",\"description\":\""+parts[1]+"\",\"type\":\""+parts[2]+"\",\"group\":\""+parts[3]+"\",\"contents\":\""+parts[4]+"\"}";
		   featuresMap.put(parts[0], jsonStr);
	   }
	   if ((type!=null) && (type.length()>0)) {
	       featureDocuments = documentDatabase.getDocuments(type);
	       for (FeatureDocument featureDocument:featureDocuments)  {
		        featuresMap.put(featureDocument.getName(), featureDocument.toString());
	       }
		   if (type.contains("list")) {
                 listMap = wordStorage.getNameofWordLists();
				 for (String key:listMap.keySet()) {
					 listDocument = new FeatureDocument(listId, key, "list", " ", listMap.get(key));
					 featuresMap.put(key, listDocument.toString());
				 }
		   }
	   }	   
      return featuresMap;	  
   }
   
}
