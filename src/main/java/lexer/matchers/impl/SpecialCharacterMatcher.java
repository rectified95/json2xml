package lexer.matchers.impl;

import lexer.matchers.Matcher;
import lexer.source.Source;
import lexer.tokenizer.token.Token;
import lexer.tokenizer.Tokenizer;

/**
 * Created by igor on 10.12.17.
 */
public class SpecialCharacterMatcher implements Matcher {
    @Override
    public Token match(Source source) {
        return new Token(
                Tokenizer.tokenMap.get(source.getCurrent()),
                Character.toString(source.getCurrent())
        );
    }
}
