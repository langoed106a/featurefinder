package app.util.feature;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import app.util.feature.FeatureFunction;
import app.util.feature.Section;
import app.util.feature.General;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.logic.Expression.Arg;
import edu.washington.cs.knowitall.regex.Match;

public class CustomLogicBaseExpression extends TextLogicBaseExpression {
       
  public CustomLogicBaseExpression(String regex, FeatureFunction featureFunction, TextBlock textBlock) {
      super(regex, featureFunction, textBlock);  
  }
 
}

