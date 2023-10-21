package app.util.feature;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class WordToken {
    private String postag;
    private String token;
    private String lemma;
    private String dependency;
    private Integer index;
    private Integer sentence;
    private String spacingleft;
    private String wordtype;

    public WordToken() {
        this.token="";
        this.lemma="";
        this.postag="";
        this.dependency="";
        this.index=0;
        this.sentence=0;
        this.spacingleft="";
        this.wordtype="";
    }

    public WordToken(String token, String lemma, String postag, String dependency, Integer index, Integer sentence ) {
        this.token=token;
        this.lemma=lemma;
        this.postag=postag;
        this.dependency=dependency;
        this.index=index;
        this.sentence=sentence;
        this.spacingleft="";
        this.wordtype="";
    }

    public String getPostag() {
        return postag;
    }

    public void setPostag(String postag) {
        this.postag = postag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWordType() {
        return wordtype;
    }

    public void setWordType(String wordtype) {
        this.wordtype = wordtype;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getSpacingLeft() {
        return this.spacingleft;
    }

    public void setSpacingLeft(String spacingleft) {
        this.spacingleft = spacingleft;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSentence() {
        return sentence;
    }

    public void setSentence(Integer sentence) {
        this.sentence = sentence;
    }

    public String toString() {
        return ("token:"+token+" lemma:"+lemma+" postag:"+postag+" wordtype:"+wordtype+" dependency:"+dependency+" spacingleft:"+spacingleft+" index:"+index+" sentence:"+sentence);
    }

    public void fromJson(JSONObject jsonObject) {
        String strIndex="", strSentence="";
        this.token = (String) jsonObject.get("token");
        this.lemma = (String) jsonObject.get("lemma");
        this.postag = (String) jsonObject.get("postag");
        this.wordtype = (String) jsonObject.get("wordtype");
        this.dependency = (String) jsonObject.get("dependency");
        this.spacingleft = (String) jsonObject.get("spacingleft");
        if ((this.dependency==null) || (this.dependency.length()==0)) {
            this.dependency="notknown";
        }
        if (this.spacingleft==null) {
            this.spacingleft="";
        }
        strIndex = (String)jsonObject.get("index");
        this.index = Integer.valueOf(strIndex);
        strSentence = (String) jsonObject.get("sentence");
        this.sentence = Integer.valueOf(strSentence);
    }

    public WordToken clone() {
        WordToken cloneToken=null;
        cloneToken = new WordToken(this.token, this.lemma, this.postag, this.dependency, this.index, this.sentence );
        cloneToken.setWordType(this.wordtype);
        cloneToken.setSpacingLeft(this.spacingleft);
        return cloneToken;
    }

}