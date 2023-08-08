package app.util.feature;

import java.util.List;
import java.util.ArrayList;

public class SentenceCompare {
    private static final String WORD_FILLER="<word>";

    public SentenceCompare() {

    }

    public SentenceMatch theSame(List<WordToken> first, String firstId, List<WordToken> second, String secondId,Integer minimumWordMatch, String startswith, String contains,Integer matchWordGap, Boolean repeatedGroups) {
        Boolean finished=false;
        String tag="";
        List<PartMatch> theSameList = new ArrayList<>();
        List<PartMatch> theFilteredSameList = new ArrayList<>();
        SentenceMatch sentenceMatch = null;
        Integer index=0, offset=0, wordgap=0;
        if ((first!=null) && (first.size()>0) && (second!=null) && (second.size()>0)) {
               if (first.size()>second.size()) { 
                      offset=0;
                      finished = false;
                      sentenceMatch = new SentenceMatch(firstId, secondId);
                      while ((!finished) && (offset<first.size())) {
                           theSameList = compare(first, second, offset);
                           wordgap = getWordGap(theSameList);
                           if ((minimumWordMatch>0) && (theSameList.size()>=minimumWordMatch) && (wordgap<=matchWordGap))  {
                                 if (matchesFilter(theSameList,startswith,contains)) {
                                      sentenceMatch.addMatch(theSameList);
                                 }
                           }
                           offset = offset + 1;
                      }
                      if (repeatedGroups) {
                        sentenceMatch.removeRepeatedGroups();
                      }
               } else {
                       offset=0;
                       finished = false;
                       sentenceMatch = new SentenceMatch(secondId, firstId);
                       while ((!finished) && (offset<second.size())) {
                            theSameList = compare(second, first, offset);
                            wordgap = getWordGap(theSameList);
                            if ((minimumWordMatch>0) && (theSameList.size()>=minimumWordMatch) && (wordgap<=matchWordGap))  {
                                if (matchesFilter(theSameList,startswith,contains)) {
                                     sentenceMatch.addMatch(theSameList);
                                }
                          }
                            offset = offset + 1;
                       }
                       if (repeatedGroups) {
                        sentenceMatch.removeRepeatedGroups();
                      }
               }
        }    
        return sentenceMatch;
    }

    private List<PartMatch> compare(List<WordToken> first, List<WordToken> second, Integer offset) {
        Integer index=0, wordTokenIndex=0;
        PartMatch partMatch = new PartMatch(offset,0,""), newMatch=null;
        String tag="";
        List<PartMatch> theSameList=new ArrayList<>();
        WordToken wordToken=null;
        if ((offset>-1) && (offset<first.size())) {
            wordTokenIndex = offset;
            while (wordTokenIndex<first.size()) {
                wordToken = first.get(wordTokenIndex);
                newMatch = this.contains(second, wordToken, partMatch);
                if (newMatch!=null) {
                    theSameList.add(newMatch);
                    partMatch = new PartMatch(newMatch.getFirst()+1, newMatch.getSecond()+1, "");
                }
            wordTokenIndex++; 
            }
        }       
      return theSameList;
    }

    private Integer getWordGap(List<PartMatch> theSameList) {
        String match="";
        Integer wordgap=0;
        for (PartMatch partMatch:theSameList) {
            match = partMatch.getMatch();
            if (match.equalsIgnoreCase(WORD_FILLER)) {
                wordgap=wordgap+1;
            }
        }
      return wordgap;
    }

    private PartMatch contains(List<WordToken> wordList, WordToken wordToken, PartMatch lastMatch) {
        Boolean found=false;
        Integer index=0;
        PartMatch partMatch = null;
        String tag=null;
        WordToken word=null;
        index = lastMatch.getSecond();
        if (index<wordList.size()) {
            while ((index<wordList.size()) && (!found)) {
                  word = wordList.get(index);
                  if (wordToken.getToken().equalsIgnoreCase(word.getToken())) {
                      tag = word.getToken();
                      partMatch = new PartMatch(lastMatch.getFirst(),index,tag+"(T)");
                      found = true;
                  } else if (wordToken.getLemma().equalsIgnoreCase(word.getLemma())) {
                      tag = word.getLemma();
                      partMatch = new PartMatch(lastMatch.getFirst(),index,tag+"(L)");
                      found = true;
                  } else if (wordToken.getPostag().equalsIgnoreCase(word.getPostag())) {
                      tag = word.getPostag();
                      partMatch = new PartMatch(lastMatch.getFirst(),index,tag+"(P)");
                      found = true;
                 }
                 index++;
            }
            if (!found) {
                  partMatch = new PartMatch(lastMatch.getFirst(),index,WORD_FILLER);
            }
        }
        return partMatch;
    }

    private Boolean matchesFilter(List<PartMatch> theSameList, String startswith, String contains) {
          List<PartMatch> mainMatches=new ArrayList<>();
          Integer index=0;
          PartMatch partMatch=null;
          Boolean okay = true, found=false;
          String word;
          if ((startswith!=null) && (startswith.length()>0)) {
               if ((theSameList!=null) && (theSameList.size()>0)) {
                      word = theSameList.get(0).getMatch();
                      okay = checkTheSame(word, startswith);
               }
            }
            if ((contains!=null) && (contains.length()>0)) {
                if ((theSameList!=null) && (theSameList.size()>0)) {
                    found = false;
                    index = 0;
                    while ((!found) && (index<theSameList.size())) {
                        partMatch = theSameList.get(index);
                        found = checkTheSame(partMatch.getMatch(), contains);
                        index = index +1;
                    }
                   if (!found) {
                       okay=false;
                   }
                }
             }  
        return okay;
    }

    public Boolean checkTheSame(String word, String startswith) {
        Boolean found = false;
        Integer index = 0;
        String[] values=null;
        String value="";
        values = startswith.split(",");
        while ((!found) && (index<values.length)) {
            value = values[index];
            if (value.equalsIgnoreCase(word)) {
                found = true;
            }
            index = index +1;
        }
      return found;
    }

}