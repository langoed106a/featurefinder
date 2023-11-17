package app.util.feature;

import java.util.Map;

import javax.json.JsonValue;
import javax.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class RegexDocument extends Document {
    private String granularity;
    private String group;
    private String regex;
    private String precondition;
    private String postcondition;
    private String invariant;
    
    public RegexDocument() {
       super();
       this.granularity = "";
       this.group = "";
       this.regex = "";
       this.precondition = "";
       this.postcondition = "";
       this.invariant = "";
    }

    public RegexDocument(String name, String group, String description, String contents, String granularity, String precondition, String postcondition, String invariant) {
       this.name = name;
       this.contents = contents;
       this.description = description;
       this.granularity = granularity;
       this.group = group;
       this.precondition = precondition;
       this.postcondition = postcondition;
       this.invariant = invariant;
       this.type = "regex";
    }

    public void setGranularity(String granularity) {
       this.granularity = granularity;
    }

    public String getGranularity() {
       return granularity;
    }

    public void setGroup(String group) {
       this.group = group;
    }

    public String getGroup() {
       return group;
    }

    public void setPostcondition(String postcondition) {
       this.postcondition = postcondition;
    }

    public String getPostcondition() {
       return postcondition;
    }

   public void setPrecondition(String precondition) {
      this.precondition = precondition;
   }

   public String getPrecondition() {
      return precondition;
   }

   public void setRegex(String regex) {
      this.regex = regex;
   }

   public String getRegex() {
      return regex;
   }

   public void setInvariant(String invariant) {
      this.invariant = invariant;
   }

   public String getInvariant() {
      return invariant;
   }

    public void fromJson(String jsonStr) {
         RegexDocument regexDocument = null;
         try {
              regexDocument = new ObjectMapper().readValue(jsonStr, RegexDocument.class);
              this.setId(regexDocument.getId());
              this.setType(regexDocument.getType());
              this.setLabel(regexDocument.getLabel());
              this.setOrigin(regexDocument.getOrigin());
              this.setName(regexDocument.getName());
              this.setContents(regexDocument.getContents());
              this.setDescription(regexDocument.getDescription());
              this.setOrigin(regexDocument.getOrigin());
              this.setRegex(regexDocument.getRegex());
              this.setGranularity(regexDocument.getGranularity());
              this.setPostcondition(regexDocument.getPostcondition());
              this.setPrecondition(regexDocument.getPrecondition());
              this.setInvariant(regexDocument.getInvariant());
         } catch (Exception exception) {
             exception.printStackTrace();
         }
    }

   public void fromDocument(Document document) {
       String jsonStr = "", contents="";
        Map<String, String> jsonMap=null;
        ObjectMapper objectMapper=null;
        try {
            contents = document.getContents();
            objectMapper = new ObjectMapper();
            jsonMap = objectMapper.readValue(contents, new TypeReference<Map<String,String>>(){});
            if (jsonMap !=null) {
               this.setRegex(jsonMap.get("regex"));
               this.setGranularity(jsonMap.get("granularity"));
               this.setPrecondition(jsonMap.get("precondition"));
               this.setPostcondition(jsonMap.get("precondition"));
               this.setInvariant(jsonMap.get("invariant"));
            }
            System.out.println("*****Regex:"+this.getRegex());
            this.setName(document.getName());
            this.setType("regex");
            this.setDescription(document.getDescription());
            this.setId(document.getId());
        } catch (Exception exception) {
                exception.printStackTrace();
        }
   }

   public String toJson() {
        String jsonStr = "";
        Map<String, String> jsonMap=null;
        ObjectMapper objectMapper=null;
        try {
            jsonStr = new ObjectMapper().writeValueAsString(this);
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return jsonStr;
    }
    
 }