package lexer.matchers.impl;

import lexer.Token;
import lexer.TokenType;
import lexer.matchers.Matcher;
import lexer.source.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Igor Klemenski on 10.12.17.
 */

public class NumberMatcher implements Matcher {
    private List<Function> stateHandlerList = new ArrayList<>();

    public NumberMatcher() {
        initializeStateHandlerList();
    }

    @Override
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        NumberMatcherState matcherState = new NumberMatcherState(
                0, source.getCurrent(), true, false, sb
        );

        while (matcherState.isCharKnown()) {
            stateHandlerList.get(matcherState.getState()).apply(matcherState);
            if (matcherState.isCharKnown()) {
                if (source.hasNext()) {
                    matcherState.setNextChar(source.getNext());
                }
            } else {
                source.rollbackCursor();
                break;
            }
        }
        if (!matcherState.isStateFinal()) {
            throw new RuntimeException("incorrect JSON string - too short");
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    private void initializeStateHandlerList() {
        stateHandlerList.add(handleState0);
        stateHandlerList.add(handleState1);
        stateHandlerList.add(handleState2);
        stateHandlerList.add(handleState3);
        stateHandlerList.add(handleState4);
        stateHandlerList.add(handleState5);
        stateHandlerList.add(handleState6);
        stateHandlerList.add(handleState7);
    }

    private Function<NumberMatcherState, NumberMatcherState> handleState0 = s -> {
        char nextChar = s.getNextChar();
        if (s.getNextChar() == '0') {
            s.setState(1);
            s.setStateFinal(true);
            s.getSb().append(nextChar);
        } else if (Character.isDigit(nextChar)) {
            s.setState(2);
            s.setStateFinal(true);
            s.getSb().append(nextChar);
        } else if (nextChar == '-'){
            s.getSb().append(nextChar);
        } else {
            s.setCharKnown(false);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState1 = s -> {
        char nextChar = s.getNextChar();
        if (nextChar == '.') {
            s.setState(3);
            s.setStateFinal(false);
            s.getSb().append(nextChar);
        } else {
            s.setCharKnown(false);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState2 = s -> {
        char nextChar = s.getNextChar();
        if (nextChar == '.') {
            s.setState(3);
            s.setStateFinal(false);
            s.getSb().append(nextChar);
        } else if (nextChar == 'e' || nextChar == 'E') {
            s.setState(5);
            s.setStateFinal(false);
            s.getSb().append(nextChar);
        } else if (!Character.isDigit(nextChar)) {
            s.setCharKnown(false);
        } else {
            s.getSb().append(nextChar);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState3 = s -> {
        char nextChar = s.getNextChar();
        if (!Character.isDigit(nextChar)) {
            s.setCharKnown(false);
        } else {
            s.setState(4);
            s.setStateFinal(true);
            s.getSb().append(nextChar);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState4 = s -> {
        char nextChar = s.getNextChar();
        if (nextChar == 'e' || nextChar == 'E') {
            s.setState(5);
            s.setStateFinal(false);
            s.getSb().append(nextChar);
        } else if (!Character.isDigit(nextChar)) {
            s.setCharKnown(false);
        } else {
            s.getSb().append(nextChar);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState5 = s -> {
        char nextChar = s.getNextChar();
        if (nextChar == '+' || nextChar == '-') {
            s.setState(6);
            s.setStateFinal(false);
            s.getSb().append(nextChar);
        } else if (Character.isDigit(nextChar)) {
            s.setState(7);
            s.setStateFinal(true);
            s.getSb().append(nextChar);
        } else {
            s.setCharKnown(false);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState6 = s -> {
        char nextChar = s.getNextChar();
        if (Character.isDigit(nextChar)) {
            s.setState(7);
            s.setStateFinal(true);
            s.getSb().append(nextChar);
        } else {
            s.setCharKnown(false);
        }
        return s;
    };

    private Function<NumberMatcherState, NumberMatcherState> handleState7 = s -> {
        char nextChar = s.getNextChar();
        if (!Character.isDigit(nextChar)) {
            s.setCharKnown(false);
        } else {
            s.getSb().append(nextChar);
        }
        return s;
    };

    class NumberMatcherState {
        int state;
        char nextChar;
        boolean isCharKnown;
        boolean isStateFinal;
        StringBuilder sb;

        public NumberMatcherState(
                int state, char nextChar, boolean isCharKnown,
                boolean isStateFinal, StringBuilder sb
        ) {
            this.state = state;
            this.nextChar = nextChar;
            this.isCharKnown = isCharKnown;
            this.isStateFinal = isStateFinal;
            this.sb = sb;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public char getNextChar() {
            return nextChar;
        }

        public void setNextChar(char nextChar) {
            this.nextChar = nextChar;
        }

        public boolean isCharKnown() {
            return isCharKnown;
        }

        public void setCharKnown(boolean charKnown) {
            isCharKnown = charKnown;
        }

        public boolean isStateFinal() {
            return isStateFinal;
        }

        public void setStateFinal(boolean stateFinal) {
            isStateFinal = stateFinal;
        }

        public StringBuilder getSb() {
            return sb;
        }

        public void setSb(StringBuilder sb) {
            this.sb = sb;
        }
    }
}
