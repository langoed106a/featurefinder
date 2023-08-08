package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;
import edu.washington.cs.knowitall.regex.RegularExpressionParser;

public class CustomRegularExpressionParser extends RegularExpressionParser<WordToken> {
    private FeatureFunction featureFunction;
    private TextBlock textBlock;

    public CustomRegularExpressionParser(FeatureFunction featureFunction, TextBlock textBlock) {
        this.featureFunction = featureFunction;
        this.textBlock = textBlock;
    }

    public CustomRegularExpressionParser(RegexHandler regexHandler) {
    }

    public BaseExpression<WordToken> factory(String str) {
        return new CustomRegularBaseExpression(str, featureFunction, textBlock);
    }

    public CustomRegularExpression process(String string) {
       CustomRegularExpression customLogicExpression =  new CustomRegularExpression(string);
       customLogicExpression.setLogicExpression(this.apply(string));
       return customLogicExpression;
    }
}