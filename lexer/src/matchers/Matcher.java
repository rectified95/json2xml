package matchers;

import tokenizer.Token;
import source.Source;

/**
 * Created by igor on 10.12.17.
 */
public interface Matcher {
    Token match(Source source);
}