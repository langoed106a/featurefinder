package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;
import edu.washington.cs.knowitall.regex.RegularExpressionParser;

public class LowerCustomRegularExpressionParser extends RegularExpressionParser<WordToken> {
    private FeatureFunction featureFunction;
    private TextBlock textBlock;

    public LowerCustomRegularExpressionParser(FeatureFunction featureFunction, TextBlock textBlock) {
        this.featureFunction = featureFunction;
        this.textBlock = textBlock;
    }

    public LowerCustomRegularExpressionParser(RegexHandler regexHandler) {
    }

    public BaseExpression<WordToken> factory(String str) {
        return new LowerCustomRegularBaseExpression(str, featureFunction, textBlock);
    }

    public LowerCustomRegularExpression process(String string) {
       LowerCustomRegularExpression customLogicExpression =  new LowerCustomRegularExpression(string);
       customLogicExpression.setLogicExpression(this.apply(string));
       return customLogicExpression;
    }
}