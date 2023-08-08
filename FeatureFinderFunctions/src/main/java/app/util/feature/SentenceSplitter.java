package app.util.feature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class SentenceSplitter {
   private String[] rulesToApply= {"abbreviations", "quotes"};
   private String[] quotations= {"\"","'"};
   private static final Set<String> ABBREVIATIONS = new HashSet<String>();
      static {
// Civilian titles
ABBREVIATIONS.add("Dr.");
ABBREVIATIONS.add("Ph.D.");
ABBREVIATIONS.add("Ph.");
ABBREVIATIONS.add("Mr.");
ABBREVIATIONS.add("Mrs.");
ABBREVIATIONS.add("Ms.");
ABBREVIATIONS.add("Prof.");
ABBREVIATIONS.add("Esq.");
// Military ranks
ABBREVIATIONS.add("Maj.");
ABBREVIATIONS.add("Gen.");
ABBREVIATIONS.add("Adm.");
ABBREVIATIONS.add("Lieut.");
ABBREVIATIONS.add("Lt.");
ABBREVIATIONS.add("Col.");
ABBREVIATIONS.add("Sgt.");
ABBREVIATIONS.add("Cpl.");
ABBREVIATIONS.add("Pte.");
ABBREVIATIONS.add("Cap.");
ABBREVIATIONS.add("Capt.");
// Political titles
ABBREVIATIONS.add("Sen.");
ABBREVIATIONS.add("Pres.");
ABBREVIATIONS.add("Rep.");
// Religious titles
ABBREVIATIONS.add("St.");
ABBREVIATIONS.add("Rev.");
// Geographical and addresses
ABBREVIATIONS.add("Mt.");
ABBREVIATIONS.add("Rd.");
ABBREVIATIONS.add("Cres.");
ABBREVIATIONS.add("Ln.");
ABBREVIATIONS.add("Ave.");
ABBREVIATIONS.add("Av.");
ABBREVIATIONS.add("Bd.");
ABBREVIATIONS.add("Blvd.");
ABBREVIATIONS.add("Co.");
ABBREVIATIONS.add("co.");
// Commercial
ABBREVIATIONS.add("Ltd.");
ABBREVIATIONS.add("Plc.");
ABBREVIATIONS.add("PLC.");
ABBREVIATIONS.add("Inc.");
ABBREVIATIONS.add("Pty.");
ABBREVIATIONS.add("Corp.");
ABBREVIATIONS.add("Co.");
// Academic
ABBREVIATIONS.add("et.");
ABBREVIATIONS.add("al.");
ABBREVIATIONS.add("ed.");
ABBREVIATIONS.add("eds.");
ABBREVIATIONS.add("Ed.");
ABBREVIATIONS.add("Eds.");
ABBREVIATIONS.add("Fig.");
ABBREVIATIONS.add("fig.");
ABBREVIATIONS.add("Ref.");
ABBREVIATIONS.add("ref.");
// General
ABBREVIATIONS.add("etc.");
ABBREVIATIONS.add("usu.");
ABBREVIATIONS.add("e.g.");
ABBREVIATIONS.add("pp.");
ABBREVIATIONS.add("vs.");
ABBREVIATIONS.add("v.");
// Measures
ABBREVIATIONS.add("yr.");
ABBREVIATIONS.add("yrs.");
ABBREVIATIONS.add("g.");
ABBREVIATIONS.add("mg.");
ABBREVIATIONS.add("kg.");
ABBREVIATIONS.add("gr.");
ABBREVIATIONS.add("lb.");
ABBREVIATIONS.add("lbs.");
ABBREVIATIONS.add("oz.");
ABBREVIATIONS.add("in.");
ABBREVIATIONS.add("mi.");
ABBREVIATIONS.add("m.");
ABBREVIATIONS.add("M.");
ABBREVIATIONS.add("mt.");
ABBREVIATIONS.add("mtr.");
ABBREVIATIONS.add("ft.");
ABBREVIATIONS.add("max.");
ABBREVIATIONS.add("min.");
ABBREVIATIONS.add("Max.");
ABBREVIATIONS.add("Min.");
ABBREVIATIONS.add("inc.");
ABBREVIATIONS.add("exc.");
// Single letter abbreviations
ABBREVIATIONS.add("A.");
ABBREVIATIONS.add("B.");
ABBREVIATIONS.add("C.");
ABBREVIATIONS.add("D.");
ABBREVIATIONS.add("E.");
ABBREVIATIONS.add("F.");
ABBREVIATIONS.add("G.");
ABBREVIATIONS.add("H.");
ABBREVIATIONS.add("I.");
ABBREVIATIONS.add("J.");
ABBREVIATIONS.add("K.");
ABBREVIATIONS.add("L.");
ABBREVIATIONS.add("M.");
ABBREVIATIONS.add("N.");
ABBREVIATIONS.add("O.");
ABBREVIATIONS.add("P.");
ABBREVIATIONS.add("Q.");
ABBREVIATIONS.add("R.");
ABBREVIATIONS.add("S.");
ABBREVIATIONS.add("T.");
ABBREVIATIONS.add("U.");
ABBREVIATIONS.add("V.");
ABBREVIATIONS.add("W.");
ABBREVIATIONS.add("X.");
ABBREVIATIONS.add("Y.");
ABBREVIATIONS.add("Z.");
ABBREVIATIONS.add("a.");
ABBREVIATIONS.add("b.");
ABBREVIATIONS.add("c.");
ABBREVIATIONS.add("d.");
ABBREVIATIONS.add("e.");
ABBREVIATIONS.add("f.");
ABBREVIATIONS.add("g.");
ABBREVIATIONS.add("h.");
ABBREVIATIONS.add("i.");
ABBREVIATIONS.add("j.");
ABBREVIATIONS.add("k.");
ABBREVIATIONS.add("l.");
ABBREVIATIONS.add("m.");
ABBREVIATIONS.add("n.");
ABBREVIATIONS.add("o.");
ABBREVIATIONS.add("p.");
ABBREVIATIONS.add("q.");
ABBREVIATIONS.add("r.");
ABBREVIATIONS.add("s.");
ABBREVIATIONS.add("t.");
ABBREVIATIONS.add("u.");
ABBREVIATIONS.add("v.");
ABBREVIATIONS.add("w.");
ABBREVIATIONS.add("x.");
ABBREVIATIONS.add("y.");
ABBREVIATIONS.add("z.");
// Temporal
ABBREVIATIONS.add("Jan.");
ABBREVIATIONS.add("Feb.");
ABBREVIATIONS.add("Mar.");
ABBREVIATIONS.add("Apr.");
ABBREVIATIONS.add("Jun.");
ABBREVIATIONS.add("Jul.");
ABBREVIATIONS.add("Aug.");
ABBREVIATIONS.add("Sep.");
ABBREVIATIONS.add("Sept.");
ABBREVIATIONS.add("Oct.");
ABBREVIATIONS.add("Nov.");
ABBREVIATIONS.add("Dec.");
ABBREVIATIONS.add("Mon.");
ABBREVIATIONS.add("Tue.");
ABBREVIATIONS.add("Wed.");
ABBREVIATIONS.add("Thu.");
ABBREVIATIONS.add("Fri.");
ABBREVIATIONS.add("Sat.");
ABBREVIATIONS.add("Sun.");
}

   public SentenceSplitter() {
   }

   public List<String> getSentences(String text) {
      Boolean finish = false;
      Word word=null;
      Integer start=0, end=0;
      List<Word> wordList = new ArrayList<>();
      List<String> sentenceList = new ArrayList<>();
      String sentence="";
      word = new Word("",start,end,"notknown");
      while (!finish) {
          finish = getSymbol(text, word);
          if (isPunctuation(word)) {
              word.setNewline();
          }
         wordList.add(word);
         word = new Word("", word.getEnd()+1, word.getEnd(), "notknown");
      }
/* alter punctuation markers depending on rules */
      for (String ruleStr:rulesToApply) {
         applyRule(ruleStr, wordList);
      }
      sentence="";
      for (Word token:wordList) {
         sentence = sentence + token.getValue();
         if (token.getNewline()) {
             sentenceList.add(sentence);
             sentence = "";
         }
      }
      if (sentence.length()>1) {
         sentenceList.add(sentence);
      }
   return sentenceList;
  }

  private void applyRule(String rule, List<Word> wordList) {
      switch (rule) {
         case "abbreviations": doAbbreviations(wordList);break;
         case "quotes": doQuotes(wordList);
      }
  }

  private void doAbbreviations(List<Word> wordList) {
     Integer index=0;
     Boolean finish=false;
     String abbrev="";
     Word token=null, oldToken=null;
     while ((!finish) && (index<wordList.size())) {
         token = wordList.get(index);
         if (token.getNewline()) {
             if (index>0) {
                oldToken = wordList.get(index-1);
                abbrev = oldToken.getValue()+token.getValue();
                if (ABBREVIATIONS.contains(abbrev)) {
                   token.unsetNewline();
                }
             }    
          }
    index++;
    }
  }
  
  private void doQuotes(List<Word> wordList) {
    Integer index=0;
    Boolean finish=false, quoteSeen=false;
    String value="";
    List<String> quotesList = Arrays.asList(quotations);
    Word token=null, oldToken=null;
    while ((!finish) && (index<wordList.size())) {
        token = wordList.get(index);
        value = token.getValue();
        if ((!quoteSeen) && (quotesList.contains(value))) {
            quoteSeen=true;
        } else if ((quoteSeen) && (quotesList.contains(value))) {
            quoteSeen=false;
        } else if (token.getNewline()) {
                  if (quoteSeen) {
                     token.unsetNewline();
                  }
       }
      index++;
    }
  }

  private Boolean getSymbol(String text, Word word) {
      String[] word_separators={" ",","};
      String wordPart="", strType="", strPart="", currentType="";
      String endofsentence="";
      Boolean finished=false, completed=false;
      char part;
      Integer sentenceLength=0, index=word.getStart();
      List<String> separators = Arrays.asList(word_separators);
      while ((!finished) && (index<text.length())) {
         part = text.charAt(index);
         strType = this.getPartType(part);
         if (index==word.getStart()) {
             currentType=strType;
             wordPart = wordPart + part;
             word.setValue(wordPart);
             word.setBounds(word.getStart(),index-1);
         } else {
              if (currentType.equalsIgnoreCase(strType)) {
                 wordPart = wordPart + part;
              } else {
                 word.setValue(wordPart);
                 word.setType(currentType);
                 word.setBounds(word.getStart(),index-1);
                 finished = true;
            }
         }
       index++;
     }
     if (!finished) {
       completed = true;
       word.setValue(wordPart);
       word.setBounds(word.getStart(),index-1);
     }
    return completed;
   }

   private Boolean isPunctuation(Word word) {
       String[] dividers = {".","...","?","??","???","????","!","!!","!!!","!!!!",":-"};
       String value = word.getValue(); 
       Boolean punctuation=false;
       for (String symbol:dividers) {
           if (symbol.equalsIgnoreCase(value)) {
              punctuation = true;
           }
       }
      return punctuation;
   }

   private String getPartType(char part) {
      char quoteDouble= '"';
      char quoteSingle='\'';
      String space=" ";
      String type=space;
     if (Character.isSpaceChar(part)) {
          type="space";
     } else if (Character.isLetter(part)) {
          type="letter";
     } else if (Character.isDigit(part)) {
          type="digit";
     } else if (quoteSingle==part) {
          type="quotesingle";
     } else if (quoteDouble==part) {
          type="quotedouble";
     } else {
          type="symbol";
     }
    return type;
  }

}