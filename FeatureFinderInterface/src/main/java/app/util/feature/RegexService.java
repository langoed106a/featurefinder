package app.util.feature;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.text.DateFormat;   
import java.util.Date; 

import java.net.URLEncoder;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import app.util.database.DocumentDatabase;
import app.util.feature.FeatureDocument;
 
@CrossOrigin
@RestController
public class RegexService { 
	private static String RESULTS_LOCATION="/tmp";
	private static String DEFAULT_LANGUAGE="english";
	private static String DEFAULT_GRANULARITY="text";
	private static String PROPERTIES_NAME="server.properties";
	private static final Logger logger=LoggerFactory.getLogger(RegexService.class);
	private ContractFunction contractFunction;
	private FeatureFunction featureFunction;
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;

	@Autowired
	private RestTemplate restSimpleTemplate;
	@Autowired
    private RestTemplate restLoadBalancedTemplate;
	@Autowired
	private RemoteAnalyzer remoteAnalyzer;
	@Autowired
	private RemoteParser remoteParser;
	@Autowired
	private RemoteBatch remoteBatch;
	@Autowired
	private RemoteDatabase remoteDatabase;
	@Autowired
	private RemoteSpellChecker remoteSpellChecker;
	@Autowired
	Documentation documentation;
	@Autowired
	DocumentDatabase documentDatabase;
	@Autowired
	private WebApplicationContext applicationContext;
	
	@PostConstruct
	public void initialise() {
		featureFunction = new FeatureFunction();
		String properties_location = System.getProperty(PROPERTIES_NAME);
		serviceLocator = new ServiceLocator(properties_location);
	
		remoteParser.setRestTemplate(restSimpleTemplate);
		remoteParser.setServiceLocator(serviceLocator);

		remoteDatabase.setRestTemplate(restSimpleTemplate);
		remoteDatabase.setServiceLocator(serviceLocator);

		remoteAnalyzer.setRestTemplate(restSimpleTemplate);
		remoteAnalyzer.setServiceLocator(serviceLocator);

		remoteBatch.setRestTemplate(restLoadBalancedTemplate);
		remoteBatch.setServiceLocator(serviceLocator);
		contractFunction = new ContractFunction(featureFunction, wordStorage);
		documentDatabase.setRemoteDatabase(remoteDatabase);
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
    public String health() { 
	   return "healthy";
    }

	@RequestMapping(value = "/runregex", method = RequestMethod.POST)
    public String runregex(@RequestBody String text, @RequestParam String granularity, @RequestParam String regex ,@RequestParam String language, @RequestParam String precondition,@RequestParam String postcondition) { 
	  String result="", matches="";	
	  Integer count=0;
	  RegexFeature regexFeature=null;
	  featureFunction.initialise();
	  Section section = new Section();
	  if (text.length()>0) {
		  text = General.decode(text);
		  text = text.substring(9,text.length());
		  text = text.substring(0,text.length()-2);
	  }
	  if (regex.length()>0) {
		  regex = General.decode(regex);
	  }
	  if (precondition.length()>0) {
		  precondition = General.decode(precondition);
	  }
	  result = remoteParser.parseTextToText(language, text);
	  section.fromJson(result);

	  regexFeature = new RegexFeature("test", "test", "test", regex, granularity, precondition, postcondition);
	  Matcher matcher = new Matcher(regexFeature, featureFunction, wordStorage, contractFunction);
	  try {
            count = matcher.matchcount(section);
            matches = "Regex matches:"+String.valueOf(count);
	  } catch (ParseRegexException regexException) {
		    matches = regexException.getMessage();
	  }		
      return matches;
    }

	@RequestMapping(value = "/regexmatches", method = RequestMethod.POST)
    public String regexmatches(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  RegexFeature regexFeature = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
	  String[] parts=null;	
	  Integer matchcount = 0;
	  JSONParser jsonParser=null;
	  JSONObject jsonObject=null;
	  List<String> matches=null;
	  List<WordToken> tokens=null;
	  Matcher matcher = null;
	  Object object = null;
	  featureFunction.initialise();
	  featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
	  Section section = new Section();
	  try {
         jsonParser = new JSONParser();
		 object = jsonParser.parse(body);
	     if (object != null) {
			 object = jsonParser.parse(body);
             jsonObject = (JSONObject)object;
			 text = (String) jsonObject.get("text");
             regex = (String) jsonObject.get("regex");
			 highlight = (String) jsonObject.get("highlight");
			 language = (String) jsonObject.get("language");
			 granularity = (String) jsonObject.get("granularity");
			 precondition = (String) jsonObject.get("precondition");
			 postcondition = (String) jsonObject.get("postcondition");
			 invariant = (String) jsonObject.get("invariant");

			 if ((text!=null) && (text.length()>0)) {
				 text = General.decode(text);
				 if (regex.length()>0) {
					 regex = General.decode(regex);
				 }
				 if (precondition.length()>0) {
					precondition = General.decode(precondition);
				 }
				 if ((highlight!=null) && (highlight.length()>0)) {
					 if (highlight.equalsIgnoreCase("on")) {
						 showHighlight = true;
					 }
				 }
				 if ((granularity!=null) && (granularity.length()>0)) {
					granularity = DEFAULT_GRANULARITY;
				 }
				 if ((language!=null) && (language.length()>0)) {
					language = DEFAULT_LANGUAGE;
				 }

				 result = remoteParser.parseTextToText(language, text); 
                 section.fromJson(result);
				 if ((regex!=null) && (regex.length()>0) && (text!=null) && (text.length()>0)) {
					 valid = true;
				 }
				 if (valid) {
					matches = null;
                    regexFeature = new RegexFeature(); 
					// regexFeature = new RegexFeature("test", "test", "test", regex, granularity, precondition, postcondition, invariant);
                    //contractFunction.setLanguage(language);
					matcher = new Matcher(regexFeature, featureFunction, wordStorage, contractFunction);
					if (showHighlight) {
					   matches = matcher.matchtext(section);
					} else {
						 matchcount = matcher.matchcount(section);
					}
	                section.toSingleSentence();
                    tokens = section.getCurrentSentence();
	                for (WordToken wordToken:tokens) {
		               tokensStr = tokensStr + wordToken.toJson()+",";
	                }
			        if (tokensStr.endsWith(",")) {
				       tokensStr = tokensStr.substring(0, tokensStr.length()-1);
			        }		 
                    tokensStr = "\"tokens\":"+"[" + tokensStr + "]";
	                for (String match:matches) {
                       parts = match.split(":");
		               entry = "{\"start\":\""+parts[0]+"\",\"end\":\""+parts[1]+"\"},";
		               matchesStr = matchesStr + entry;
	                }
	                if (matchesStr.length()>0) {
	                   matchesStr = matchesStr.substring(0,matchesStr.length()-1);
	                   matchesStr = "\"matches\":"+"[" + matchesStr + "]";
	                } else {
		               matchesStr = "\"matches\":"+"[" + "]";
	                }	   
	            result = "{"+tokensStr+","+matchesStr+"}";
		        }
			  }  
		    }
		} catch (Exception regexException) {
			result = "{\"error\":\""+regexException.getMessage()+"\"}";
		}	
      return result;
    }

	@RequestMapping(value = "/getmatches",  method = RequestMethod.POST)
    public String getMatches(@RequestBody String text, @RequestParam String language, @RequestParam(required=false) Integer minimumWordMatch, @RequestParam(required=false) String startsWith, @RequestParam(value="",required=false) String contains, @RequestParam Integer matchgap, @RequestParam Boolean removeRepeatedGroups) { 
	  Boolean finished=false, found=false;
	  String firsttext="";
	  String secondtext="";
	  featureFunction.initialise();
	  PartMatch lineMatch=null;
	  SentenceMatch sentenceMatch=null;
	  String id="", response="", firstId="", firstTextId="", secondTextId="", secondId="", strLine="", jsonStr="", reply="", content="", parsedText="";
	  Integer sentenceCountFirst=0, sentenceCountSecond=0, sentenceFirstIndex=0, sentenceSecondIndex=0, index=0;
	  JSONParser jsonParser=null;
	  JSONObject jsonObject=null;
	  JSONArray jsonArray=null;
	  List<WordToken> sentenceFirst=null, sentenceSecond=null;
	  List<String> idList = new ArrayList<>();
	  List<PartMatch> uniqueMatches = new ArrayList<>();
	  Map<String, Section> sectionMap = new HashMap<>();
	  List<List<PartMatch>> matches=null;
	  Object object=null;
	  Section section=null, sectionFirst=null, sectionSecond=null;
	  SentenceCompare sentenceCompare=null;
	  
	  if (text.length()>0) {
		  featureFunction.setFeatureStore(documentDatabase);
		  featureFunction.setWordStorage(wordStorage);
		  try {
			    jsonParser = new JSONParser();
				object = jsonParser.parse(text);
				if (object instanceof JSONObject) {
					jsonObject = (JSONObject)object;
					object = jsonObject.get("text");
                    if (object instanceof JSONArray) {
                        jsonArray = (JSONArray)object;
                        for (int i = 0; i <jsonArray.size(); i++) {
							object = jsonArray.get(i);
                            if (object instanceof JSONObject) {
                                jsonObject = (JSONObject)object;
                                id = (String)jsonObject.get("id");
                                content = (String) jsonObject.get("content");
								content = URLDecoder.decode(content);
								section = new Section();
								parsedText = remoteParser.parseTextToText(language, content);
								section.fromJson(parsedText);
								sectionMap.put(id, section);
								idList.add(id);
							}
						}
					}
				}
				if ((sectionMap.size()>0) && (idList.size()==2)) {		
					 firstTextId = idList.get(0);
					 secondTextId = idList.get(1);
					 sectionFirst = sectionMap.get(firstTextId);
					 sectionSecond = sectionMap.get(secondTextId);		
				     sentenceFirstIndex=0;
				     sentenceSecondIndex=0;
				     sentenceCountSecond=sectionSecond.getSentenceCount();
				     sentenceCountFirst=sectionFirst.getSentenceCount();
				     reply="[";
				     jsonStr="";
				     sentenceCompare = new SentenceCompare();
                     while (sentenceFirstIndex<sentenceCountFirst) {
					     sentenceFirst = sectionFirst.getSentenceAtIndex(sentenceFirstIndex);
						 sentenceSecondIndex=0;
					     while (sentenceSecondIndex<sentenceCountSecond) {
						     sentenceSecond = sectionSecond.getSentenceAtIndex(sentenceSecondIndex);
						     firstId=firstTextId+":"+String.valueOf(sentenceFirstIndex+1);
						     secondId=secondTextId+":"+String.valueOf(sentenceSecondIndex+1);
						     sentenceMatch = sentenceCompare.theSame(sentenceFirst,firstId,sentenceSecond,secondId,minimumWordMatch,startsWith, contains, matchgap, removeRepeatedGroups);
                             sentenceSecondIndex++;
						     if (sentenceMatch != null) {
							     matches = sentenceMatch.getMatches();
							    strLine="";
							    if (matches.size()>0) {
                                   for (List<PartMatch> matchList:matches) {
								 	strLine="";
									 for (PartMatch partMatch:matchList) {
										 strLine = strLine + partMatch.getMatch()+" ";
									 }
                                     if (removeRepeatedGroups) {
											 found = false;
											 index = 0;
											 while ((!found) && (index<uniqueMatches.size())) {
                                                  lineMatch = uniqueMatches.get(index);
												  if (lineMatch.theSame(strLine)) {
													  lineMatch.incrementCount();
													  found=true;
												  }
												index = index + 1;
											 }
											 if (!found) {
												 lineMatch = new PartMatch(sentenceFirstIndex, sentenceSecondIndex, strLine);
												 lineMatch.incrementCount();
												 uniqueMatches.add(lineMatch);
											 }
									} else {
											lineMatch = new PartMatch(sentenceFirstIndex, sentenceSecondIndex, strLine);
											lineMatch.incrementCount();
											uniqueMatches.add(lineMatch);
									}
							   }
						     } 
				           }
						}
					sentenceFirstIndex++;
				  }
				jsonStr="";
				for (PartMatch partMatch:uniqueMatches) {
					jsonStr = jsonStr + partMatch.toJson()+",";
				}
				if (jsonStr.length()>10) {
					jsonStr = jsonStr.substring(0,jsonStr.length()-1);
				}
				reply = reply + jsonStr;
			  }
		  } catch (Exception regexException) {
			   regexException.printStackTrace();
			   reply = "{\"error\":\""+regexException.getMessage()+"\"}";
		  }
	  }	 
	  reply = reply + "]"; 
      return reply;
    }
	
	@RequestMapping(value = "/runfeature", method = RequestMethod.GET)
    public String runfeature(@RequestParam String featurename, @RequestParam String text, @RequestParam String granularity, @RequestParam String language) { 
	  featureFunction.initialise();
	  FeatureDocument featureDocument=null;
	  RegexFeature regexFeature=null;
	  Matcher matcher=null;
	  String response="";
	  Integer matches=0;
	  Section section=new Section();
	  String featureRegex ="";
	  if (featurename.length()>0) {
		  featureFunction.setFeatureStore(documentDatabase);
		  featureFunction.setWordStorage(wordStorage);
		  featureDocument = documentDatabase.getDocumentByName("regex", featurename);
		  featureRegex = featureDocument.getContents();
	      response = remoteParser.parseTextToText(language, text);
	      section.fromJson(response);
		  regexFeature = new RegexFeature(featurename, "test", "test", featureRegex, granularity, "", "");
	      matcher = new Matcher(regexFeature, featureFunction, wordStorage, contractFunction);
		  try {
                matches = matcher.matchcount(section);
				response = "Feature Matches:" + String.valueOf(matches);
		  } catch (ParseRegexException regexException) {
			   response = "{\"error\":\""+regexException.getMessage()+"\"}";
		  }
	  }	  
      return response;
    }
	
    @RequestMapping(value = "/getruns", method = RequestMethod.GET)
    public String getruns() { 
	  featureFunction.initialise();
	  String response="";
	  response = remoteAnalyzer.getRuns();
	  try {
           response = URLEncoder.encode(response, "UTF-8");
	  } catch (Exception exception) {
		  exception.printStackTrace();
	  }
      return response;
    }

	@RequestMapping(value = "/modelresults", method = RequestMethod.GET)
    public String getModelResults(@RequestParam String runname, @RequestParam String modelname) { 
	  featureFunction.initialise();
	  String response="";
	  if ((runname!=null) && (runname.length()>0)) {
	      response = remoteAnalyzer.getResults(runname, modelname);
	  }	 
	  try {
           response = URLEncoder.encode(response, "UTF-8");
	  } catch (Exception exception) {
		  exception.printStackTrace();
	  } 
      return response;
    }

	@RequestMapping(value = "/addmodel", method = RequestMethod.GET)
    public String addmodel(@RequestParam String modelname, @RequestParam String description, @RequestParam String modelfilepath) { 
	  featureFunction.initialise();
	  String result="", contents="", runname="";
	  if ((modelname!=null) && (modelname.length()>0)) {
	      result = remoteAnalyzer.getResults(runname, modelname);
	  }	 
	  try {
		   contents="\"file\":\""+modelfilepath+"\"}";
           contents = URLEncoder.encode(contents, "UTF-8");
		   result = documentDatabase.addDocument(modelname, "model", contents, description);
	  } catch (Exception exception) {
		  exception.printStackTrace();
	  } 
      return result;
    }

	@PostMapping(value = "/confusionmatrix", produces = "application/json")
    public String confusionmatrix(@RequestBody String data) { 
	  String result="";
          try {
                Thread.sleep(600000);
          } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
          }
          result="{\"matrix\":\"found it\"}";
/*
	  if ((data!=null) && (data.length()>0)) {
	      result = remoteAnalyzer.confusionMatrix(data);
	  }
*/	 
      return result;
    }

	@PostMapping(value = "/buildclassifier", produces = "application/json")
    public Boolean buildclassifier(@RequestBody String model) { 
	  Boolean result=false;
	  if ((model!=null) && (model.length()>0)) {
	      result = remoteAnalyzer.buildClassifier(model);
	  }	 
      return result;
    }

	@PostMapping(value = "/buildclassifierfrommodel", produces = "application/json")
    public Boolean buildclassifierfrommodel(@RequestBody String model) { 
	  Boolean result=false;
	  if ((model!=null) && (model.length()>0)) {
	      result = remoteAnalyzer.buildClassifierFromModel(model);
	  }	 
      return result;
    }

	@PostMapping(value = "/classifydata", produces = "application/json")
    public String classifydata(@RequestBody String model) { 
	  String result="";
	  //if ((model!=null) && (model.length()>0)) {
	  //    result = remoteAnalyzer.classifydata(model);
	 // }	 
      return result;
    }

	@RequestMapping(value = "/getresults", method = RequestMethod.GET)
    public String getresults(@RequestParam String runid, @RequestParam String model) { 
	  featureFunction.initialise();
	  String response="";
	  if ((runid!=null) && (runid.length()>0)) {
	      response = remoteAnalyzer.getResults(runid, model);
	  }	 
	  try {
           response = URLEncoder.encode(response, "UTF-8");
	  } catch (Exception exception) {
		  exception.printStackTrace();
	  } 
      return response;
    }

	@RequestMapping(value = "/getmodels", method = RequestMethod.GET)
    public String getmodels() { 
	  featureFunction.initialise();
	  String response="";
	  response = remoteAnalyzer.getModels();
	  try {
           response = URLEncoder.encode(response, "UTF-8");
	  } catch (Exception exception) {
		  exception.printStackTrace();
	  }
      return response;
    }

	@RequestMapping(value = "/featureimport", method = RequestMethod.POST)
    public String featureimport(@RequestParam("file") MultipartFile file) throws InterruptedException {
        JSONParser parser = null;
        JSONArray jsonArray=null;
        JSONObject jsonObject=null;
        Object object=null;
        String response="Successfully uploaded file", line="", strBlock="", name="", regex="", type="regex", description="", contents="", precondition="", postcondition="", granularity="";
        BufferedReader bufferedReader=null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
             // Get the file and save it somewhere
             bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
             }
             bufferedReader.close();
             if (stringBuffer.length()>10) {
                  parser = new JSONParser();
                  strBlock = stringBuffer.toString();
                  object = parser.parse(strBlock);
                 if (object instanceof JSONArray) {
                    jsonArray = (JSONArray)object;
                    for (int i = 0; i <jsonArray.size(); i++) {
                            object = jsonArray.get(i);
                            if (object instanceof JSONObject) {
                                jsonObject = (JSONObject)object;
                                name = (String)jsonObject.get("featurename");
                                description = (String) jsonObject.get("description");
                                regex = (String) jsonObject.get("regex");
                                precondition = (String) jsonObject.get("precondition");
                                postcondition = (String) jsonObject.get("postcondition");
                                if (granularity==null) {
                                    granularity = "text";
                                }
                                contents = contents + "\"granularity\":\""+granularity+"\",";
                                if ((precondition!=null) && (precondition.length()>0)) {
                                     precondition = URLEncoder.encode(precondition);
                                } else {
                                     precondition="";
                                }
                                contents = contents + "\"precondition\":\""+precondition+"\",";
                                if ((postcondition!=null) && (postcondition.length()>0)) {
                                    postcondition = URLEncoder.encode(postcondition);
                                }  else {
                                    postcondition="";
                                }
                                 contents = contents + "\"postcondition\":\""+postcondition+"\",";
                                 if ((regex!=null) && (regex.length()>0)) {
                                     regex = URLEncoder.encode(regex);
                                 }
                                 contents = "{"+ contents + "\"regex\":\""+regex+"\"}";
                                 contents = URLEncoder.encode(contents);
                                 documentDatabase.addDocument(name, type, contents, description);
                             }
                    }
               }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }  
      return response;
    }

	@RequestMapping(value = "/runasyncgroupagainstdocument", method = RequestMethod.GET) 
    public String runasyncgroupagainstdocument(@RequestParam String runname, @RequestParam String description,  @RequestParam String language, @RequestParam String featuregroupname, @RequestParam String documentgroupname) throws InterruptedException { 
	  String path="", response="";
	  remoteBatch.setFeatureStore(documentDatabase);
      response = remoteBatch.runasyncgroupagainstdocument(runname, description, language, featuregroupname, documentgroupname);	 
      return response;
    }
	
	@RequestMapping(value = "/adddocument", method = RequestMethod.GET)
    public String adddocument(@RequestParam String documentname, @RequestParam String documenttype, @RequestParam String documentcontents, @RequestParam String documentdescription) { 
	  String response = "";
	  documentDatabase.addDocument(documentname, documenttype, documentcontents, documentdescription);
      return response;
    }
	
	@RequestMapping(value = "/getdocument", method = RequestMethod.GET)
    public FeatureDocument getdocument(@RequestParam String documentid) { 
		 FeatureDocument document = null;
		 document = documentDatabase.getDocumentById(documentid);
	     return document;
    }
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getdocuments", method = RequestMethod.GET)
    public List<FeatureDocument> getdocuments(@RequestParam String type) { 
		 List<FeatureDocument> documents = null;
		 documents = documentDatabase.getDocumentByType(type);
	     return documents;
    }
	
	@RequestMapping(value = "/updatedocument", method = RequestMethod.GET)
    public String updatedocument(@RequestParam String id, @RequestParam String name, @RequestParam String type, @RequestParam String contents, @RequestParam String description) { 
		 String reply = null;
		 reply = documentDatabase.updateDocument(Integer.valueOf(id), name, type, contents, description);
	     return reply;
    }
	
	@RequestMapping(value = "/deletedocument", method = RequestMethod.GET)
    public String deletedocument(@RequestParam String documentid) { 
		 String reply = null;
		 reply = documentDatabase.deleteDocument(documentid);
	     return reply;
    }
	
	@RequestMapping(value = "/parsetext", method = RequestMethod.GET)
    public String parsetext(@RequestParam String text, @RequestParam String language ) { 
	  String result="";
	  result = remoteParser.parseTextToText(language, text);	  
      return result;
    }

	@PostMapping(value = "/spellcorrection")
    public String spellcorrection(@RequestBody String text, @RequestParam String language) {
	  String result="";
	  String parsedText = "";
	  parsedText = this.parsetext(text, language);
	  result = remoteSpellChecker.checkText(parsedText, language);
      return result;
    }
	
	@RequestMapping(value = "/featurelist", method = RequestMethod.GET)
    public String featurelist(@RequestParam String type) {
	  String result="";
	  List<FeatureDocument> featureDocumentList = documentDatabase.getDocumentByType("regex");	
	  for (FeatureDocument featureDocument:featureDocumentList) {
           result = result + featureDocument.toString() + ",";
	  }
	  result = result.substring(0,result.length()-1);
	  result = "[" + result + "]";
      return result;
    }
	
	@RequestMapping(value = "/documentation", method = RequestMethod.GET)
    public String documentation() { 
	  String docs = documentation.getDocumentation(applicationContext);
      return docs;
    }
	
}