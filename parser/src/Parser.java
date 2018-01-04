import tokenizer.TokenType;
import tokenizer.Tokenizer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class Parser {
    private static final List<Character> specialCharList =
            Arrays.asList(new Character[]{'{', '}', '[', ']', ':', ',', '"'});
    private static Tokenizer tokenizer;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    private boolean term(TokenType token) {
        return tokenizer.getNextToken().getType().equals(token);
    }

    private boolean pair() {
        return term(TokenType.STRING) && term(TokenType.COMMA) && value();
    }

    private boolean value() {
        return valueString() || valueNumber() || valueObject() || valueArray() || valueKeyword();
    }

    private boolean valueString() {
        return term(TokenType.STRING);
    }

    private boolean valueNumber() {
        return term(TokenType.NUMBER);
    }

    private boolean valueObject() {
        return object();
    }

    private boolean valueArray() {
        return array();
    }

    private boolean valueKeyword() {
        return term(TokenType.KEYWORD);
    }

    private boolean elements() {
        return elementsValue() || elementsValueElements();
    }

    private boolean elementsValue() {
        return value();
    }

    private boolean elementsValueElements() {
        return value() && term(TokenType.COMMA) && elements();
    }

    private boolean array() {
        return arrayEmpty() || arrayElements();
    }

    private boolean arrayEmpty() {
        return term(TokenType.LBRACKET) && term(TokenType.RBRACKET);
    }

    private boolean arrayElements() {
        return term(TokenType.LBRACKET) && elements() && term(TokenType.RBRACKET);
    }

    private boolean object() {
        return objectEmpty() || objectMembers();
    }

    private boolean objectEmpty() {
        return term(TokenType.LCURL) && term(TokenType.RCURL);
    }

    private boolean objectMembers() {
        return term(TokenType.LCURL) && members() && term(TokenType.RCURL);
    }

    private boolean members() {
        return membersPair() || membersPairMembers();
    }

    private boolean membersPair() {
        return pair();
    }

    private boolean membersPairMembers() {
        return pair() && term(TokenType.COMMA) && members();
    }
}
