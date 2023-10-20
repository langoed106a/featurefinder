package app.util.feature;

import java.util.HashMap;
import java.util.Map;

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
	
}
