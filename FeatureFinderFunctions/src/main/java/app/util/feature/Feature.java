package app.util.feature;

public interface Feature {
    public void setId(Integer id);
    public Integer getId();
    public void setDescription(String description);
    public String getDescription();
    public void setName(String name);
    public String getName();
    public void setType(String type);
    public String getType();
    public void setContents(String contents);
    public String getContents();
}