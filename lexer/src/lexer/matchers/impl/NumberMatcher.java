package lexer.matchers.impl;

import lexer.Token;
import lexer.TokenType;
import lexer.matchers.Matcher;
import lexer.source.Source;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
public class NumberMatcher implements Matcher {
    @Override
    public Token match(Source source) {
        return new Token(TokenType.NUMBER, String.valueOf(0));
    }
}
