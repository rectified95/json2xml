package lexer.source.impl;

import lexer.source.Source;

/**
 * Created by Igor Klemenski on 09.12.17.
 */
public class StringSource implements Source {
    private static int lastTokenReadIdx = -1;
    private static int idx = -1;

    private String inputString;
    private int curLen;

    public StringSource() { }

    public StringSource(String jsonString) {
        inputString = jsonString;
        curLen = inputString.length();
    }

    public char getNext() {
        return hasNext() ? inputString.charAt(++idx) : '\0';
    }

    public char getNextNonBlank() {
        char next = getNext();
        while (next == ' ' || next == '\t' || next == '\n' || next == '\r') {
            next = getNext();
        }
        return next;
    }

    public char getCurrent() {
        return inputString.charAt(idx);
    }

    public boolean hasNext() {
        return idx+1 < curLen;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
        this.curLen = inputString.length();
    }

    protected void resetCursor() {
        this.idx = -1;
    }
}
