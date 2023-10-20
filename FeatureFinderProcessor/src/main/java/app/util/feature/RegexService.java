package app.util.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import app.util.feature.Matcher;
import app.util.feature.RegexDocument;
import app.util.feature.FeatureFunction;
import app.util.feature.WordStorage;
import app.util.feature.ContractFunction;

@Service
public class RegexService {

    Logger logger = LoggerFactory.getLogger(RegexService.class);

    @Async
    public CompletableFuture<Integer> doAsyncRegex(TextDocument textDocument, RegexDocument regexDocument, FeatureFunction featureFunction, WordStorage wordStorage, ContractFunction contractFunction) throws InterruptedException {
        Matcher matcher = null;
        Integer matchcount = 0;
        logger.info("Starting: regex");
        matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
        try {
		     matchcount = matcher.matchcount(textDocument);
        } catch(Exception exception) {
            logger.error("Error: async regex error");
        }		
        return CompletableFuture.completedFuture(matchcount);
    }

    @Async
    public  CompletableFuture<List<String>> doSyncRegex(TextDocument textDocument, RegexDocument regexDocument, FeatureFunction featureFunction, WordStorage wordStorage, ContractFunction contractFunction) throws InterruptedException {
        List<String> matches=null;
        Matcher matcher = null;
        Integer matchcount = 0;
        logger.info("Starting: regex");
        matches = null;
		matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
		try {
             matches = matcher.matchtext(textDocument);
         } catch(Exception exception) {
            logger.error("Error: async regex error");
        }	
        return CompletableFuture.completedFuture(matches);
    }
}