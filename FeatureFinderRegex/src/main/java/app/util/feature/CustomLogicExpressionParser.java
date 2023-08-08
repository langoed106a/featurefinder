package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.logic.Expression.Arg;
import edu.washington.cs.knowitall.logic.LogicExpressionParser;

public class CustomLogicExpressionParser extends LogicExpressionParser<WordToken> {
     private FeatureFunction featureFunction;
     private TextBlock textBlock;

     public CustomLogicExpressionParser(FeatureFunction featureFunction, TextBlock textBlock) {
        this.featureFunction = featureFunction;
        this.textBlock = textBlock;
    }

    public Arg<WordToken> factory(String str) {
        return new CustomLogicBaseExpression(str, featureFunction, textBlock);
    }
}