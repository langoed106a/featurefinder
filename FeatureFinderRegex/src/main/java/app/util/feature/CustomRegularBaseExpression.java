package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.logic.LogicExpression;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;

public class CustomRegularBaseExpression extends BaseExpression<WordToken> {
       private CustomLogicExpressionParser customLogicExpressionParser;
       private LogicExpression<WordToken> logicExpression;
       
  public CustomRegularBaseExpression(String regex, FeatureFunction featureFunction, TextBlock textBlock) {
        super(regex);
        customLogicExpressionParser = new CustomLogicExpressionParser(featureFunction, textBlock);
        logicExpression = customLogicExpressionParser.parse(regex);
  }
 
  public boolean apply(WordToken wordToken) {
        Boolean found = false;
        found = logicExpression.apply(wordToken);
        return found ;
  }
}