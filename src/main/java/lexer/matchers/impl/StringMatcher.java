package lexer.matchers.impl;

import lexer.matchers.Matcher;
import lexer.source.Source;
import lexer.tokenizer.exception.TokenizerException;
import lexer.tokenizer.token.Token;
import lexer.tokenizer.token.TokenType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
@Slf4j
public class StringMatcher implements Matcher {
    private static final Set<Character> controlCharacterSet =
            new TreeSet<>(Arrays.asList(
                    '"', '\\', '/', 'b', 'f', 'n', 'r', 't')
            );

    @Override
    // TODO add hex lexing
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        try {
            char nextChar = source.getNext();
            while (nextChar != '"') {
                if (nextChar == '\\') {
                    sb.append(nextChar);
                    nextChar = source.getNext();
                    if (!controlCharacterSet.contains(nextChar)) {
                        throw new TokenizerException("invalid json string - missing control character after escape" +
                                        " '\\' character on line " + source.getLineNumber() + ":" + source.getLineIdx());
                    }
                }
                // escaping quotes handled here
                sb.append(nextChar);
                nextChar = source.getNext();
            }
        } catch (TokenizerException e) {
            log.error(e.getMessage());
        }
        return new Token(TokenType.STRING, sb.toString());
    }
}
