package app.util.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.util.feature.Emoji;

public class EmojiChecker {
    private Map<String, Emoji> emojiMap;
    private Integer characterLower = 0x0000;
    private Integer characterUpper = 0x007F;

    public EmojiChecker(Map<String, Emoji> emojiMap) {
        this.emojiMap = emojiMap;
    }

    public String getCodepoint(String item) {
        Character ch=null;
        Integer index=0, pointer=0;
        Long hexBoundary = Long.parseLong("D83D",16);
        Long hexValue = null;
        String codepoint="";
        List<Character> codeUnits = new ArrayList<>();
        for (int j=0; j<item.length(); j++) {
           ch = item.charAt(j);
           codeUnits.add(ch);
        }
        pointer=0;
        index=0;
        codepoint="";
        while (index<item.length()) {
            ch = item.charAt(index);
            hexValue = Long.valueOf((int)ch);
            if ((Character.isSurrogate(ch)) && ((index+1)<item.length())) {
                codepoint = codepoint + " U+"+String .format("%x",Character.toCodePoint(codeUnits.get(index), codeUnits.get(index+1)));
                index=index+2;
            } else {
                codepoint = codepoint + " U+"+String.format("%x", (int) codeUnits.get(index));
                index=index+1;
            }
        }
        if (codepoint.startsWith(" ")) {
            codepoint = codepoint.substring(1,codepoint.length());
        }
      return codepoint;
    }

    public List<UTFCharacter> getUTFList(String item) {
        Emoji emoji;
        Character ch = null;
        Integer lastIndex=-1, intChar=0;
        String str="", type="", codepoint="", word = "", newline="", noline="";
        UTFCharacter utfCharacter;
        List<UTFCharacter> utfCharacterList = new ArrayList<>();
        for (int k=0; k<item.length(); k++) {
            str = item.substring(k, k+1);
            ch = str.charAt(0);
            intChar = (int)ch;
            if ((intChar>=characterLower) && (intChar<=characterLower)) {
               if (lastIndex==(k-1)) {
                  word = word + ch;
                  lastIndex = k;
               } else {
                  newline = newline + word;
                  word = "";
                  word = word + ch;
               }
            } else if (lastIndex==(k-1)) {
                      word = word + ch;
                      lastIndex = k;
                   } else {
                      noline = noline + ch;
                   }
            codepoint = this.getCodepoint(str);
            emoji = emojiMap.get(codepoint);
            if (emoji==null) {
                type= "character";
            } else {
                type = "emoji";
            }
            System.out.println("**Character:"+str+"   Codepoint:"+codepoint+"   Type:"+type);
            utfCharacter = new UTFCharacter(str.charAt(0), type, codepoint);
            utfCharacterList.add(utfCharacter);
        }
        return utfCharacterList;
    }

}