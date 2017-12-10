package lexer.matchers.impl;

import lexer.Token;
import lexer.TokenType;
import lexer.matchers.Matcher;
import lexer.source.Source;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
public class StringMatcher implements Matcher {

    @Override
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        char nextChar = source.getNext();
        while (nextChar != '"') {
            if (nextChar == '\\') {
                sb.append(nextChar);
                nextChar = source.getNext();
            }
            // escaping quotes handled here
            sb.append(nextChar);
            nextChar = source.getNext();
        }
        return new Token(TokenType.STRING, sb.toString());
    }
}
