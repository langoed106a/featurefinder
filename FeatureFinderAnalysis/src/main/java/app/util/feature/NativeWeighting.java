package app.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Formatter;

public class NativeWeighting {
    private Formatter formatter;
    private String[] ROUNDED_LIST={"comma","uniquewords","uniquecommon","uncommonwords","infinitive","mainverb","meansentencelength","passives","participle"};
    private List<String> featuresToDisplay;

    public NativeWeighting(String modelName, String fileLocation){
        ModelStore modelStore = new ModelStore(fileLocation);
        modelStore.loadModelList();
        JsonValue jsonValue = modelList.get(modelName);
        if (jsonValue != null) {
            model = new Model(modelName);
            model.fromJson(jsonValue);
        }
        featuresTDisplay = model.getFeatureList();
    }

    public StringBuffer getHeading(StringBuffer csvBuffer) {
        String featureName="";
        for (String name:featuresToDisplay) {
            featureName = name;
            csvBuffer.append(featureName+",");
        }
        return csvBuffer;
    }

    public List<String> getFeaturesToDisplay() {
        List<String> featuresToShow = featuresToDisplay;
        return featuresToShow;
    }

    public StringBuffer getOutputResults(StringBuffer csvBuffer, Map<String, String> featureResultList) {
        String resultStr="";
        for (String name:featuresToDisplay) {
            resultStr = featureResultList.get(name);
            if (resultStr==null) {
                resultStr = "0.0";
            }
            csvBuffer.append(resultStr+",");
        }
        return csvBuffer;
    }

    public Integer getStructuralAdjustment(Map<String, String> featureResults, List<String> adjustmentList) {
        Boolean words_alteration=false, s5_applied=false;
        Integer alterStructural = 0;
        String threshold="",appliedAdjustments="";
        String[] thresholds=null;
        Double value=0.0,upper=0.0,lower=0.0;
        Double mainVerb=0.0,infinitive=0.0,participle=0.0;
        Double pastTenseCount=0.0;
        Double commaCount=0.0,nonnativecommaCount=0.0;
        Double nomainverbCount=0.0;
        Double uniquewords=0.0,uniquewordsthreshold=0.0;
        Double uncommonwords=0.0, uncommonwordsthreshold=0.0;
        Double uniqueuncommonwords=0.0;
        Double pronouns=0.0,totalthreshold=0.0;
        Double complexsentences=0.0,complexsentencesthreshold=0.0;
        Double multiclause=0.0,multiclausethreshold=0.0;
        Double presentonly=0.0,presentonlythreshold=0.0;
        Double regularwords=0.0,reportedSpeechCheck=0.0,regularwordsthreshold=0.0;
        Double mupper=0.0, cupper=0.0;

        /* 1. If pronouns outside upperthreshold
         *    and there are more than 2s hits across
         *    uniquewords, uncommonwords and uniqueuncommonwords
         *    then deduct 1 from the structural count
         */
         
        pronouns =getFeatureValue(featureResults, "pronouns");
        threshold = this.getFunctionalThreshold("pronouns");
        thresholds = threshold.split(",");
        threshold=thresholds[1];
        value = Double.valueOf(threshold);
        if ((pronouns!=null) && ((pronouns>value))) {
            words_alteration = true;
        }
        if (words_alteration) {
            totalthreshold=0.0;
            uncommonwords=getFeatureValue(featureResults, "uncommonwords");
            if (uncommonwords!=null) {
                threshold = this.getStructuralThreshold("uncommonwords");
                thresholds = threshold.split(",");
                threshold = thresholds[0];
                lower = Double.valueOf(threshold);
                threshold = thresholds[1];
                upper = Double.valueOf(threshold);
                if ((uncommonwords<lower) || (uncommonwords>upper)) {
                    totalthreshold = totalthreshold + 1.0;
                }
            }

            uniquewords=getFeatureValue(featureResults, "uniquewords");
            if (uniquewords!=null) {
                threshold = this.getStructuralThreshold("uniquewords");
                thresholds = threshold.split(",");
                threshold = thresholds[0];
                lower = Double.valueOf(threshold);
                threshold = thresholds[1];
                upper = Double.valueOf(threshold);
                if ((uniquewords<lower) || (uniquewords>upper)) {
                    totalthreshold = totalthreshold + 1.0;
                }
            }

            uniqueuncommonwords=getFeatureValue(featureResults, "uniqueuncommonwords");
            if (uniqueuncommonwords!=null) {
                threshold = this.getStructuralThreshold("uniqueuncommonwords");
                thresholds = threshold.split(",");
                threshold = thresholds[0];
                lower = Double.valueOf(threshold);
                threshold = thresholds[1];
                upper = Double.valueOf(threshold);
                if ((uniqueuncommonwords<lower) || (uniqueuncommonwords>upper)) {
                    totalthreshold = totalthreshold + 1.0;
                }
            }
            if (totalthreshold>=2.0) {
                alterStructural = alterStructural+(-1);
                appliedAdjustments = appliedAdjustments+",S1(-1)";
            } else {
                appliedAdjustments = appliedAdjustments+",S1(0)";
            }

        }
        /*
         * 2. A relationship between the use of commas being used
         *    by a native and non-native person.
         */

        commaCount=this.getFeatureValue(featureResults, "comma");
        nonnativecommaCount=this.getFeatureValue(featureResults, "nonnativecomma");
        if ((commaCount!=null) && (nonnativecommaCount!=null) && (commaCount<1.0) && (nonnativecommaCount<1.0)) {
                alterStructural = alterStructural+(-1);
                appliedAdjustments = appliedAdjustments+",S2(-1)";
        } else {
                appliedAdjustments = appliedAdjustments+",S2(0)";
        }


        /*
         * 3. If mainverb is greater than 80 and infinitive and
         *    participle is not in the theshold then ensure that the
         *    penalty is not 2 but 1.
         */

        mainVerb=this.getFeatureValue(featureResults, "mainverb");
        infinitive=this.getFeatureValue(featureResults, "infinitive");
        participle=this.getFeatureValue(featureResults, "participle");
        nonnativecommaCount=this.getFeatureValue(featureResults, "nonnativecomma");
        if ((mainVerb!=null) && (mainVerb>80.0)) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("infinitive");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((infinitive<lower) || (infinitive>upper)) {
                totalthreshold = totalthreshold + 1.0;
            }
            threshold = this.getStructuralThreshold("participle");
            thresholds = threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((participle<lower) || (participle>upper)) {
                totalthreshold=totalthreshold+1.0;
            }
            if (totalthreshold>1.0) {
                alterStructural=alterStructural+(-1);
                appliedAdjustments=appliedAdjustments+",S3(-1)";
            } else {
                 appliedAdjustments=appliedAdjustments+",S3(0)";
            }
        }

 
        /*
         * 4. Adjustment for having the multipasttense value within
         *    the threshold.
         */

        pastTenseCount=getFeatureValue(featureResults, "multipasttense");
        if (pastTenseCount!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("multipasttense");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((pastTenseCount>=lower) && (pastTenseCount<=upper)) {
                alterStructural=alterStructural+(-1);
                appliedAdjustments=appliedAdjustments+",S4(-1)";
            } else {
                 appliedAdjustments=appliedAdjustments+",S4(0)";
            }
        }

       
        /*
         * 5. Using a multipasttense is a very English thing to do so we want
         *    to ensure that anyone who uses it will get a point but if
         *    you don't use it then you should be penalised.
         */

        pastTenseCount=getFeatureValue(featureResults, "multipasttense");
        if (pastTenseCount!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("multipasttense");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((pastTenseCount<lower) || (pastTenseCount>upper)) {
                alterStructural=alterStructural+(-1);
                appliedAdjustments=appliedAdjustments+",S5(-1)";
            } else {
                 appliedAdjustments=appliedAdjustments+",S5(0)";
            }
        }

        
        /*
         * 6. If an author is within the parameters of complex sentences, multiclause,
         *    present only and number of regular words, but is uncommon word,
         *    unique words, or the unique uncommon words, please can we
         *    minus 1 from the structural total to compensate.
         */

        complexsentences=getFeatureValue(featureResults, "complexsentences");
        if (complexsentences!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("complexsentences");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((complexsentences>=lower) || (complexsentences<=upper)) {
                complexsentencesthreshold=1.0;
            }
        }

        multiclause=getFeatureValue(featureResults, "multiclause");
        if (multiclause!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("multiclause");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((multiclause>=lower) || (multiclause<=upper)) {
                multiclausethreshold=1.0;
            }
        }

        presentonly=getFeatureValue(featureResults, "presentonly");
        if (presentonly!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("presentonly");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((presentonly>=lower) || (presentonly<=upper)) {
                presentonlythreshold=1.0;
            }
        }


        regularwords=getFeatureValue(featureResults, "numberregularword");
        if (regularwords!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("numberregularword");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((regularwords>=lower) && (regularwords<=upper)) {
                regularwordsthreshold=1.0;
            }
        }

       if ((complexsentencesthreshold>0.0) && (multiclausethreshold>0.0) && (presentonlythreshold>0.0) && (regularwordsthreshold>0.0)) {
            totalthreshold=0.0;
            uncommonwords = getFeatureValue(featureResults, "uncommonwords");
            if (uncommonwords!=null) {
                threshold = this.getStructuralThreshold("uncommonwords");
                thresholds=threshold.split(",");
                threshold = thresholds[0];
                lower = Double.valueOf(threshold);
                threshold = thresholds[1];
                upper = Double.valueOf(threshold);
                if ((uncommonwords<lower) || (uncommonwords>upper)) {
                    totalthreshold=totalthreshold + 1.0;
                }
            }
        }

        uniquewords=getFeatureValue(featureResults, "uniquewords");
        if (uniquewords!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("uniquewords");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((uniquewords<lower) && (uniquewords>upper)) {
                totalthreshold=totalthreshold + 1.0;
            }
        }
 

        uniqueuncommonwords=getFeatureValue(featureResults, "uniqueuncommon");
        if (uniqueuncommonwords!=null) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("uniqueuncommon");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            upper = Double.valueOf(threshold);
            if ((uniqueuncommonwords<lower) && (uniqueuncommonwords>upper)) {
                totalthreshold=totalthreshold + 1.0;
            }
        }
        if (totalthreshold>=2.0) {
            alterStructural=alterStructural + (-1);
            appliedAdjustments=appliedAdjustments + ",S6(-1)";
        } else {
            appliedAdjustments=appliedAdjustments + ",S6(0)";
        }

     
    /*
     * 7. Relationship between reportedspeech, multipasttense and multiclause where if there are more
     *    than 3 sentence in a piece of text don't penalise multitense or
     *    multi-clause if they are over the upper threshold.
     */

    reportedSpeechCheck=getFeatureValue(featureResults, "reportedspeechcheck");
    if ((reportedSpeechCheck!=null) && (reportedSpeechCheck>2.0)) {
            totalthreshold=0.0;
            threshold = this.getStructuralThreshold("multipasttense");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            mupper = Double.valueOf(threshold);
            threshold = this.getStructuralThreshold("multiclause");
            thresholds=threshold.split(",");
            threshold = thresholds[0];
            lower = Double.valueOf(threshold);
            threshold = thresholds[1];
            cupper = Double.valueOf(threshold);
            Double multiCount = getFeatureValue(featureResults, "multipasttense");
            Double clauseCount = getFeatureValue(featureResults, "multiclause");
            if ((multiCount!=null) && (multiCount>mupper)) {
                alterStructural=alterStructural+(-1);
                appliedAdjustments=appliedAdjustments+",S7(-1)";
            } else {
                 appliedAdjustments=appliedAdjustments+",S7(0)";
            }
            if ((clauseCount!=null) && (clauseCount>cupper)) {
                alterStructural = alterStructural + (-1);
                appliedAdjustments = appliedAdjustments + ",S7b(-1)";
            } else {
                appliedAdjustments = appliedAdjustments + ",S7b(0)";
            }
        adjustmentList.add(appliedAdjustments);
    }
    return alterStructural;
  }

  public Integer getFeatureValue(List<Integer> featureValueList, List<String> featurenameList, String featurename) {
    Integer value=0, index=0;
    Boolean found=false;
    String feature="";
    while ((!found) && (index<featurenameList.size())) {
        feature = featurenameList.get(index);
        if (feature.equalsIgnoreCase(featurename)) {
            value = featureValueList.get(index);
            found = true;
        }
        index++;
    }
    return value;
  }

  public Integer getFunctionalAdjustment(Map<String, String> percResultMap, List<String> adjustmentList) {
    Integer alterFunctional=0;
    Double whichCount, thatCount, aCount, pronounCount, infinitiveCount, theCount;
    Double theCountUpperThreshold, theCountLowerThreshold, aCountLowerThreshold, aCountUpperThreshold;
    Double toCount, forCount, upper=0.0, lower=0.0, lowerTo=0.0,upperTo=0.0, upperInfinitive=0.0, lowerFor=0.0, upperFor=0.0;
    String threshold="", appliedAdjustments="";
    String[] thresholds=null;

    /*
     * 1. Relationship between aCount and theCount
     */

    theCount = getFeatureValue(percResultMap, "theCount");
    aCount = getFeatureValue(percResultMap, "acount");
    threshold = this.getFunctionalThreshold("thecount");
    thresholds = threshold.split(",");
    threshold = thresholds[0];
    theCountLowerThreshold = Double.valueOf(threshold);
    threshold = thresholds[1];
    theCountUpperThreshold = Double.valueOf(threshold);
    threshold = this.getFunctionalThreshold("acount");
    thresholds = threshold.split(",");
    threshold = thresholds[1];
    aCountUpperThreshold = Double.valueOf(threshold);
    threshold = thresholds[0];
    aCountLowerThreshold = Double.valueOf(threshold);
    if ((aCount!=null) && (theCount!=null) && (theCount<theCountLowerThreshold) && (aCount>aCountUpperThreshold)) {
        alterFunctional=alterFunctional + (-2);
        appliedAdjustments = appliedAdjustments + ",F1(-2)";
    } else {
        if ((theCount!=null) && (aCount!=null) && (theCount!=null) && (theCount>theCountUpperThreshold) && (aCount<aCountLowerThreshold)) {
            alterFunctional = alterFunctional + (-2);
            appliedAdjustments = appliedAdjustments + ",F1(-2)";
        } else {
            appliedAdjustments = appliedAdjustments + ",F1(0)";
        }
    }

    /*
     * 2. Relationship between toCount and infinitiveCount
     */

    threshold = this.getFunctionalThreshold("tocount");
    thresholds = threshold.split(",");
    threshold = thresholds[0];
    lowerTo = Double.valueOf(threshold);
    threshold = thresholds[1];
    upperTo = Double.valueOf(threshold);
    threshold = this.getFunctionalThreshold("infinitive");
    thresholds = threshold.split(",");
    threshold = thresholds[1];
    upperInfinitive = Double.valueOf(threshold);
    toCount = getFeatureValue(percResultMap, "tocount");
    infinitiveCount = getFeatureValue(percResultMap, "infinitive");
    if ((toCount!=null) && (infinitiveCount!=null) && (toCount>upperTo) && (infinitiveCount>upperInfinitive)) {
        alterFunctional=alterFunctional + (-1);
        appliedAdjustments = appliedAdjustments + ",F2(-1)";
    } else {
            appliedAdjustments = appliedAdjustments + ",F2(0)";
    }

    /*
     * 3. Relationship between toCount and forCount within threshold
     */

    threshold = this.getFunctionalThreshold("forcount");
    thresholds = threshold.split(",");
    threshold = thresholds[0];
    lowerFor = Double.valueOf(threshold);
    threshold = thresholds[1];
    upperFor = Double.valueOf(threshold);
    toCount = getFeatureValue(percResultMap, "tocount");
    forCount = getFeatureValue(percResultMap, "forcount");
    if ((toCount!=null) && (forCount!=null) && (toCount>upperTo) && (forCount<lowerTo)) {
        alterFunctional=alterFunctional + (-1);
        appliedAdjustments = appliedAdjustments + ",F3(-1)";
    } else {
            appliedAdjustments = appliedAdjustments + ",F3(0)";
    }


    /*
     * 4. Relationship between toCount and forCount out of threshold
     */

    threshold = this.getFunctionalThreshold("forcount");
    thresholds = threshold.split(",");
    threshold = thresholds[0];
    lowerFor = Double.valueOf(threshold);
    threshold = thresholds[1];
    upperFor = Double.valueOf(threshold);
    toCount = getFeatureValue(percResultMap, "tocount");
    forCount = getFeatureValue(percResultMap, "forcount");
    if ((toCount!=null) && (forCount!=null) && (toCount<lowerTo) && (forCount>upperFor)) {
        alterFunctional=alterFunctional + (-1);
        appliedAdjustments = appliedAdjustments + ",F4(-1)";
    } else {
            appliedAdjustments = appliedAdjustments + ",F4(0)";
    }


    /*
     * 5. Relationship between pronounCount, theCount and aCount 
     */

    threshold = this.getFunctionalThreshold("pronouns");
    thresholds = threshold.split(",");
    threshold = thresholds[0];
    lower = Double.valueOf(threshold);
    threshold = thresholds[1];
    upper = Double.valueOf(threshold);
    pronounCount = getFeatureValue(percResultMap, "pronouns");
    if ((pronounCount!=null) && (pronounCount>upper)) {
        if ((aCount!=null) && (theCount!=null) && (aCount>=aCountLowerThreshold && aCount<=aCountUpperThreshold) && (theCount>=theCountLowerThreshold && theCount<=theCountUpperThreshold)) {
            alterFunctional=alterFunctional + (-2);
            appliedAdjustments = appliedAdjustments + ",F5(-2)";
        } else {
            appliedAdjustments = appliedAdjustments + ",F5(0)";
        }
    }
    adjustmentList.add(appliedAdjustments);
    return alterFunctional;
  }

  public String getLowerThresholdValues(Map<String, String> percResultMap) {
    String lowerThresholdStr = "Lower Threshold,";
    String threshold="";
    String[] parts=null;
    for (String name: featuresToDisplay) {
        threshold = this.getStructuralThreshold(name);
        if (threshold.length()>0) {
            parts = threshold.split(",");
            lowerThresholdStr = lowerThresholdStr + parts[0] +",";
        }
    }
    for (String fname:featuresToDisplay) {
        threshold = this.getFunctionalThreshold(fname);
        if (threshold.length()>0) {
            parts = threshold.split(",");
            lowerThresholdStr = lowerThresholdStr + parts[0] + ",";
        }
    }
    return lowerThresholdStr;
  }

  public String getUpperThresholdValues(Map<String, String> percResultMap) {
    String lowerThresholdStr = "Upper Threshold,";
    String threshold="";
    String[] parts=null;
    for (String name: featuresToDisplay) {
        threshold = this.getStructuralThreshold(name);
        if (threshold.length()>0) {
            parts = threshold.split(",");
            lowerThresholdStr = lowerThresholdStr + parts[1] +",";
        }
    }
    for (String fname:featuresToDisplay) {
        threshold = this.getFunctionalThreshold(fname);
        if (threshold.length()>0) {
            parts = threshold.split(",");
            lowerThresholdStr = lowerThresholdStr + parts[1] + ",";
        }
    }
    return lowerThresholdStr;
  }
  
  public Integer getFunctionalValue(String valueStr, String featureName) {
    Double lower=0.0, upper=0.0, value=0.0;
    Integer functionalValue=0;
    String[] parts=null;
    String threshold="", lowerStr="",upperStr="";
    threshold = this.getFunctionalThreshold(featureName);
    if (threshold.length()>0) {
        parts = threshold.split(",");
        lowerStr = parts[0];
        upperStr = parts[1];
        lower = Double.valueOf(lowerStr);
        upper = Double.valueOf(upperStr);
        if (valueStr.contains("(")) {
            valueStr =valueStr.substring(0,valueStr.length()-7);
        }
        value=Double.valueOf(valueStr);
        if ((value<lower) || (value>upper)) {
            functionalValue = 1;
        }
    }
    return functionalValue;
  }

   
  public Integer getStructuralValue(String valueStr, String featureName) {
    Double lower=0.0, upper=0.0, value=0.0;
    Integer structuralValue=0, position=0;
    String[] parts=null;
    String threshold="", lowerStr="",upperStr="";
    threshold = this.getStructuralThreshold(featureName);
    if (threshold.length()>0) {
        parts = threshold.split(",");
        lowerStr = parts[0];
        upperStr = parts[1];
        lower = Double.valueOf(lowerStr);
        upper = Double.valueOf(upperStr);
        if (valueStr.contains("(")) {
            position = valueStr.indexOf("(");
            threshold = valueStr.substring(position+1, valueStr.length()-1);
            parts = threshold.split(":");
            lowerStr = parts[0];
            upperStr = parts[1];
            lower = Double.valueOf(lowerStr);
            upper = Double.valueOf(upperStr);
            valueStr =valueStr.substring(0,position);
        }
        value=Double.valueOf(valueStr);
        if ((value<lower) || (value>upper)) {
            structuralValue = 1;
        }
    }
    return structuralValue;
  }

  private String getFunctionalThreshold(String featureName) {
    String threshold="";
    threshold = this.model.getThreshold("functional", featureName);
    threshold = threshold.trim();
    threshold = threshold.replace("\"","");
    return threshold;
  }

  private String getStructuralThreshold(String featureName) {
    String threshold="";
    threshold = this.model.getThreshold("structural", featureName);
    threshold = threshold.trim();
    threshold = threshold.replace("\"","");
    return threshold;
  }

  private Double getFeatureValue(Map<String, String> featureValueList, String featurename) {
    String valueStr = featureValueList.get(featurename);
    Integer position=0;
    Double result = null;
    if (valueStr!=null) {
        if (valueStr.contains("(")) {
            position = valueStr.indexOf("(");
            valueStr = valueStr.substring(0,position);
        }
        result = Double.valueOf(valueStr);
    }
    return result;
  }

  public String roundup(Double value, String format) {
    String defaultValue=String.valueOf(value);
    String revisedValue="";
    try {
        formatter = new Formatter();
        formatter.format(format, value);
        revisedValue = formatter.toString();
    } catch (Exception exception) {
        revisedValue = defaultValue;
    }
    return revisedValue;
  }
  
  public String getDecision(Integer functionalInt, Integer structuralInt) {
    String decision="";
    if ((functionalInt>=0) && (structuralInt>=-1) && (structuralInt<=3) && (functionalInt<=7)) {
        decision = "native";
    } else {
        decision = "nonnative";
    }
    return decision;
  }

  private boolean contains(List<String> list, String value) {
     String valueToSearch = value.toLowerCase();
     String valueStored="";
     boolean found = false;
     Integer index=0;
     while ((!found) && (index<list.size())) {
        valueStored = list.get(index);
        valueStored = valueStored.toLowerCase();
        if (valueStored.equalsIgnoreCase(valueToSearch)) {
            found = true;
        }
        index++;
     }
     return found;
  }

  private String getEnglishWeightForSmallFiles(Double value, Double lower, Double upper) {
    String result="-1";
    if ((value>=lower) && (value<=upper)) {
        result = "1";
    } else {
        result = "0";
    }
    return result;
  }

  public String getEnglshValueForSmallFiles(String header, Integer value, List<Integer> featurevalueList, List<String> featurenameList) {
    List<String> roundedList = Arrays.asList(ROUNDED_LIST);
    Double cell=0.0, pastparticiple=0.0,presentparticiple=0.0,passiveparticiple=0.0,divider=1.0;
    Double temp1, temp2, temp3,temp4,temp5,temp6;
    Double divisor=Double.valueOf(value);
    String str="",result="";
    String[] numbers=null;
    String appendix="";
    header = header.toLowerCase().trim();
    cell=divisor;
    switch(header){
        case "meansentencelength": {
            divisor = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (divisor/divider);
        }; break;
       case "uniquewords": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell=(100*(divisor/divider));
            if (divider>600) {
                appendix = "(28:55)";
            }
        }; break;

        case "misspeltcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "uncommonwords": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null || divider==0.0) {
                divider = 1.0;
            }
            cell=(100*(divisor/divider));
            if (divider>600) {
                appendix="(25:45)";
            } else if (divider>=450) {
                     appendix="";
            } else {
                     appendix="(30:45)";
            }
        }; break;
        case "reportedspeech": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "mainverb"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "numberregularword": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "uniqueuncommon": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
            if (divider>600) {
                appendix="(15:30)";
            } else if (divider>450) {
                appendix="";
            } else {
                appendix="(18:30)";
            }
        }; break;
        case "pronouns": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "passives": {
            temp1 = divisor;
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "allverbs"));
            divider = temp1 + temp2;
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "presentonly": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "singleverb": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "multitense": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "multiclause": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "multipasttense": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if (divider==null)  {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "infinitive": {
            temp1 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "mainverb"));
            temp2 = divisor;
            temp3 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "participle"));
            divider = temp1 + temp2 +temp3;
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "participle": {
            temp1 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "mainverb"));
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "infinitive"));
            temp3 = divisor;
            divider = temp1 + temp2 + temp3;
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "mainverb": {
            temp1 = divisor;
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "infinitive"));
            temp3 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "participle"));
            divider = temp1 +temp2 + temp3;
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "punctuations": {
            temp1 = divisor;
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "punctuationcount"));
            cell = temp2;
        }; break;
       case "complexsentences": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "sentencecount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
       case "impersonal": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "allverbs"));
            if ((divider==null) || (divider==0.0)) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "ofcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "suchcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "thecount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "thatcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "forcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "acount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "tocount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "incount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "atcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "fromcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "thiscount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "withcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "becausecount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "butcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "andcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "orcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "althoughcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "conjunctioncount": {
            temp1 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "andcount"));
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "becausecount"));
            temp3 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "butcount"));
            temp4 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "althoughcount"));
            temp5 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "orcount"));
            divisor = temp1 + temp2 + temp3 + temp4 +temp5;
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "whichcount": {
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "withfrominoncount": {
            temp1 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "withcount"));
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "fromcount"));
            temp3 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "incount"));
            temp4 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "oncount"));
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            divisor = temp1 + temp2 + temp3 + temp4;
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
        case "thatwhichcount": {
            temp1 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "withcount"));
            temp2 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "fromcount"));
            temp3 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "incount"));
            temp4 = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "oncount"));
            divider = Double.valueOf(this.getFeatureValue(featurevalueList, featurenameList, "wordcount"));
            divisor = temp1 + temp2 + temp3 + temp4;
            if (divider==null) {
                divider = 1.0;
            }
            cell = (100*(divisor/divider));
        }; break;
    }
    if (roundedList.contains(header)) {
        result = this.roundup(cell, "%3.2f");
    }
    if ((appendix!=null) && (appendix.length()>0)) {
        result = result+appendix;
    }
   return result;

  }

}