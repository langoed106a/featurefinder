package app.util.feature;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RegexResult {
	String textname;
	String regexname;
	Integer count;
	
	public RegexResult() {
		this.textname="";
		this.regexname="";
		this.count=0;
	}
	
	public RegexResult(String textName, String regexName, Integer count) {
		this.textname=textName;
		this.regexname=regexName;
		this.count=count;
	}

	public String getTextName() {
		return textname;
	}

	public void setTextName(String name) {
		this.textname = name;
	}

	public String getRegexName() {
		 return regexname;
	}

	public void setRegexName(String regex) {
		this.regexname = regex;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	 public void fromJson(String jsonStr) {
         RegexResult regexResult = null;
         try {
              regexResult = new ObjectMapper().readValue(jsonStr, RegexResult.class);
              this.setCount(regexResult.getCount());
              this.setRegexName(regexResult.getRegexName());
              this.setTextName(regexResult.getTextName());
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
