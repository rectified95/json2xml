package lexer.matchers.impl;

import lexer.matchers.Matcher;
import lexer.source.Source;
import lexer.tokenizer.token.Token;
import lexer.tokenizer.token.TokenType;
import lexer.tokenizer.exception.TokenizerException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class KeywordMatcher implements Matcher {
    private static final Set<String> keywords = new TreeSet<>(
            Arrays.asList("true", "false", "null"));
    @Override
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        char nextChar = source.getCurrent();
        while(Character.isLetter(nextChar)) {
            sb.append(nextChar);
            nextChar = source.getNext();
        }
        source.reverseCursor();
        String matchResult = sb.toString();
        if (!keywords.contains(matchResult)) {
            throw new TokenizerException("invalid input - cannot match keyword: " + matchResult);
        }
        return new Token(TokenType.KEYWORD, matchResult);
    }
}
