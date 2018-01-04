package tokenizer;

import matchers.Matcher;
import matchers.impl.KeywordMatcher;
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

    public Token getNextToken() {
        currentChar = source.getNextNonBlank();
        Matcher matcher = matcherMap.get(currentChar);
        if (matcher == null) {
            throw new RuntimeException("cannot recognize token - invalid input");
        }
        return matcher.match(source);
    }

    public String getNextTokenValue() {
        return getNextToken().getValue();
    }

    private void initializeMatcherMap() {
        Matcher specialCharacterMatcher = new SpecialCharacterMatcher();
        Matcher stringMatcher = new StringMatcher();
        Matcher numberMatcher = new NumberMatcher();
        Matcher keywordMatcher = new KeywordMatcher();

        matcherMap.put('"', stringMatcher);
        matcherMap.put('-', numberMatcher);
        for (int digit = 0; digit < 10; ++digit) {
            matcherMap.put(Character.forDigit(digit, 10), numberMatcher);
        }
        for (Character c : tokenMap.keySet()) {
            matcherMap.put(c, specialCharacterMatcher);
        }
        for (char c = 'A'; c < 'Z'; ++c) {
            matcherMap.put(c, keywordMatcher);
            matcherMap.put((char)(c + 32), keywordMatcher);
        }
    }

    public Source getSource() {
        return source;
    }
}
