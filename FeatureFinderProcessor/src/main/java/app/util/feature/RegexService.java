package app.util.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import app.util.feature.Matcher;
import app.util.feature.RegexDocument;
import app.util.feature.RegexResult;
import app.util.feature.FeatureFunction;
import app.util.feature.HTTPAsyncSender;
import app.util.feature.ServiceLocator;
import app.util.feature.WordStorage;
import app.util.feature.ContractFunction;

import app.util.database.DocumentDatabase;

public class RegexService {
    Logger logger = LoggerFactory.getLogger(RegexService.class);
    DocumentDatabase documentDatabase;

    private HTTPAsyncSender asyncSender;

    public RegexService(ServiceLocator serviceLocator, DocumentDatabase documentDatabase) {
        asyncSender = new HTTPAsyncSender(serviceLocator);
        this.documentDatabase = documentDatabase;
    }

    @Async("taskExecutor")
    public CompletableFuture<String> doAsyncRegex(TextDocument textDocument, RegexDocument regexDocument, FeatureFunction featureFunction, WordStorage wordStorage, ContractFunction contractFunction) throws InterruptedException {
        Document runDocument = null, outputDocument = null;
        Matcher matcher = null;
        Map<String, String> params = null;
        RegexResult regexResult = new RegexResult();
        Integer matchcount = 0;
        String jsonStr="", response="", param1="", param2="";
        logger.info("Starting: regex-"+regexDocument.getRegex());
        matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
        try {
             params = new HashMap<>();
             runDocument = this.documentDatabase.getDocumentByName(regexDocument.getId());
             outputDocument = this.documentDatabase.getDocumentByName(runDocument.getOrigin());
		     matchcount = matcher.matchcount(textDocument);
             regexResult.setCount(matchcount);
             regexResult.setRegexName(regexDocument.getName());
             regexResult.setTextName(textDocument.getName());
             try {
                  param1=outputDocument.getContents();
                  param2=runDocument.getContents();
                  param1 = URLDecoder.decode(param1);
             } catch (Exception exception) {
                exception.printStackTrace();
             }
             params.put("param1", param1);
             params.put("param2", param2);
             jsonStr = regexResult.toJson();
             response = asyncSender.sendpost("regexresult", jsonStr, params);
        } catch(Exception exception) {
            exception.printStackTrace();
            logger.error("Error: async regex error");
        }		
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public  CompletableFuture<FeatureResult> doSyncRegex(TextDocument textDocument, RegexDocument regexDocument, FeatureFunction featureFunction, WordStorage wordStorage, ContractFunction contractFunction) throws InterruptedException {
        List<String> matches=null;
        Match match = null;
        Matcher matcher = null;
        FeatureResult featureResult = new FeatureResult();
        Integer matchcount = 0;
        List<Match> matchList = null;
        logger.info("Starting: regex-"+regexDocument.getRegex());
		matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
		try {
             matches = matcher.matchtext(textDocument);
             if (matches!=null) {
                matchList = new ArrayList<>();
                for (String point:matches) {
                    match = new Match();
                    match.setMatch(point);
                    matchList.add(match);
                }
                featureResult.setMatches(matchList);
                featureResult.setSentenceList(textDocument.getSentenceList());
             }
        } catch(Exception exception) {
            logger.error("Error: async regex error");
            exception.printStackTrace();
        }	
        return CompletableFuture.completedFuture(featureResult);
    }
}