package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RegexDocumentList {

    @JsonProperty("messagetype")
    private String messagetype;

    @JsonProperty("regexdocumentlist")
    private List<RegexDocument> regexdocumentlist;

    public RegexDocumentList() {
       messagetype = "";
       regexdocumentlist = new ArrayList<>();
    }

    public void setMessageType(String messagetype) {
        this.messagetype = messagetype;
    }

    @JsonIgnore
    public String getMessageType() {
        return messagetype;
    }

    public void setRegexDocumentList(List<RegexDocument> regexDocumentList) {
        this.regexdocumentlist = regexDocumentList;
    }

    @JsonIgnore
    public List<RegexDocument> getRegexDocumentList() {
        return regexdocumentlist;
    }

    public void fromJson(String jsonStr) {
         RegexDocumentList regexDocumentList = null;
         try {
              regexDocumentList = new ObjectMapper().readValue(jsonStr, RegexDocumentList.class);
              this.setMessageType(regexDocumentList.getMessageType());
              this.setRegexDocumentList(regexDocumentList.getRegexDocumentList());
         } catch (Exception exception) {
             exception.printStackTrace();
         }
    }

    public String toJson() {
        String jsonStr = "";
        try {
            jsonStr = new ObjectMapper().writeValueAsString(this);
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return jsonStr;
    }


}