package app.util.feature;

import app.util.feature.WordToken;
import app.util.feature.Section;

public class TextBlockExpression {
    private Integer locationIndex;

    public TextBlockExpression() {
        this.locationIndex = 1;
    }

    public boolean apply(String part,String value,String valueType,WordToken wordToken,TextDocument textDocument) {
        boolean found=false;
        return found;
    }
}