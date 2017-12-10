package lexer;

import lexer.matchers.Matcher;
import lexer.matchers.impl.SpecialCharacterMatcher;
import lexer.matchers.impl.StringMatcher;
import lexer.source.Source;

import java.util.*;

/**
 * Created by Igor Klemenski on 09.12.17.
 */

public class Tokenizer {
    public static final Map<Character, TokenType> tokenMap;
    private Map<Character, Matcher> matcherMap = new HashMap<>();
    private Source source;
    private char currentChar;

    static {
        Map<Character, TokenType> tempMap = new HashMap<>();
        tempMap.put('{', TokenType.LCURL);
        tempMap.put('}', TokenType.RCURL);
        tempMap.put('[', TokenType.LBRACKET);
        tempMap.put(']', TokenType.RBRACKET);
        tempMap.put(',', TokenType.COMMA);
        tempMap.put(':', TokenType.COLON);
        tokenMap = Collections.unmodifiableMap(tempMap);
    }

    public Tokenizer(Source source) {
        this.source = source;
        initializeMatcherMap();
    }

    public List<Token> tokenize() {
        List<Token> tokenList = new LinkedList<>();
        while (source.hasNext()) {
            tokenList.add(getNextToken());
        }
        return tokenList;
    }

    private Token getNextToken() {
        currentChar = source.getNextNonBlank();
        return matcherMap.get(currentChar).match(source);
    }

    private void initializeMatcherMap() {
        Matcher specialCharacterMatcher = new SpecialCharacterMatcher();
        Matcher stringMatcher = new StringMatcher();
        matcherMap.put('"', stringMatcher);
        matcherMap.put('{', specialCharacterMatcher);
        matcherMap.put('}', specialCharacterMatcher);
        matcherMap.put('[', specialCharacterMatcher);
        matcherMap.put(']', specialCharacterMatcher);
        matcherMap.put(',', specialCharacterMatcher);
        matcherMap.put(':', specialCharacterMatcher);
    }

    public Source getSource() {
        return source;
    }

    public void reset(Source source) {
        this.source = source;
    }
}
