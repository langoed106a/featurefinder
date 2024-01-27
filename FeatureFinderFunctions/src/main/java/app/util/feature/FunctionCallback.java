package app.util.feature;

public interface FunctionCallback {
    public Boolean doFunction(String part, String valueType, String value, WordToken wordToken, TextDocument textDocument);
}