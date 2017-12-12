package tokenizer;

import matchers.Matcher;
import matchers.impl.NumberMatcher;
import matchers.impl.SpecialCharacterMatcher;
import matchers.impl.StringMatcher;
import source.Source;

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
        while (source.hasNextNonBlank()) {
            tokenList.add(getNextToken());
        }
        return tokenList;
    }

    private Token getNextToken() {
        currentChar = source.getNextNonBlank();
        Matcher matcher = matcherMap.get(currentChar);
        if (matcher == null) {
            throw new RuntimeException("cannot recognize token - invalid input");
        }
        return matcher.match(source);
    }

    private void initializeMatcherMap() {
        Matcher specialCharacterMatcher = new SpecialCharacterMatcher();
        Matcher stringMatcher = new StringMatcher();
        Matcher numberMatcher = new NumberMatcher();

        matcherMap.put('"', stringMatcher);
        matcherMap.put('{', specialCharacterMatcher);
        matcherMap.put('}', specialCharacterMatcher);
        matcherMap.put('[', specialCharacterMatcher);
        matcherMap.put(']', specialCharacterMatcher);
        matcherMap.put(',', specialCharacterMatcher);
        matcherMap.put(':', specialCharacterMatcher);

        matcherMap.put('1', numberMatcher);
        matcherMap.put('2', numberMatcher);
        matcherMap.put('3', numberMatcher);
        matcherMap.put('4', numberMatcher);
        matcherMap.put('5', numberMatcher);
        matcherMap.put('6', numberMatcher);
        matcherMap.put('7', numberMatcher);
        matcherMap.put('8', numberMatcher);
        matcherMap.put('9', numberMatcher);
        matcherMap.put('-', numberMatcher);
    }

    public Source getSource() {
        return source;
    }
}
