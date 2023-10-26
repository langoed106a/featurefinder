package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RegexDocumentList {

    @JsonProperty("tokenid")
    private String tokenid;

    @JsonProperty("regexdocumentlist")
    private List<RegexDocument> regexdocumentlist;

    public RegexDocumentList() {
       tokenid = "";
       regexdocumentlist = new ArrayList<>();
    }

    public void setTokenId(String tokenid) {
        this.tokenid = tokenid;
    }

    @JsonIgnore
    public String getTokenId() {
        return tokenid;
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
              this.setTokenId(regexDocumentList.getTokenId());
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