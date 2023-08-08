package app.util.feature;

public class ListFeature implements Feature {
    private Integer id;
    private String name;
    private String description;
    private String contents;
    private String type;
    
    public ListFeature(String name) {
        this.name = name;
        this.type = "list";
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
    
}