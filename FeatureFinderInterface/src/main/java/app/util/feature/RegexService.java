package app.util.feature;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.text.DateFormat;   
import java.util.Date; 

import java.net.URLEncoder;

import java.util.concurrent.TimeUnit;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.core.io.Resource;

import app.util.database.DocumentDatabase;
import app.util.feature.FeatureDocument;
import app.util.feature.FeatureResult;
import app.util.feature.Match;
import app.util.feature.RegexDocument;
import app.util.feature.RegexDocumentList;
import app.util.feature.TextDocument;
 
@CrossOrigin
@RestController
public class RegexService { 
	private static String RESULTS_LOCATION="datalocation";
	private static String STORAGE_LOCATION="datalocation";
	private static String DEFAULT_LANGUAGE="1";
	private static String[] LANGUAGE_LIST={"","english","chinese","russian","pinyin"};
	private static String DEFAULT_GRANULARITY="text";
	private static String PROPERTIES_NAME="server.properties";
	private static final Logger logger=LoggerFactory.getLogger(RegexService.class);
	private ContractFunction contractFunction;
	private FeatureFunction featureFunction;
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;
	private String dataLocation;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RemoteAnalyzer remoteAnalyzer;
	@Autowired
	private RemoteParser remoteParser;
	@Autowired
	private RemoteProcessor remoteProcessor;
	@Autowired
	private RemoteIngestor remoteIngestor;
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
		dataLocation = serviceLocator.getService(STORAGE_LOCATION);
		
		remoteParser.setRestTemplate(restTemplate);
		remoteParser.setServiceLocator(serviceLocator);

		remoteAnalyzer.setRestTemplate(restTemplate);
		remoteAnalyzer.setServiceLocator(serviceLocator);

        remoteProcessor.setServiceLocator(serviceLocator);
		remoteProcessor.setRestTemplate(restTemplate);

		remoteDatabase.setRestTemplate(restTemplate);
		remoteDatabase.setServiceLocator(serviceLocator);
		contractFunction = new ContractFunction(featureFunction, wordStorage);
		documentDatabase.setRemoteDatabase(remoteDatabase);

		remoteIngestor.setServiceLocator(serviceLocator);
		remoteIngestor.setDatabase(documentDatabase);
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
    public String health() { 
	   return "healthy";
    }

	@RequestMapping(value = "/regexmatches", method = RequestMethod.POST)
    public String regexmatches(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  FeatureResult featureResult = null;
	  List<RegexDocument> regexFeatureList = new ArrayList<>();
	  TextDocument textDocument = null;
	  RegexDocument regexDocument = null;
	  RegexDocumentList regexDocumentList = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
	  String[] parts=null;	
	  Integer matchcount = 0, languageId = 0;
	  JSONParser jsonParser=null;
	  JSONObject jsonObject=null;
	  List<Match> matches=null;
	  List<WordToken> tokens=null;
	  Matcher matcher = null;
	  Object object = null;
	  featureFunction.initialise();
	  featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
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
				 if ((granularity==null) || (granularity.length()==0)) {
					granularity = DEFAULT_GRANULARITY;
				 }
				 if ((language==null) || (language.length()==0)) {
					language = DEFAULT_LANGUAGE;
				 } 
				 languageId = Integer.valueOf(language);
				 language = LANGUAGE_LIST[languageId];
				

				 if ((regex!=null) && (regex.length()>0) && (text!=null) && (text.length()>0)) {
					 valid = true;
				 }
				 if (valid) {
					matches = null;
					
                    regexDocument = new RegexDocument("experiment", "none", "none", "", granularity, precondition, postcondition, invariant);
					regexDocument.setRegex(regex);
					regexFeatureList = new ArrayList<>();
					regexFeatureList.add(regexDocument);
					regexDocumentList = new RegexDocumentList();
					regexDocumentList.setRegexDocumentList(regexFeatureList);
					regexDocumentList.setMessageType("add");

					result = remoteProcessor.processFeature(regexDocumentList, "experiment");
					result = remoteProcessor.processText(text, language, "experiment");
		        }
			  }  
		    }
		} catch (Exception regexException) {
			result = "{\"error\":\""+regexException.getMessage()+"\"}";
		}	
      return result;
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

	@RequestMapping(value = "/modelresults", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
	public ResponseEntity<String> getModelResults(@RequestParam String token, @RequestParam String runname, @RequestParam String model) {
	  featureFunction.initialise();
	  String response="";
	  PrintWriter printWriter = null;
	  StringWriter stringWriter = new StringWriter();
	  if ((token!=null) && (token.length()>0)) {
	      response = remoteAnalyzer.getResults(token, runname, model);
	      try {
			      printWriter = new PrintWriter(stringWriter);
			      printWriter.println(response);
				  printWriter.flush();
		   } catch (Exception exception) {//
			    response = "{\"error\":\"Failed to build results file:"+token+".dat\"}";
 		   }
     
	 }
     return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+token+".csv\"")
                .body(stringWriter.toString());
    }

	@RequestMapping(value = "/addmodel", method = RequestMethod.GET)
    public String addmodel(@RequestParam String modelname, @RequestParam String description, @RequestParam String modelfilepath) { 
	  featureFunction.initialise();
	  Document document = null;
	  String result="", contents="", runname=""; 
	  try {
		   document = new Document();
		   contents="\"file\":\""+modelfilepath+"\"}";
           contents = URLEncoder.encode(contents, "UTF-8");
		   document.setContents(contents);
		   document.setDescription(description);
		   document.setType("model");
		   document.setName(modelname);
		   result = documentDatabase.addDocument(document);
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
	      response = remoteAnalyzer.getResultsById(runid, model);
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
		Document document = null;
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
								 document = new Document();
								 document.setType(type);
								 document.setDescription(description);
								 document.setContents(contents);
								 document.setName(name);
                                 documentDatabase.addDocument(document);
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
    public String runasyncgroupagainstdocument(@RequestParam String runname, @RequestParam String description,  @RequestParam String language, @RequestParam String featuregroupname, @RequestParam String documentgroupname, @RequestParam String outputlocation) throws InterruptedException { 
	  String path="", response="";
      response = remoteIngestor.runasyncgroupagainstdocument(runname, description, language, featuregroupname, documentgroupname, outputlocation);	 
      return response;
    }
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/adddocument", method = RequestMethod.POST)
    public String adddocument(@RequestBody Document document) { 
       String response = "", language = "", type = "";
	   Integer languageId = Integer.valueOf(DEFAULT_LANGUAGE);
	   try {
			 type = document.getType();
			 if (type.equalsIgnoreCase("list")) {
				 language = LANGUAGE_LIST[languageId];
				 remoteParser.addList(language, document);
			 } else {
			     documentDatabase.addDocument(document);
			 }
			 response = "{\"message\":\"new document has been added\"}";
	   } catch (Exception exception) {
		  exception.printStackTrace();
		  response = "{\"error\":\""+exception.getMessage()+"\"}";
	   }
       return response;
    }

  @PostMapping(value = "/uploaddatazip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
	Boolean saved = null;
	Integer dotLocation = 0, position = 0;
    String message = "Successfully unpacked zip file";
	String destDir = "", filename = "", filelocation = "", filePath ="", entryName = "";
	File directory = null, zipFile = null, zipLocation = null;
	ZipInputStream zipStream = null;
	ZipEntry zipEntry = null;
    try {
		  filename = file.getOriginalFilename();
		  dotLocation = filename.indexOf(".");
		  if (dotLocation>0) {
             filelocation = filename.substring(0, dotLocation);
		  }
          filelocation = dataLocation + File.separator + filelocation;
          directory = new File(String.valueOf(filelocation));
          if (!directory.exists()) {
             directory.mkdirs();
          }
          zipStream = new ZipInputStream(file.getInputStream());
          zipEntry = zipStream.getNextEntry();
        // iterates over entries in the zip file
          while (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                // if the entry is a file, extracts it
				filePath = directory + File.separator + zipEntry.getName();
			    entryName = zipEntry.getName();
			    position = entryName.lastIndexOf(File.separator);
			    if (position>0) {
                   entryName = entryName.substring(0, position);
			       destDir = directory + File.separator + entryName;
			       zipLocation = new File(destDir);
			       if (!zipLocation.exists()) {
				      zipLocation.mkdirs();
			       }
			    }
                extractFile(zipStream, filePath);
            } 
            zipStream.closeEntry();
            zipEntry = zipStream.getNextEntry();
          }
          zipStream.close();
	  } catch (Exception exception) {
		  message="Failed to extract zip file";
	  }
	  return ResponseEntity.ok().body(message);
    }
	
	@RequestMapping(value = "/getdocument", method = RequestMethod.GET)
    public Document getdocument(@RequestParam String documentid) { 
		 Document document = null;
		 document = documentDatabase.getDocumentById(documentid);
	     return document;
    }

	@RequestMapping(value = "/getlistdocument", method = RequestMethod.GET)
    public Document getlistdocument(@RequestParam String documentid) { 
		 Document document = null;
		 String language = "";
		 Integer languageId = Integer.valueOf(DEFAULT_LANGUAGE);
		 language = LANGUAGE_LIST[languageId];
         document = remoteParser.getList(language, documentid);
	     return document;
    }

	@RequestMapping(value = "/getregexdocument", method = RequestMethod.GET)
    public RegexDocument getregexdocument(@RequestParam String documentid) { 
		 Document document = null;
		 RegexDocument regexDocument = new RegexDocument();
		 document = documentDatabase.getDocumentById(documentid);
		 if (document!=null) {
            regexDocument.fromDocument(document);
		 }
	     return regexDocument;
    }
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getdocuments", method = RequestMethod.GET)
    public List<Document> getdocuments(@RequestParam String type) { 
		 Integer languageId=Integer.valueOf(DEFAULT_LANGUAGE);
		 List<Document> documents = null;
		 List<Document> allDocuments = new ArrayList<>();
		 List<String> typeList = new ArrayList<>();
		 Document tempDocument = null;
		 String someType="", function="", language = "";
		 String[] allTypes = type.split(","), parts = null;
	     String[] functionList = FeatureFunction.FUNCTION_FEATURES;
		 if (allTypes.length>0) {
			for (int i=0; i<allTypes.length; i++) {
				someType = allTypes[i];
				if (!typeList.contains(someType)) {
					typeList.add(someType);
					if (someType.equalsIgnoreCase("list")) {
				        language = LANGUAGE_LIST[languageId];
						documents = remoteParser.getDocuments(language, someType);
					} else if (someType.equalsIgnoreCase("function")) {
						      documents = new ArrayList<>();
					          for (int j=0; j<functionList.length; j++) {
			                       function = functionList[j];
			                       parts = function.split(":");
                                   tempDocument = new Document(null, parts[0], "function", parts[3], parts[1]);
                                   documents.add(tempDocument);
			                  }
					} else {
		                documents = documentDatabase.getDocumentByType(someType);
					}
					if (documents!=null) {
				        for (Document document:documents) {
					        allDocuments.add(document);
				        }
					}
				    if (someType.equalsIgnoreCase("model")) {
					    tempDocument = new Document("", "none", "model", "", "");
					    allDocuments.add(tempDocument);
                        tempDocument = new Document("", "native_english", "model", "", "");
					    allDocuments.add(tempDocument);
				    }
				}
			}
		 }
	     return allDocuments;
    }
	
	@RequestMapping(value = "/updatedocument", method = RequestMethod.GET)
    public String updatedocument(@RequestParam String id, @RequestParam String name, @RequestParam String type, @RequestParam String contents, @RequestParam String description) { 
		 String reply = null;
		 Document document = new Document(id, name, type, contents, description);
		 reply = documentDatabase.updateDocument(document);
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
	  List<Document> documentList = documentDatabase.getDocumentByType("regex");	
	  for (Document document:documentList) {
           result = result + document.toJson() + ",";
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


    private void extractFile(ZipInputStream zipIn, String filePath) {
		Integer BUFFER_SIZE = 4096;
        BufferedOutputStream bos = null;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
		try {
			 bos = new BufferedOutputStream(new FileOutputStream(filePath));
             while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
             }
             bos.close();
		} catch (Exception exception) {
			  exception.printStackTrace();
		}
    }

}