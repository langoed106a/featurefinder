package app.util.feature; 

import java.lang.NumberFormatException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class FeatureFunction {
	private static final Logger logger = LoggerFactory.getLogger(FeatureFunction.class);
	public static String[] FUNCTION_FEATURES= {"active:identifies whether a word is an active verb:function:none:function",
		                                       "actionverb:identifies whether a word is an action verb:function:none:function",
						                       "anycase:alters text so that its case insensitive:function:none:function",
										       "anyword:represents any generic word:function:none:function",
											   "anything:represents any generic word, symbol etc:function:none:function",
											   "badconsonant:identifies a mispelt word that uses an incorrect consonant:function:none:function",
											   "conjunction:identifies whether a phrase is a conjunction:function:none:function",
											   "causative:identifies whether a phrase is a causative:function:none:function",
											   "coordinatingconjunction:identifies whether a phrase is a coordinating conjunction:function:none:function",
											   "contains:identifies whether one or more words are present:function:none:function",
											   "distinctword:identifies whether a word is distinct:function:none:function",
											   "endswith:identifies whether a word ends with a known sequence of letters:function:none:function",
											   "existsbefore:identifies whether a word ends exists before the current word:function:none:function",
											   "existsafter:identifies whether a word exists after the current word:function:none:function",
											   "futuretense:identifies whether a phrase is in the future tense:function:none:function",
											   "gerund:identifies whether a word is a gerund:function:none:function",
											   "intransitiveverb:identifies whether a word is an intransitive verb:function:none:function",
											   "irregularverb:identifies whether a word is an irregular word:function:none:function",
											   "emoji:identifies whether a word is an emoji:none:function",
											   "lengthmorethan:identifies whether the number of characters is more than the given parameter:function:none:function",
											   "manyword:skips over many words until it finds its match:function:none:function",
											   "misspeltdoublevowel:identifies whether a word has a double vowel by accident:function:none:function",
											   "nan:identifies whether a word is not a number:function:none:function",
											   "notcontains:identifies whether one or more words are not present:function:none:function",
											   "notendswith:identifies whether a word doesn't end with the given part of a word:function:none:function",
											   "notexistsbefore:identifies whether a word doesn't exist before the current word:function:none:function",
											   "notexistsafter:identifies whether a word doesn't exist after the current word:function:none:function",
											   "notprevious:identifies whether a word is nor preceded by another word:function:none:function",
											   "notstartswith:identifies whether a word doesn't start with a given partial word:function:none:function",
											   "notmorethan:identifies whether the text has more words ($token) or sentences ($sentence) than a number specified:function:none:function",
											   "next:identifies whether a word is followed by another word:function:none:function",
											   "notnext:identifies whether a word is not followd by another word:function:none:function",
											   "notin:identifies whether a word is not in a given list:function:none:function",
											   "notsymbol:identifies whether a word is not a symbol:function:none:function",
											   "notlowercase:identifies whether a word is not in strict lowercase:function:none:function",
											   "numbertokens:checks whether text contains a certain amount of words '=?', '>?', '<?':function:none:function",
											   "object:identifies whether a word is classed as an object:function:none:function",
											   "perfectpasttense:identifies whether a phrase is in the perfect past tense:function:none:function",
											   "pastparticiple:identifies whether a word is a past participle:function:none:function",
											   "positionbefore:identifies whether a word(s) is at a certain location in a sentence:function:none:function",
											   "positionafter:identifies whether a word(s) is at a certain location in a sentence:function:none:function",
											   "previous:identifies whether a phrase is a preposition:function:none:function",
											   "preposition:identifies whether a phrase is a preposition:function:none:function",
											   "punctuation:identifies whether a token is a punctuation:function:none:function",
											   "passive:identifies whether a phrase is a passive phrase:function:none:function",
											   "regex:function which takes in a conventional regular expression:function:none:function",
											   "spacingleft:checks whether a word is prefixed by a parameter of spaces or 'blank':function:none:function",
											   "sentencecount:returns the nunmber of sentences in a text':function:none:function",
											   "subject:identifies whether a word can be classed as a subject:function:none:function",
											   "symbol:identifies whether a token is a symbol or a token is contained in a parameter like ',$%':function:none:function",
											   "startswith:identifies whether a word starts with a known sequence of letters:function:none:function",
											   "transitiveverb:identifies whether a word is a transitive verb:function:none:function",
											   "validwithoutprefix:identifies whether a word is still a known word with any prefix removed:function:none:function",
											   "validwithprefix:identifies whether a word is still a known word when a prefix is added:function:none:function",
											   "validafterreplace:identifies whether a word is still a known word when text is replaced:function:none:function",
											   "verblinked:identifies whether a word islinked to a verb:function:none:function",
											   "verb:identifies whether a word is a verb:function:none:function",
											   "wordsinasentence:identifies whether the number of words in a sentence is below or greater than a given value:function:none:function",
											   "wordslice:removes a portion of a word and check whether the remaining is aknown token,postag or lemma:function:none:function"};

	private FeatureFunctionList featureFunctionList;

	public FeatureFunction() {
		featureFunctionList = new FeatureFunctionList();
	}

	public void initialise() {
		 featureFunctionList.initialise();
	}

	public void setFeatureStore(DocumentStore documentStore) {
		featureFunctionList.setDocumentStore(documentStore);
	}

	public void setWordStorage(WordStorage wordStorage) {
		featureFunctionList.setWordStorage(wordStorage);
	}

	public Document getPredefinedRegex(String name) {
		Document document;
		document = featureFunctionList.getPredefinedRegex(name);
		return document;
	}

	public List<String> getPredefinedList(String name) {
		List<String> alist;
		alist = featureFunctionList.getPredefinedList(name);
		return alist;
	}

	public Boolean isPredefinedRegex(String name) {
		Boolean exists = false;
		exists = featureFunctionList.isPredefinedRegex(name);
		return exists;
	}

	public Boolean isPredefinedList(String name) {
		Boolean exists = false;
		exists = featureFunctionList.isPredefinedList(name);
		return exists;
	}

	public Boolean doFunction(String part, String name, String value, WordToken wordToken, TextDocument textDocument, FunctionCallback functionCallback) {
		Boolean found = false;
		Class<?> classObj = null;
		String content = "", description="";
		String[] parts = null;
		Integer sentence = 0;
		List<String> parameters = null;
		Method functionMethod=null;
		
		if (name.equalsIgnoreCase("symbol")) {
			  parameters = General.getSingleParameter(name, value);
		} else {
		      parameters = General.getParameters(name, value);
		}	  
		if (functionExists(name)) {
			classObj = this.getClass();
			try {
				 featureFunctionList.setCallbackHandler(functionCallback);
			     functionMethod = FeatureFunctionList.class.getMethod(name, String.class, WordToken.class, TextDocument.class, List.class);
				 found = (Boolean)functionMethod.invoke(featureFunctionList, part, wordToken, textDocument, parameters); 
			}
			catch (Exception exception) {
				logger.error("Unable to call function:"+name);
			}
		} else {
			found = featureFunctionList.checkExists(part, name, wordToken, textDocument, parameters);
		}
	   return found;
	}

	private Boolean functionExists(String name) {
		Boolean found=false;
		Integer index=0;
		String functionName="";
		while ((!found) && (index<FUNCTION_FEATURES.length)) {
			  functionName = FUNCTION_FEATURES[index];
			  if (functionName.startsWith(name)) {
				  found = true;
			  }
			  index++;
		}
		return found;
	}

}

