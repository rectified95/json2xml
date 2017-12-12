package source.impl;

import source.Source;

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
        if (!hasNext()) {
            throw new RuntimeException("no more characters");
        }
        return inputString.charAt(++idx);
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
    public boolean hasNext() {
        return (idx + 1) < curLen;
    }

    public boolean hasNextNonBlank() {
        char next;
        boolean hasNextNonBlank = false;
        while (hasNext()) {
            next = getNext();
            if (next != ' ' && next != '\t' && next != '\n' && next != '\r') {
                hasNextNonBlank = true;
                rollbackCursor();
                break;
            }
        }
        return hasNextNonBlank;
    }

    @Override
    public void rollbackCursor() {
        --this.idx;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
        this.curLen = inputString.length();
    }
}
