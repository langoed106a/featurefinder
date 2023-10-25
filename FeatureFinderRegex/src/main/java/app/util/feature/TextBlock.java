package app.util.feature;

import java.util.List;

import app.util.feature.TextDocument;

public class TextBlock {
    private TextDocument textDocument;
    private TextBlockExpression textBlockExpression;

    public TextDocument getTextDocument() {
        return textDocument;
    }

    public void setTextDocument(TextDocument textDocument) {
        this.textDocument = textDocument;
    }

    public TextBlockExpression getTextBlockExpression() {
        return textBlockExpression;
    }

    public void setTextBlockExpression(TextBlockExpression textBlockExpression) {
        this.textBlockExpression = textBlockExpression;
    }

}