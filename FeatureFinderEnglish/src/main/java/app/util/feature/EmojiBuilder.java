package app.util.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.net.URLDecoder;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser; 

public class EmojiBuilder {

    public EmojiBuilder() {
    }

    public Map<String, Emoji> build(Document emojiDocument) {
        Emoji emoji = null;
        String contents = emojiDocument.getContents();
        String strObject = "";
        String[] emojiParts = null;
        String[] emojiFields = null;
        Map<String, Emoji> emojiMap = new HashMap<>();
        try {
              contents = URLDecoder.decode(contents);
              emojiParts = contents.split("}");
              for (String emojiStr: emojiParts) {
                  if ((emojiStr.startsWith(",")) || (emojiStr.startsWith("["))) {
                    emojiStr = emojiStr.substring(1, emojiStr.length());
                  }
                  emojiStr = emojiStr.replace("{","");
                  emojiStr = emojiStr.replace("\"","");
                  emojiFields = emojiStr.split(",");
                  if (emojiFields.length==5) {
                      emoji = new Emoji(emojiFields[0], emojiFields[2], emojiFields[1], emojiFields[3], emojiFields[4]);
                      emojiMap.put(emojiFields[1], emoji);
                  }
              }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
      return emojiMap;
    }
}