package app.util.feature;

import app.util.feature.Section;

public class TextBlock {
    private Section section;
    private TextBlockExpression textBlockExpression;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public TextBlockExpression getTextBlockExpression() {
        return textBlockExpression;
    }

    public void setTextBlockExpression(TextBlockExpression textBlockExpression) {
        this.textBlockExpression = textBlockExpression;
    }

}