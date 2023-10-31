package app.util.feature;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Match {
    
    @JsonProperty("start")
    private String start;
    
    @JsonProperty("end")
    private String end;

    public Match() {
        start="";
        end="";
    }

    @JsonIgnore
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @JsonIgnore
    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setMatch(String point) {
        Integer position=0;
        if ((point!=null) && (point.length()>0)) {
           position = point.indexOf(":");
           if (position>0) {
              this.start = point.substring(0,position);
              this.end = point.substring(position+1, point.length());
           }
        }
    }
}