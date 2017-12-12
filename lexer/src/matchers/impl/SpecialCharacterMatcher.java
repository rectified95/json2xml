package matchers.impl;

import matchers.Matcher;
import source.Source;
import tokenizer.Token;
import tokenizer.Tokenizer;

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
