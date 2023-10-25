package app.util.feature;

public class Match {
    String start;
    String end;

    public Match() {
        start="";
        end="";
    }

    public void setMatch(String point) {
        Integer position=0;
        if ((point!=null) && (point.length()>0)) {
           position = point.indexOf(":");
           if (position>0) {
              start = point.substring(0,position);
              end = point.substring(position+1, point.length());
           }
        }
    }
}