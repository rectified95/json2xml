package lexer.matchers.impl;

import lexer.Token;
import lexer.Tokenizer;
import lexer.matchers.Matcher;
import lexer.source.Source;

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
