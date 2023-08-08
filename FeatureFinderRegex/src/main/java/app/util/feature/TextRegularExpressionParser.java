package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;
import edu.washington.cs.knowitall.regex.RegularExpressionParser;

public class TextRegularExpressionParser extends RegularExpressionParser<WordToken> {
    private FeatureFunction featureFunction;
    private TextBlock textBlock;

    public TextRegularExpressionParser(FeatureFunction featureFunction, TextBlock textBlock) {
        this.featureFunction = featureFunction;
        this.textBlock = textBlock;
    }

    public TextRegularExpressionParser(RegexHandler regexHandler) {
    }

    public BaseExpression<WordToken> factory(String str) {
        return new TextRegularBaseExpression(str, featureFunction, textBlock);
    }

    public TextRegularExpression process(String string) {
       TextRegularExpression customLogicExpression =  new TextRegularExpression(string);
       customLogicExpression.setLogicExpression(this.apply(string));
       return customLogicExpression;
    }
}