package app.util.feature;

public class PartMatch {
   private Integer firstPos;
   private Integer secondPos;
   private String match;
   private Integer count;

    public PartMatch(Integer first, Integer second, String match) {
        this.firstPos = first;
        this.secondPos = second;
        this.match = match;
        this.count=0;
    }

    public Integer getFirst() {
        return firstPos;
    }

    public Integer getSecond() {
        return secondPos;
    }

    public String getMatch() {
        return match;
    }

    public Integer getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public Boolean theSame(String value) {
        Boolean same=false;
        if (this.match.equalsIgnoreCase(value)) {
            same = true;
        }
        return same;
    }

    public String toJson() {
       String jsonStr = "{\"firstid\":\""+firstPos+"\",\"secondId\":\""+secondPos+"\",\"match\":\""+match+"\",\"count\":\""+count+"\"}";
	   return jsonStr;						
    }

}