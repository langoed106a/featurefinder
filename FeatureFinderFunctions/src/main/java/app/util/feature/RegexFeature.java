package app.util.feature;

import javax.json.JsonValue;
import javax.json.JsonObject;

public class RegexFeature implements Feature {
    private Integer id;
    private String name;
    private String description;
    private String granularity;
    private String group;
    private String contents;
    private String precondition;
    private String postcondition;
    private String type;
    
    public RegexFeature() {
       this.name = "";
       this.contents = "";
       this.description = "";
       this.granularity = "";
       this.group = "";
       this.precondition = "";
       this.postcondition = "";
       this.type = "";
    }

    public RegexFeature(String name, String group, String description, String contents, String granularity, String precondition, String postcondition) {
       this.name = name;
       this.contents = contents;
       this.description = description;
       this.granularity = granularity;
       this.group = group;
       this.precondition = precondition;
       this.postcondition = postcondition;
       this.type = "regex";
    }

    public void setId(Integer id) {
       this.id =id;
    }
    
    public Integer getId() {
       return id;
    }
    
    public void setDescription(String description) {
       this.description=description;
    }
    public String getDescription() {
       return description;
    }

    public void setName(String name) {
       this.name = name;
    }

    public String getName() {
       return name;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void fromJson(JsonValue jsonValue) {
      JsonObject jsonObject = jsonValue.asJsonObject();
      String id_str = jsonObject.getString("id");
      this.id = Integer.valueOf(id_str);
      this.name = jsonObject.getString("name");
      this.description = jsonObject.getString("description");
      this.granularity = jsonObject.getString("granularity");
      this.contents = jsonObject.getString("contents");
      this.group = jsonObject.getString("group");
      this.postcondition = jsonObject.getString("postcondition");
      this.precondition = jsonObject.getString("precondition");
      this.type = jsonObject.getString("type");
    }

    public String toJson() {
      String jsonString="{";
      jsonString = jsonString +"\"id\":\""+id+"\",\"name\":\""+name+"\",\"description\":\""+description+"\",\"granularity\":\""+granularity+"\",\"contents\":\""+contents+"\",\"precondition\":\""+precondition+"\",\"postcondition\":\""+postcondition+"\",\"type\":\""+type+"\"";
      jsonString = jsonString + "}";
      return jsonString;
    }
    
 }