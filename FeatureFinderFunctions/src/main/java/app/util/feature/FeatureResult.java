package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FeatureResult {

	@JsonProperty("sentencelist")
    private List<Sentence> sentencelist;

	@JsonProperty("matches")
	private List<Match> matches;

	public FeatureResult() {
		matches = new ArrayList<>();
		sentencelist = new ArrayList<>();
	}

    @JsonIgnore
	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	@JsonIgnore
	public List<Sentence> getSentenceList() {
		return sentencelist;
	}

	public void setSentenceList(List<Sentence> sentencelist) {
		this.sentencelist = sentencelist;
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

	public void fromJson(String jsonStr) {
         FeatureResult featureResult = null;
         try {
              featureResult = new ObjectMapper().readValue(jsonStr, FeatureResult.class);
              this.setMatches(featureResult.getMatches());
              this.setSentenceList(featureResult.getSentenceList());
         } catch (Exception exception) {
             exception.printStackTrace();
         }
    }
	
}