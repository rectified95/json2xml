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

    @Override
    public char getNext() {
        return hasNext() ? inputString.charAt(++idx) : '\0';
    }

    @Override
    public char getNextNonBlank() {
        char next = getNext();
        while (next == ' ' || next == '\t' || next == '\n' || next == '\r') {
            next = getNext();
        }
        return next;
    }

    @Override
    public char getCurrent() {
        return inputString.charAt(idx);
    }

    @Override
    // TODO change this to account for trailing blanks eg. "{XXX}    " -> shoud return false after '}'
    public boolean hasNext() {
        return idx+1 < curLen;
    }

    @Override
    public void rollbackCursor() {
        --this.idx;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
        this.curLen = inputString.length();
    }

    protected void resetCursor() {
        this.idx = -1;
    }
}
