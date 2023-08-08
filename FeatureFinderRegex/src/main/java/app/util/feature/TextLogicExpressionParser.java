package app.util.feature;

import app.util.feature.FeatureFunction;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.logic.Expression.Arg;
import edu.washington.cs.knowitall.logic.LogicExpressionParser;

public class TextLogicExpressionParser extends LogicExpressionParser<WordToken> {
     private FeatureFunction featureFunction;
     private TextBlock textBlock;

     public TextLogicExpressionParser(FeatureFunction featureFunction, TextBlock textBlock) {
        this.featureFunction = featureFunction;
        this.textBlock = textBlock;
    }

    public Arg<WordToken> factory(String str) {
        return new TextLogicBaseExpression(str, featureFunction, textBlock);
    }
}