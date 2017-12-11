package lexer.matchers.impl;

import lexer.Token;
import lexer.TokenType;
import lexer.matchers.Matcher;
import lexer.source.Source;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
// TODO ogarnac spacje po liczbach
public class NumberMatcher implements Matcher {
    @Override
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        short state = 0;
        char nextChar = source.getCurrent();
        boolean isCharKnown = true;
        boolean isStateFinal = false;

        while (isCharKnown) {
            switch (state) {
                case 0:
                    if (nextChar == '0') {
                        state = 1;
                        isStateFinal = true;
                        sb.append(nextChar);
                    } else if (Character.isDigit(nextChar)) {
                        state = 2;
                        isStateFinal = true;
                        sb.append(nextChar);
                    } else if (nextChar == '-'){
                        sb.append(nextChar);
                    } else {
                        isCharKnown = false;
                    }
                    break;
                case 1:
                    if (nextChar == '.') {
                        state = 3;
                        isStateFinal = false;
                        sb.append(nextChar);
                    } else {
                        isCharKnown = false;
                    }
                    break;
                case 2:
                    if (nextChar == '.') {
                        state = 3;
                        isStateFinal = false;
                        sb.append(nextChar);
                    } else if (nextChar == 'e' || nextChar == 'E') {
                        state = 5;
                        isStateFinal = false;
                        sb.append(nextChar);
                    } else if (!Character.isDigit(nextChar)) {
                        isCharKnown = false;
                    } else {
                        sb.append(nextChar);
                    }
                    break;
                case 3:
                    if (nextChar == 'e' || nextChar == 'E') {
                        state = 5;
                        isStateFinal = false;
                        sb.append(nextChar);
                    } else if (!Character.isDigit(nextChar)) {
                        isCharKnown = false;
                    } else {
                        state = 4;
                        isStateFinal = true;
                        sb.append(nextChar);
                    }
                    break;
                case 4:
                    if (nextChar == 'e' || nextChar == 'E') {
                        state = 5;
                        isStateFinal = false;
                        sb.append(nextChar);
                    } else if (!Character.isDigit(nextChar)) {
                        isCharKnown = false;
                    } else {
                        sb.append(nextChar);
                    }
                    break;
                    // TODO add case without +/-
                case 5:
                    if (nextChar == '+' || nextChar == '-') {
                        state = 6;
                        isStateFinal = false;
                        sb.append(nextChar);
                    } else {
                        isCharKnown = false;
                    }
                    break;
                case 6:
                    if (Character.isDigit(nextChar)) {
                        state = 7;
                        isStateFinal = true;
                        sb.append(nextChar);
                    } else {
                        isCharKnown = false;
                    }
                    break;
                case 7:
                    if (!Character.isDigit(nextChar)) {
                        isCharKnown = false;
                    } else {
                        sb.append(nextChar);
                    }
                    break;
            }
            if (isCharKnown) {
                if (source.hasNext()) {
                    nextChar = source.getNext();
                }
            } else {
                source.rollbackCursor();
                break;
            }
        }
        if (!isStateFinal) {
            throw new RuntimeException("incorrect JSON string - too short");
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }
}
