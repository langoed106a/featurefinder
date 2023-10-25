package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.logic.LogicExpression;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;

public class LowerCustomRegularBaseExpression extends BaseExpression<WordToken> {
       private LowerCustomLogicExpressionParser lowerCustomLogicExpressionParser;
       private LogicExpression<WordToken> logicExpression;
       
  public LowerCustomRegularBaseExpression(String regex, FeatureFunction featureFunction, TextBlock textBlock) {
        super(regex);
        lowerCustomLogicExpressionParser = new LowerCustomLogicExpressionParser(featureFunction, textBlock);
        logicExpression = lowerCustomLogicExpressionParser.parse(regex);
  }
 
  public boolean apply(WordToken wordToken) {
        Boolean found = false;
        found = logicExpression.apply(wordToken);
        return found ;
  }
}