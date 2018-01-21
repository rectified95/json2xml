package lexer.source.impl;

import lexer.source.Source;
import lexer.tokenizer.exception.TokenizerException;

/**
 * Created by Igor Klemenski on 09.12.17.
 */
public class StringSource implements Source {
    private int inputIdx = -1;
    private int lineNumber = 1;
    private int lineIdx = 1;
    private int curLen;

    private String inputString;
    public StringSource() { }

    public StringSource(String jsonString) {
        inputString = jsonString;
        curLen = inputString.length();
    }

    @Override
    public char getNext() {
        if (!hasNext()) {
            throw new TokenizerException("no more characters");
        }
        ++lineIdx;
        return inputString.charAt(++inputIdx);
    }

    @Override
    public char getNextNonBlank() {
        char next = getNext();
        while (next == ' ' || next == '\t' || next == '\n' || next == '\r') {
            if (next == '\n') {
                ++lineNumber;
                lineIdx = 1;
            }
            next = getNext();
        }
        return next;
    }

    @Override
    public char getCurrent() {
        return inputString.charAt(inputIdx);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineIdx() {
        return lineIdx;
    }

    @Override
    public boolean hasNext() {
        return (inputIdx + 1) < curLen;
    }

    public boolean hasNextNonBlank() {
        char next;
        int inputIdxSave = inputIdx;
        int lineIdxSave = lineIdx;
        while (hasNext()) {
            next = getNext();
            if (next != ' ' && next != '\t' && next != '\n' && next != '\r') {
                inputIdx = inputIdxSave;
                lineIdx = lineIdxSave;
                return true;
            }
        }
        inputIdx = inputIdxSave;
        lineIdx = lineIdxSave;
        return false;
    }

    @Override
    public void reverseCursor() {
        --this.inputIdx;
        if (inputString.charAt(inputIdx) == '\n') {
            --this.lineNumber;
        }
        --this.lineIdx;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
        this.curLen = inputString.length();
    }
}
