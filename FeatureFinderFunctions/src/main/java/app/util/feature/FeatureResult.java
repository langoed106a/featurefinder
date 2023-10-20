package app.util.feature;

import java.util.ArrayList;
import java.util.List;

public class FeatureResult {
	private List<RegexResult> resultList;

	public FeatureResult() {
		resultList = new ArrayList<>();
	}

	public List<RegexResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<RegexResult> resultList) {
		this.resultList = resultList;
	}
	
}