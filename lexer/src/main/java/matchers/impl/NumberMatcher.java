package matchers.impl;

import matchers.Matcher;
import source.Source;
import tokenizer.exception.TokenizerException;
import tokenizer.token.Token;
import tokenizer.token.TokenType;

/**
 * Created by Igor Klemenski on 10.12.17.
 */

public class NumberMatcher implements Matcher {
    @Override
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        char nextChar = source.getCurrent();
        if (nextChar == '-') {
            sb.append(nextChar);
            nextChar = source.getNext();
        }
        if (nextChar == '0') {
            sb.append(nextChar);
            nextChar = source.getNext();
        } else if (Character.isDigit(nextChar)) {
            sb.append(nextChar);
            nextChar = source.getNext();
            while (Character.isDigit(nextChar)) {
                sb.append(nextChar);
                nextChar = source.getNext();
            }
        }
        if (nextChar == '.') {
            sb.append(nextChar);
            nextChar = source.getNext();
            if (!Character.isDigit(nextChar)) {
                throw new TokenizerException("incorrect JSON string at " + source.getLineNumber() +
                        ":" + source.getLineIdx());
            }
            while (Character.isDigit(nextChar)) {
                sb.append(nextChar);
                nextChar = source.getNext();
            }
        }
        if (nextChar == 'e' || nextChar == 'E') {
            sb.append(nextChar);
            nextChar = source.getNext();
            if (nextChar == '+' || nextChar == '-') {
                sb.append(nextChar);
                nextChar = source.getNext();
            } else if (!Character.isDigit(nextChar)) {
                throw new TokenizerException("incorrect JSON string at " + source.getLineNumber() +
                        ":" + source.getLineIdx());
            }
            while (Character.isDigit(nextChar)) {
                sb.append(nextChar);
                nextChar = source.getNext();
            }
        } else if (!Character.isDigit(nextChar)) {
            source.reverseCursor();
            return new Token(TokenType.NUMBER, sb.toString());
        }
        source.reverseCursor();
        return new Token(TokenType.NUMBER, sb.toString());
    }
}
