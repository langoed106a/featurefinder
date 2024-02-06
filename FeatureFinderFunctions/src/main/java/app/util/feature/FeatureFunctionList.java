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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

public class FeatureFunctionList {
	private FunctionCallback functionCallback;
	private WordStorage wordStorage;
	private DocumentStore documentStore;
	private List<String> definedRegexList;
	private Integer wordCount;

	public FeatureFunctionList() {
		String[] parts=null;
		String item="";
		Integer count=0;
		definedRegexList=new ArrayList<>();
	}
	public void initialise() {
		String[] parts=null;
		String item="";
		Integer count=0;
		definedRegexList=new ArrayList<>();
	}

	public void setDocumentStore(DocumentStore documentStore) {
		this.documentStore = documentStore;
	}

	public void setCallbackHandler(FunctionCallback functionCallback) {
		this.functionCallback = functionCallback;
	}
	
	public Document getPredefinedRegex(String name) {
		Document featureDocument=null;
		featureDocument = documentStore.getDocumentByName(name);
		return featureDocument;
	}

	public List<String> getPredefinedList(String name) {
		List<String> list = null;
		list = wordStorage.getList(name);
		return list;
	}

	public Boolean isPredefinedRegex(String name) {
		Boolean exists = false;
		Document document = null;
		document = this.getPredefinedRegex(name);
		if ((document != null) && (document.getType().equalsIgnoreCase("regex"))) {
           exists = true;
		}
		return exists;
	}

	public Boolean isPredefinedList(String name) {
		Boolean exists = false;
		exists = this.getWordStorage().listExists(name);
		return exists;
	}

	public void setWordStorage(WordStorage wordStorage) {
		this.wordStorage = wordStorage;
	}

    public WordStorage getWordStorage() {
		return wordStorage;
	}

    public Boolean checkExists(String name, String part, WordToken wordToken, TextDocument textDocument, List<String> parameters)  {
		Boolean found = false;
		Integer sentence = null;
		String description="";
		String[] parts=null;
	    if (wordStorage.listExists(name)) {
		    if (wordStorage.wordExists(name, wordToken.getToken())) {
			    found = true;
		    }
	    } 
	  return found;
    }
	
	public boolean actionverb(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false;
		String dependency="";
		dependency = wordToken.getDependency();
		if ((dependency!=null) && (dependency.equalsIgnoreCase("action"))) {
			found = true;
		}
		      return found; 
	}
	public boolean adjective(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false;
		String content = General.getValue(part, wordToken);
		return found; 
	}
	public boolean anycase(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false;
		Integer index = 0;
		String currentWord = "", param="";
		if ((parameters.size()>0) && (wordToken != null)) {
			currentWord = wordToken.getToken();
			while ((!found) && (index<parameters.size())) {
				param = parameters.get(index);
				if (param.equalsIgnoreCase(currentWord)) {
					found = true;
				}
				 index++; 
			}
		}
		      return found; 
	}

    public boolean badconsonant(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
        boolean found = false;
		String word = wordToken.getToken(), temp = "", goodWord = "", consonant = "";
		String[] parts = null;
		String[] badconsonants={"dly","scr","shr","str","thr","ght","mpl"};
		Integer charIndex = 0, index = 0;
		if (!wordStorage.wordExists("commonword", word)) {
			temp = word;
            temp = temp.replaceAll("[AEIOUaeiou]","a");
            parts = temp.split("a");
            for (String partWord:parts) {
				index=0;
			    while ((index<badconsonants.length) && (!found)) {
                    consonant = badconsonants[index];
					charIndex = consonant.indexOf(partWord);
					if ((charIndex>0) && (partWord.length()<consonant.length())) {
                        goodWord = word.replace(partWord, consonant);
						if (wordStorage.wordExists("commonword", goodWord)) {
							found = true;
						}
					}
				    index++;
				}
			}
		}
        return found;
    }

	public boolean emoji(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
        boolean found = false;
		String word = wordToken.getToken();
		int length = word.length(), type=0;

        for (int i = 0; i < length; i++) {
           type = Character.getType(word.charAt(i));
           if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
               found = true;
            }
        }
      return found;
     }

	public boolean contains(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, match = false, dependency=false;
		String previousItem = "", param = "", token = "";
		Integer sentenceIndex=0,wordIndex = wordToken.getIndex(), paramIndex=0, matchCount=0;
		Integer paramsIndex = 0;
		List<WordToken> sentence = null;
		WordToken nextToken = null;
		if ((params.size() > 0) && (wordIndex==0)) {
			sentenceIndex = wordToken.getSentence();
			sentence = textDocument.getSentenceAtIndex(sentenceIndex);
			while ((paramIndex<params.size()) && (!found)) {
				param = params.get(paramIndex);
				if ((param.equalsIgnoreCase("$subject")) || (param.equalsIgnoreCase("$object")) || (param.equalsIgnoreCase("$actionverb"))) {
					param=param.substring(1,param.length());
					wordIndex=0;
					while ((wordIndex<sentence.size()) && (!match)) {
				         nextToken=General.getWordToken(wordIndex, sentence);
				         match = General.theSame("dependency", nextToken, sentence, param);  
						 if (match) {
							 found = true;
						  }
						  wordIndex++;
					}      
				} else {
				        wordIndex=0;
						while ((wordIndex<sentence.size()) && (!match)) {
				               nextToken=General.getWordToken(wordIndex, sentence);
							  
				               match = General.theSame(part, nextToken, sentence, param);  
						       if (match) {
							        found = true;
						       }
							   wordIndex++;
						}      
				}		       
				paramIndex++; 
			}    
		}
		return found;
	}
	public boolean verb(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		String content = General.getValue(part, wordToken);
		Boolean found = false;
		return found; 
	}
	public boolean doublevowel(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found=false;
		Integer count=0, times=0, letterIndex=0, paraIndex=0, index=0;
		List<String> tempList=null;
		String content = General.getValue(part, wordToken), vowelStr="", letter="", newWord="", tempStr="", partWord1="", partWord2="";
		WordToken token = null;
		if ((parameters.size()>0) && (part.equalsIgnoreCase("token"))) {
			if (!(this.isPredefinedList(content))) {
				count = 0;
				times = 0;

				while ((count<parameters.size()) && (!found)) {
					   vowelStr = parameters.get(count);
					      times = countOccurrencesOf(content, vowelStr);
					         if (times>=2) {
							     letterIndex = content.indexOf(vowelStr);
							     partWord1 = content.substring(0, letterIndex);
								 partWord2 = content.substring(letterIndex+1, content.length());
								 while ((!found) && (paraIndex<parameters.size())) {
									letter = parameters.get(paraIndex);
									tempStr = letter + vowelStr;
									if ((!letter.equalsIgnoreCase(vowelStr)) && (!tempStr.equalsIgnoreCase("ei")) && (!tempStr.equalsIgnoreCase("ei")))  {
										newWord = partWord1 + letter + partWord2;
										token = new WordToken(newWord, "", "", "", 0, 0);
										//if (this.isPreDefinedList(part, wordToken, textDocument, "commonword")) {
									    //		found = true;
									    //}
									}
									paraIndex++;
								}
								if (!found) {
									partWord1 = partWord1 + vowelStr;
									index = partWord2.indexOf(vowelStr);
									if (index>=0) {
										partWord1 = partWord1 + partWord2.substring(0,index);
										partWord2 = partWord2.substring(index+1, partWord2.length());
										paraIndex = 0;
										while ((!found) && (paraIndex<parameters.size())) {
											letter = parameters.get(paraIndex);
											tempStr = letter + vowelStr;
											if ((!letter.equalsIgnoreCase(vowelStr)) && (!tempStr.equalsIgnoreCase("ei")) && (!tempStr.equalsIgnoreCase("ei")))  {
												newWord = partWord1 + letter + partWord2;
												token = new WordToken(newWord, "", "", "", 0, 0);
												//if (this.checkPreDefinedList(part, wordToken, textDocument, "commonword")) {
												//	found = true;
												//}
											}
											paraIndex++;
										}
									}
								}
							}
						count++; 
				}
			}
		}
		return found; 
	 }
	 
	public Boolean distinctword(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false, finish = false;
		Integer sentenceNumber=0, wordIndex=0, index=0;
		List<WordToken> sentence;
		String contents="", value="", wordValue="";
		WordToken wordItem=null;
		if (wordToken!=null) {
			contents = General.getValue(part, wordToken);
			wordIndex = wordToken.getIndex();
			sentenceNumber = wordToken.getSentence();
			sentence = textDocument.getSentenceAtIndex(sentenceNumber);
			while ((!finish) && (index<sentence.size())) {
				wordItem = sentence.get(index);
				if (wordItem.getIndex()!=wordIndex) {
					wordValue = General.getValue(part, wordItem);
					if (wordValue.equalsIgnoreCase(contents)) {
						finish = true;
					}
				}
				index = index + 1;
			}
		}
		  found = !finish;
		    return found; 
	}

	public boolean lengthmorethan(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		String content = "", param="";
		Integer wordIndex = wordToken.getIndex(), length=0;
		Boolean found = false;
		if ((parameters!=null) && (parameters.size()==1)) {
			 param = parameters.get(0);
			 content = General.getValue(part, wordToken);
			 try {
				   length=Integer.parseInt(param);
				   if ((content!=null) && (content.length()>length)) {
					   found = true;
				   }	   
			} catch (NumberFormatException ex){
				return false;
			}
		}
	  return found; 
	}

	public boolean startswith(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String content = "";
		content = General.getValue(part, wordToken);
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0)) {
			for (String param : params) {
				param = General.removeQuotes(param);
				if (content.startsWith(param)) {
					found = true;
				}
			}
		}
		return found;
	}

	public boolean notstartswith(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, exists = false;
		Integer index = 0;
		String content = "", param="";
		content = General.getValue(part, wordToken);
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0) && (params!=null)) {
			while ((index<params.size()) && (!exists)) {
				param = params.get(index);
				param = General.removeQuotes(param);
				if (content.startsWith(param)) {
					exists = true;
				}
				index++;
			}
			if (!exists) {
				found = true;
			}
		}
		return found;
	}

	public boolean misspeltdoublevowel(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, finished = false, check = false;
		Integer vowelCount=0, vowelIndex=0, firstIndex=0, lastIndex=0, index=0, sentenceIndex;
		String[] vowels= {"a","e","i","o","u"};
		String content = "", partStr="", newContent="", vowel="", firstStr="", secondStr="";
		List<String> vowelList = Arrays.asList(vowels);
		List<Integer> indexList = new ArrayList<>();
		List<WordToken> sentence = null;
		char ch;
		if (part.equalsIgnoreCase("token")) {
			sentenceIndex = wordToken.getSentence();
			sentence = textDocument.getSentenceAtIndex(sentenceIndex);
			if (!wordStorage.wordExists("commonword", wordToken.getToken())) {
				content = wordToken.getToken();
				if (params.size()==0) {
					params = vowelList;
				}
				check=false;
                for (String item:vowelList) {
					vowelCount = StringUtils.countMatches(content,item);
					if (vowelCount>2) {
						check=true;
					}
				}
                if (check) {
				   vowelIndex=0;
				   finished=false;
				   while ((vowelIndex<vowelList.size()) && (!finished)) {
					    vowel = vowelList.get(vowelIndex);
					    vowelCount = StringUtils.countMatches(content,vowel);
						if (vowelCount>1) {
							index = 0;
							while ((index<content.length()) && (!finished)) {
								partStr = content.substring(index, index+1);
								if ((vowelList.contains(partStr)) && (!partStr.equalsIgnoreCase(vowel))) {
									firstStr = content.substring(0,index);
									secondStr = content.substring(index+1, content.length());
									newContent = firstStr + vowel + secondStr;
									if (wordStorage.wordExists("commonword", newContent)) {
										finished=true;
										found=true;
									}
								}
							  index++;
							}
						}
					  vowelIndex++;
					}
				}
			}
		} 
	 return found;
	}

    public boolean sentencecount(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		Integer position=0, wordIndex=0, sentenceCount=0, sentenceNumber=0;
		if ((part.equalsIgnoreCase("token")) && (wordToken!=null)) {
			wordIndex = wordToken.getIndex();
			sentenceNumber = wordToken.getSentence();
			sentenceCount = textDocument.getSentenceList().size();
			if ((sentenceNumber==1) && (wordIndex<=sentenceCount)) {
				  found = true;
			}
		} 
	  return found;
	}

	public boolean symbol(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, finished = false;
		Integer position=0, index=0;
		String content = "", param="";
		char ch, paramch;
		if ((part.equalsIgnoreCase("token")) && (wordToken!=null)) {
			content = wordToken.getToken();
			if (content.length()==1) {
				    ch = content.charAt(position);
				    if (params.size()==0) {
					    if ((Character.isLetter(ch)) && (Character.isDigit(ch))) {
						    found=false;
					    } else {
							found=true;
						}
					} else if (params.size()==1) {
						        param = params.get(0);
								index=0;
								while ((index<param.length()) && (!found)) {
									 paramch=param.charAt(index);
									 if (paramch==ch) {
										 found = true;
									 }
									 index++;
								}	 

					}	
			}
		} 
	  return found;
	}

	public boolean notsymbol(String part, WordToken wordToken, TextDocument textDocument,  List<String> params) {
		boolean found = false, asymbol = false;
		String content = "";
		if ((wordToken!=null) && (part.equalsIgnoreCase("token"))) {
			asymbol = this.symbol(part,wordToken,textDocument,params);
			found = !asymbol;
		} 
		  return found;
	}

	public boolean lower(String part, WordToken wordToken, TextDocument textDocument,  List<String> params) {
		boolean found = false, asymbol = false, finish = false, itemFound = false;
		String content = "", temp="", currentItem = "", item = "";
		Integer indicator = 0, index = 0, sentenceIndex = 0;
		if ((wordToken!=null) && (params.size()>0)) {
			currentItem = General.getValue(part, wordToken);
			sentenceIndex = wordToken.getSentence();
			while ((!finish) && (index<params.size())) {
				item = params.get(index);
				if (item.startsWith("$")) {
					itemFound = true;
					//this.checkPredefinedList(part, wordToken, textDocument, item);
					if (itemFound) {
						temp = currentItem.toLowerCase();
						indicator = temp.compareTo(currentItem);
						if (indicator==0) {
						   found = true;
						   finish = true;
						}
					}
			    } else {
					      temp = currentItem.toLowerCase();
						  indicator = temp.compareTo(currentItem);
						  if (indicator==0) {
						    found = true;
						    finish = true;
						 }
				       }
			  index++;
		    }
		}
		return found;
	}

	public boolean upper(String part, WordToken wordToken, TextDocument textDocument,  List<String> params) {
		boolean found = false, asymbol = false, finish = false, itemFound = false;
		String content = "", temp="", currentItem = "", item = "";
		Integer indicator = 0, index = 0, sentenceIndex = 0;
		if ((wordToken!=null) && (params.size()>0)) {
			currentItem = General.getValue(part, wordToken);
			sentenceIndex = wordToken.getSentence();
			while ((!finish) && (index<params.size())) {
				item = params.get(index);
				if (item.startsWith("$")) {
					itemFound = true;
					//this.checkPreDefinedList(part, wordToken, textDocument, item);
					if (itemFound) {
						temp = currentItem.toUpperCase();
						indicator = temp.compareTo(currentItem);
						if (indicator==0) {
						   found = true;
						   finish = true;
						}
					}
			    } else {
					      temp = currentItem.toUpperCase();
						  indicator = temp.compareTo(currentItem);
						  if (indicator==0) {
						    found = true;
						    finish = true;
						 }
				       }
			  index++;
		    }
		}
		return found;
	}

	public boolean notlower(String part, WordToken wordToken, TextDocument textDocument,  List<String> params) {
		boolean found = this.lower(part, wordToken, textDocument,  params);
		return !found;
	}

	public boolean notupper(String part, WordToken wordToken, TextDocument textDocument,  List<String> params) {
		boolean found = this.upper(part, wordToken, textDocument,  params);
		return !found;
	}

	public boolean existsafter(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, finish = false, itemFound = false;
		Integer index = 0, wordIndex = 0, currentWordIndex = 0, currentSentenceIndex = 0;
		String currentItem = "", item="", word="";
		List<WordToken> currentSentence=null;
		WordToken nextToken = null;
		if ((wordToken!=null) && (params.size()>0)) {
			    currentItem = General.getValue(part, wordToken); 
				currentWordIndex = wordToken.getIndex();
				currentSentenceIndex = wordToken.getSentence();
				currentSentence = textDocument.getSentenceAtIndex(currentSentenceIndex);
				while ((!finish) && (index<params.size())) {
						item = params.get(index);
						wordIndex = currentWordIndex+1;
					    while ((wordIndex<currentSentence.size()) && (!finish)) {
								nextToken = currentSentence.get(wordIndex);
								if ((nextToken!=null) && (General.theSame(part, nextToken, currentSentence, item))) {
									found = true;
									finish = true;
								}
						 wordIndex++; 
						}
					index++; 
				}
		}
		return found;
	}

	public boolean existsbefore(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, finish = false, itemFound = false;
		Integer index = 0, wordIndex = 0, currentWordIndex = 0, currentSentenceIndex=0;
		String currentItem = "", item="", word="";
		List<WordToken> currentSentence=null;
		WordToken previousToken = null;
		if ((wordToken!=null) && (params.size()>0)) {
			    currentItem = General.getValue(part, wordToken); 
				currentWordIndex = wordToken.getIndex();
				currentSentenceIndex = wordToken.getSentence();
				currentSentence = textDocument.getSentenceAtIndex(currentSentenceIndex);
				while ((!finish) && (index<params.size())) {
						item = params.get(index);
						wordIndex = currentWordIndex-1;
					    while ((wordIndex>-1) && (!finish)) {
								previousToken = currentSentence.get(wordIndex);
								if (previousToken != null) {
									word = General.getValue(part, previousToken);
									if (General.theSame(part, previousToken, currentSentence, item)) {
											found = true;
											finish = true;
									}
								}
						 wordIndex=wordIndex-1; 
						}
					index++; 
				}
		}
		return found;
	}

	public boolean notexistsafter(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, finish = false, itemFound = false;
		Integer index = 0, wordIndex = 0, currentWordIndex = 0, currentSentenceIndex=0;
		String currentItem = "", item="", word="";
		List<WordToken> currentSentence=null;
		WordToken nextToken = null;
		if ((wordToken!=null) && (params.size()>0)) {
			             currentItem = General.getValue(part, wordToken); 
				                  currentWordIndex = wordToken.getIndex();
								  currentSentenceIndex = wordToken.getSentence();
						        currentSentence = textDocument.getSentenceAtIndex(currentSentenceIndex);
							       while ((!finish) && (index<params.size())) {
								       item = params.get(index);
								       wordIndex = 0;
								       while (wordIndex<currentSentence.size()) {
									       nextToken = currentSentence.get(wordIndex);
									       if (nextToken.getIndex()>currentWordIndex) {
										       word = General.getValue(part, nextToken);
										       if (item.equalsIgnoreCase(word)) {
											       finish = true;
										       }
									       }
									       wordIndex++; 
								       }
								       index++; 
							       }
							       if (!finish) {
								       found = true;
							       }
		}
		return found;
	}
	public boolean notexistsbefore(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		found = this.existsbefore(part, wordToken, textDocument, params);
		return !found;
	}


	public boolean notmorethan(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
        boolean found=false;
		String key="",number="";
		Integer amount=0, size=0, wordIndex=0, sentenceIndex=0;
		List<WordToken> sentence=null;
		if ((wordToken!=null) && (params.size()==2)) {
			 wordIndex = wordToken.getIndex();
			 sentenceIndex = wordToken.getSentence();
			 if (wordIndex==0) {
                 key = params.get(0);
			     number = params.get(1);
			     try {
				       amount = Integer.parseInt(number);
			     } catch (Exception exception) {
				      amount=0;
			     }
			     if (amount>0) {
				     if (key.equalsIgnoreCase("$token")) {
                            sentence = textDocument.getSentenceAtIndex(sentenceIndex);
							size = sentence.size();
							if (size<=amount) {
								found = true;
							}
				     } else if (key.equalsIgnoreCase("$sentence")) {
						        size = textDocument.getSentenceList().size();
								if (size<=amount) {
									found=true;
								}
					 }
			      }
				}
		}
	  return found;
	}

	public boolean notin(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = true, finish = false, itemFound = false;
		Document regexDocument = null;
		Integer index = 0, sentenceIndex=0;
		String currentItem = "", item="", itemName="";
		List<String> wordList = null;
		if ((wordToken!=null) && (params.size()>0)) {
			currentItem = General.getValue(part, wordToken);
			sentenceIndex = wordToken.getSentence();
			while ((!finish) && (index<params.size())) {
				item = params.get(index);
				if (item.startsWith("$")) {
					itemName = item.substring(1,item.length());
					if (this.isPredefinedList(itemName)) {
                          wordList = this.getPredefinedList(itemName);
						  if (wordList.contains(currentItem)) {
							  finish = true;
						  }
					} else if (this.isPredefinedRegex(itemName)) {
                              finish = functionCallback.doFunction(part, "definedregex", item, wordToken, textDocument);
					}
				} else if (item.equalsIgnoreCase(currentItem)) {
						finish = true;
				}
				index++; 
			}
			if (finish) {
				found = false;
			}
		}
		return found;
	}

	public boolean regex(String part, WordToken wordToken, TextDocument textDocument, List<String> param) {
		String contents="", expression="";
		Boolean found = false, matchFound = false;
		Pattern pattern = null;
		Matcher matcher = null;
		contents = General.getValue(part, wordToken);
		if ((contents!=null) && (contents.length()>0) && (param.size()==1)) {
			expression = param.get(0);
			if (expression.length()>0) {
				expression = General.removeQuotes(expression);
				if (expression.length()>0) {
					try {
						     pattern = Pattern.compile(expression);
						              matcher = pattern.matcher(contents);
							               matchFound = matcher.find();
					} catch(PatternSyntaxException e) {
						    e.printStackTrace();
						        found = false;
							   }
					   if (matchFound) {
						          found = true;
							     } 
				}
			}
			   
		}
		  return found; 
	}
	public boolean manyword(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		    String WILDCARD_REGEX="^(?:(?!(%1)).)";
		        Integer index=0, sentenceIndex=0, wordIndex=wordToken.getIndex();
			    List<WordToken> sentence;
			        String contents="", token="", expression="", parameter="", wildcardRegex="";
				    String[] parts=null;
				    Boolean found = false, matchFound = false;
				    Pattern pattern = null;
				    Matcher matcher = null;
				    WordToken nextToken = null;
				    contents = General.getValue(part, wordToken);
				        if ((contents!=null) && (contents.length()>0) && (parameters.size()==1)) {
						parameter = parameters.get(0);
						if (parameter.length()>0) {
							parameter = General.removeQuotes(parameter);
							parts = parameter.split(" ");
							if (parts.length>1) {
								index=1;
								wordIndex++;
								sentenceIndex = wordToken.getSentence();
								sentence = textDocument.getSentenceAtIndex(sentenceIndex);
								nextToken = General.getWordToken(wordIndex, sentence);
								while ((index<parts.length) && (nextToken!=null)) {
									token = General.getValue(part, nextToken);
									contents = contents + " " + token;
									index++;
									wordIndex++;
									nextToken = General.getWordToken(wordIndex, sentence);
								}
							}
							if (contents.length()>0) {
								wildcardRegex = WILDCARD_REGEX.replace("%1", parameter);
								try {
									     pattern = Pattern.compile(wildcardRegex);
									              matcher = pattern.matcher(contents);
										               matchFound = matcher.find();
								} catch(PatternSyntaxException e) {
									    e.printStackTrace();
									        found = false;
										   }
								   if (matchFound) {
									          found = true;
										     }
							}
						}
						   
					}
					return found;
	}
	
	public boolean nan(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String currentWord = "";
		Long value=null;
		if (wordToken != null) {
			currentWord = wordToken.getToken();
			try {
				 value = Long.valueOf(currentWord);
			} catch (Exception exception) {
				found = true;
			}
		}
		return found;
	}

	public boolean punctuation(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, divider=false;
		String currentWord = "";
		Long value=null;
		char[] charArray=null;
		if ((params.size()==0) && (wordToken != null)) {
			currentWord = wordToken.getToken();
			if (currentWord.length()==1) {
                 charArray = currentWord.toCharArray();
				 divider=true;
				 for (char ch:charArray) {
					 if (Character.isLetter(ch)) {
						 divider=false;
					 }
				 }
				if (divider) {
					found=true;
				}
			}	 
		}
	   return found;
	}

	public boolean positionafter(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, validNumber=false, same=false;
		String wordparam = "", numberparam="";
		Integer wordIndex = wordToken.getIndex(),number=0;
		Integer paramsIndex = 0, tokenIndex = 0, tokenLimit = 0, sentenceIndex = 0;
		List<WordToken> sentence = null;
		WordToken someToken = null;
		if ((params.size()==2) && (wordIndex==0)) {
			numberparam = params.get(1);
			numberparam = General.removeQuotes(numberparam);
			validNumber = General.isNumber(numberparam);
			if (validNumber) {
				number = Integer.valueOf(numberparam);
				sentenceIndex = wordToken.getSentence();
			    sentence = textDocument.getSentenceAtIndex(sentenceIndex);
				wordparam = params.get(0);
			    wordparam = General.removeQuotes(wordparam);
			    tokenIndex = 0;
				tokenLimit = sentence.size();
				while ((!found) && (tokenIndex<tokenLimit)) {
					 someToken = sentence.get(tokenIndex);
                     same = General.theSame(part, someToken, sentence, wordparam);
					 if ((same) && (tokenIndex>number)) {
						 found=true;
					 }
					 tokenIndex++;
				}	 
			}
		}
		return found;
	}

	public boolean positionbefore(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, validNumber=false, same=false;
		String wordparam = "", numberparam="";
		Integer wordIndex = wordToken.getIndex(),number=0, sentenceIndex=0;
		Integer paramsIndex = 0, tokenIndex = 0, tokenLimit = 0;
		List<WordToken> sentence = null;
		WordToken someToken = null;
		if ((params.size()==2) && (wordIndex==0)) {
			numberparam = params.get(1);
			numberparam = General.removeQuotes(numberparam);
			validNumber = General.isNumber(numberparam);
			if (validNumber) {
				number = Integer.valueOf(numberparam);
				sentenceIndex = wordToken.getSentence();
			    sentence = textDocument.getSentenceAtIndex(sentenceIndex);
				wordparam = params.get(0);
			    wordparam = General.removeQuotes(wordparam);
			    tokenIndex = 0;
				tokenLimit = sentence.size();
				while ((!found) && (tokenIndex<tokenLimit)) {
					 someToken = sentence.get(tokenIndex);
                     same = General.theSame(part, someToken, sentence, wordparam);
					 if ((same) && (tokenIndex<number)) {
						 found=true;
					 }
					 tokenIndex++;
				}	 
			}
		}
		return found;
	}

	public boolean previous(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String previousItem = "", param = "";
		Integer wordIndex = wordToken.getIndex();
		Integer paramsIndex = 0, currentIndex=0;
		List<WordToken> sentence = null;
		WordToken previousToken = null;
		if ((params.size() > 0) && (wordIndex > 0)) {
			currentIndex = wordToken.getSentence();
			sentence = textDocument.getSentenceAtIndex(currentIndex);
			previousToken = sentence.get(wordIndex-1);
			previousItem = General.getValue(part, previousToken);
			previousItem = General.removeQuotes(previousItem);
			paramsIndex = 0;
			while ((!found) && (paramsIndex < params.size())) {
				param = params.get(paramsIndex);
				param = General.removeQuotes(param);
				if (previousItem.equalsIgnoreCase(param)) {
					found = true;
				}
				paramsIndex++;
			}
		}
		return found;
	}
	public boolean numbertokens(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		boolean found = false;
		Integer position=0, number=0, currentIndex=0;
		String expression="", numbStr;
		List<WordToken> line=null;
		currentIndex = wordToken.getSentence();
        line = textDocument.getSentenceAtIndex(currentIndex);
		if (parameters.size()==1) {
			expression = parameters.get(0);
			if ((expression.startsWith("\"")) || (expression.startsWith("'"))) {
				expression = expression.substring(1,expression.length());
			}
			if ((expression.endsWith("\"")) || (expression.endsWith("'"))) {
				expression = expression.substring(0,expression.length()-1);
			}
			position = expression.indexOf("=");
			if (position>-1) {
				numbStr = expression.substring(position,expression.length());
				number = Integer.valueOf(numbStr);
				if (number==line.size()) {
					found = true;
				}
			} else {
				position = expression.indexOf("<");
				if (position>-1) {
					numbStr = expression.substring(position,expression.length());
					number = Integer.valueOf(numbStr);
					if (line.size()<number) {
						found = true;
					}
				} else {
					position = expression.indexOf(">");
					if (position>-1) {
						numbStr = expression.substring(position,expression.length());
						number = Integer.valueOf(numbStr);
						if (line.size()>number) {
							found = true;
						}
					}
				}
			}
		}     
		return found;
	}

	public boolean endsWith(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String content = "";
		if (part.equalsIgnoreCase("postag")) {
			content = wordToken.getPostag();
		} else if (part.equalsIgnoreCase("lemma")) {
			content = wordToken.getLemma();
		} else if (part.equalsIgnoreCase("token")) {
			content = wordToken.getToken();
		}
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0)) {
			for (String param : params) {
				param = General.removeQuotes(param);
				if (content.endsWith(param)) {
					found = true;
				}
			}
		}
		return found;
	}

	public boolean notendswith(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, exists = false;
		Integer index = 0;
		String content = "", param="";
		content = General.getValue(part, wordToken);
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0) && (params!=null)) {
			while ((index<params.size()) && (!exists)) {
				param = params.get(index);
				param = General.removeQuotes(param);
				if (content.endsWith(param)) {
					exists = true;
				}
				index++;
			}
			if (!exists) {
				found = true;
			}
		}
		return found;
	}

	public boolean notcontains(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		found = this.contains(part, wordToken, textDocument, params);
		return !found;
	}

	public boolean spacingleft(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		Boolean found=false, quit=false;
		Integer index = 0;
		String param="";
		String wordOnLeft=wordToken.getSpacingLeft();
		if (params.size()>0) {
			  index=0;
			  while ((index<params.size()) && (!quit)) {
			      param = params.get(index);
			      if (param.equalsIgnoreCase("blank")) {
			         if (wordOnLeft.length()==0) {
						   quit=true;
						   found=true;
					 } 
				  } else if (param.equalsIgnoreCase(wordOnLeft)) {
					            quit=true;
								found=true;
				  }
				index++;
			  }
		}
	  return found; 
	}

	public boolean verblinked(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		Boolean isLinked = false, isVerb = false, isNextVerb = false;
		Integer wordIndex = 0, sentenceIndex = 0;
		String currentPostag = wordToken.getPostag(), nextPostag="", nextWord="";
		WordToken nextToken=null;
		wordIndex = wordToken.getIndex();
		isVerb = this.verb("postag", wordToken, textDocument, params);
		if (isVerb) {
			sentenceIndex = wordToken.getSentence();
			nextToken = textDocument.getSentenceAtIndex(sentenceIndex).get(wordIndex +1);
			isNextVerb = this.verb("postag", nextToken, textDocument, params);
			nextWord = nextToken.getToken();
			if ((isNextVerb) && (nextWord.endsWith("ing"))) {
				isLinked = true;
			}
		 }
		 return isLinked; 
	}
	
	
	public boolean preposition(String part, WordToken wordToken, TextDocument textDocument, List<String >parameters) {
		Boolean found = false, verb=false;
		String[] preposition_words= {"through", "since","to", "of", "at", "by", "for", "from", "into", "in", "under", "on", "off", "out", "over", "down", "up", "with"};
		String[] preposition_nouns = {"PRP", "NN", "NNS"};
		List<String> words = Arrays.asList(preposition_words);
		List<String> nouns = Arrays.asList(preposition_nouns);
		Integer wordIndex = wordToken.getIndex(), nextIndex = 0;
		WordToken nextToken = null, previousToken = null;
		String word = wordToken.getToken(), nextWord="", nextPostag="", lemmaBefore="";
		word = word.toLowerCase();
		if (words.contains(word)) {
			nextIndex = wordIndex+1;
			   nextToken = this.getWordToken(textDocument, nextIndex);
			      nextPostag = nextToken.getPostag();
			         if (nextToken!=null) {
					    nextWord = nextToken.getToken();
					       if (nextPostag.equalsIgnoreCase("DT")) {
								nextIndex = nextIndex+1;
								nextToken = this.getWordToken(textDocument, nextIndex);
								if (nextToken!=null) {
									nextPostag = nextToken.getPostag();
									nextWord = nextToken.getToken();
								}
							} else if (words.contains(nextWord)) {
										nextIndex = nextIndex+1;
										nextToken = this.getWordToken(textDocument, nextIndex);
										if (nextToken!=null) {
											nextPostag = nextToken.getPostag();
											nextWord = nextToken.getToken();
										}
							} 
					        if (nouns.contains(nextPostag)) {
							      found = true;
							         }
						     } 
		}
		  return found;
	}

	public boolean subject(String part, WordToken wordToken, TextDocument textDocument, List<String >parameters) {
		Boolean found = false;
		String dependency="";
		dependency = wordToken.getDependency();
		if ((dependency!=null) && (dependency.equalsIgnoreCase("subject"))) {
			found = true;
		}
		   return found;
	}
	
	public boolean object(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		Boolean found = false;
		String dependency="";
		dependency = wordToken.getDependency();
		if ((dependency!=null) && (dependency.equalsIgnoreCase("object"))) {
			found = true;
		}
		   return found;
	}

	public boolean validwithoutprefix(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false, exists = false, validWord=false;
		Integer index =0, wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		String content = General.getValue(part, wordToken), word="", param="";
		index = 0;
		validWord = wordStorage.wordExists("commonword", content);
		if (!validWord) {
		   while ((!found) && (index<parameters.size())) {
			   param = parameters.get(index);
			   param = General.removeQuotes(param);
			   param = param.toLowerCase();
			   content = General.removeQuotes(content);
			   content = content.toLowerCase();
			   if (content.startsWith(param)) {
				   word = content.substring(param.length(),content.length());
				   exists = wordStorage.wordExists("commonword", word);
				   if (exists) {
					   found = true;
				   }
			   }
			   index++;
		   }
		}   
		return found;
	}

	public boolean validwithprefix(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false, exists = false, validWord=false;
		Integer index =0, wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		String content = General.getValue(part, wordToken), word="", param="";
		index = 0;
		if (parameters.size()>0) {
		    while ((!found) && (index<parameters.size())) {
			   param = parameters.get(index);
			   param = General.removeQuotes(param);
			   param = param.toLowerCase();
			   content = General.removeQuotes(content);
			   content = content.toLowerCase();
			   content = param+content;
			   exists = wordStorage.wordExists("commonword", content);
			   if (exists) {
					found = true;
			   }
			   index++;
		   }
		}   
		return found;
	}

	public boolean validafterreplace(String part, WordToken wordToken, TextDocument textDocument, List<String> parameters) {
		Boolean found = false, exists = false, validWord=false;
		Integer index =0, wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		String content = General.getValue(part, wordToken), word="", param1="", param2="", token="", lemma="", postag="";
		String firstpart="", secondpart="";
		WordToken tempToken=null;
		index = 0;
		if (parameters.size()==2) {
			validWord = wordStorage.wordExists("commonword", content);
			if (!validWord) {
			   param1 = parameters.get(0);
			   param1 = General.removeQuotes(param1);
			   param1 = param1.toLowerCase();
			   param2 = parameters.get(1);
			   param2 = General.removeQuotes(param2);
			   param2 = param2.toLowerCase();
               if ((content!=null) && (content.contains(param1))) {
				   firstpart = content;
				   secondpart = content;
				   while ((!found) && (secondpart.contains(param1))) { 
					   index = secondpart.indexOf(param1);
					   if (index>-1) {
						   firstpart = content.substring(0, content.length()+index-secondpart.length());
						   secondpart = secondpart.substring(index+param1.length(), secondpart.length());
						   word = firstpart + param2 + secondpart;
						   exists = wordStorage.wordExists("commonword", word);
			               if (exists) {
				  	           found = true;
			               } else {
							   secondpart = secondpart;
						   }
				       }	 
		           }
			    }
			}			   
		} 	
	   return found;
	}

	public boolean anyword(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String word="";
		char ch;
		if (wordToken!=null) {
			word = wordToken.getToken();
			if ((word!=null) && (word.length()>0)) {
				ch = word.charAt(0);
				if (Character.isLetter(ch)) {
					found = true;
				}
			}
		}
		return found;
	}
	public boolean notprevious(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		found = !(this.previous(part, wordToken, textDocument, params));
		return found;
	}

	public boolean next(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String nextItem = "", param = "";
		Integer wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		Integer paramsIndex = 0;
		List<WordToken> sentence = null;
		WordToken nextToken = null;
		sentence = textDocument.getSentenceAtIndex(sentenceIndex);
		if ((params.size() > 0) && (wordIndex < sentence.size() - 1)) {
			nextToken = sentence.get(wordIndex+1);
			nextItem = General.getValue(part, nextToken);
			paramsIndex = 0;
			nextItem = General.removeQuotes(nextItem);
			while ((!found) && (paramsIndex < params.size())) {
				param = params.get(paramsIndex);
				param = General.removeQuotes(param);
				if (nextItem.equalsIgnoreCase(param)) {
					found = true;
				}
				paramsIndex++;
			}
		}
		return found;
	}

	public boolean wordslice(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		String itemToRemove = "", param = "", content = "";
		Integer wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		Integer paramsIndex = 0;
		List<WordToken> sentence = null;
		WordToken nextToken = null;
		sentence = textDocument.getSentenceAtIndex(sentenceIndex);
		if (params.size()==2) {
			itemToRemove = params.get(1);
			content = wordToken.getToken();
			if (content.endsWith(itemToRemove)) {
				content=content.substring(0,content.length()-itemToRemove.length());
			}
		}
		return found;
	}

	public boolean wordsinasentence(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false, isAWord = false;
		String param = "", value = "", word = "", operator = "";
		Integer wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		Integer intValue = 0, numberWords = 0, code = 0;
		List<WordToken> sentence = null;
		WordToken nextToken = null;
		
		if (params.size()==1) {
			wordCount = 0;
			param = params.get(0);
			param = param.toLowerCase();
			sentence = textDocument.getSentenceAtIndex(sentenceIndex);
			if ((param.startsWith("eq")) || (param.startsWith("lt")) || (param.startsWith("gt"))) {
				operator = param.substring(0,2);
				value = param.substring(2, param.length());
				try {
				      intValue = Integer.valueOf(value); 
				} catch (Exception exception) {
				      intValue = -1;
				}
				if (intValue>=0) {
                    for (WordToken token:sentence) {
						word = token.getToken();
						isAWord = true;
						for (int i = 0; i < word.length(); i++) {
							code = (int) word.charAt(i);
                            if (!(code > 64 && code < 91) && // upper alpha (A-Z)
                                !(code > 96 && code < 123)) { // lower alpha (a-z)
						        isAWord=false;
							} 
					    }
						if (isAWord) {
                            numberWords++;
						}
					}
					if (operator.equalsIgnoreCase("eq")) {
						if (numberWords==intValue) {
							found = true;
						}
					} else if (operator.equalsIgnoreCase("lt")) {
							if (numberWords<intValue) {
								found = true;
							}
					} else if (operator.equalsIgnoreCase("gt")) {
							if (numberWords>intValue) {
								found = true;
							}
					}
				}
			}	
		}
		return found;
	}

	public boolean notnext(String part, WordToken wordToken, TextDocument textDocument, List<String> params) {
		boolean found = false;
		found = !(this.next(part, wordToken, textDocument, params));
		return found;
	}


	    
	    private Integer countOccurrencesOf(String content, String part) {
		    Integer index, occurrences=0, partlength = part.length();
		    Boolean finish = false;
		    String value = content;
		    while (!finish) {
			    index = value.indexOf(part);
			    if (index>0) {
				    occurrences = occurrences + 1;
				    if ((index+partlength)<value.length()) {
					      value = value.substring(index+partlength, value.length());
					         } else {
							     value="";
							        }
			    } else {
				    finish = true;
			    }
		    }
		     return occurrences; 
	    }
	        private String getCheckValue(String part, WordToken wordToken) {
			String value="";
			if (part.equalsIgnoreCase("token")) {
				value = wordToken.getToken();
			} else if (part.equalsIgnoreCase("lemma")) {
				value = wordToken.getLemma();
			} else {
				value = wordToken.getPostag();
			}
			 return value; 
		}

		private WordToken getWordToken(TextDocument textDocument, Integer index) {
			Integer sentenceIndex=0;
			WordToken wordToken=null;

			if (index>0) {
				sentenceIndex = wordToken.getSentence();
				wordToken = textDocument.getSentenceAtIndex(sentenceIndex).get(index);
			}
			return wordToken; 
		}

}

