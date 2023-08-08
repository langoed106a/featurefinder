package app.util.feature;

public class Word {
    private String value;
    private Integer start;
    private Integer end;
    private String type;
    private Boolean newline;

    public Word(String value, Integer start, Integer end, String type) {
        this.value=value;
        this.start=start;
        this.end=end;
        this.type=type;
        this.newline=false;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public String getValue() {
        return this.value;
    }

    public void setBounds(Integer start, Integer end) {
        this.start=start;
        this.end=end;
    }

    public Integer getEnd() {
        return end;
    }

    public Integer getStart() {
        return start;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNewline() {
        this.newline=true;
    }

    public void unsetNewline() {
        this.newline=false;
    }

    public Boolean getNewline() {
        return newline;
    }

    public String toString() {
        return "Word:"+value+" Type:"+type+" Start:"+start+" End:"+end+" Newline:"+newline;
    }
}