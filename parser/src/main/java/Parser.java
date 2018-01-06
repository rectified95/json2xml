import source.impl.StringSource;
import tokenizer.token.TokenType;
import tokenizer.Tokenizer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class Parser {
    private static final List<Character> specialCharList =
            Arrays.asList('{', '}', '[', ']', ':', ',', '"');
    private static Tokenizer tokenizer;
    private static List<TokenType> tokenStream = new LinkedList<>();
    private static ListIterator<TokenType> curToken = tokenStream.listIterator();

    public Parser() {
        tokenizer = new Tokenizer(new StringSource(""));
    }

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void parse() {
        object();
    }

    protected boolean term(TokenType token) {
        TokenType nextTokenType;
        if (curToken.hasNext()) {
            nextTokenType = curToken.next();
        } else {
            nextTokenType = tokenizer.getNextToken().getType();
            curToken.add(nextTokenType);
        }
        return nextTokenType.equals(token);
    }

    protected boolean pair() {
        return term(TokenType.STRING) && term(TokenType.COLON) && value();
    }

    protected boolean value() {
        int nextIndexSave = curToken.nextIndex();

        List<Supplier<Boolean>> productionList = new LinkedList<>();
        productionList.add(() -> valueString());
        productionList.add(() -> valueObject());
        productionList.add(() -> valueArray());
        productionList.add(() -> valueNumber());
        productionList.add(() -> valueKeyword());

        for (Supplier<Boolean> s : productionList) {
            if (s.get()) {
                return true;
            } else {
                curToken = tokenStream.listIterator(nextIndexSave);
            }
        }
        return false;
    }

    protected boolean valueString() {
        return term(TokenType.STRING);
    }

    protected boolean valueNumber() {
        return term(TokenType.NUMBER);
    }

    protected boolean valueObject() {
        return object();
    }

    protected boolean valueArray() {
        return array();
    }

    protected boolean valueKeyword() {
        return term(TokenType.KEYWORD);
    }

    protected boolean elements() {
        int nextIndexSave = curToken.nextIndex();
        if (elementsValueElements()) {
            return true;
        } else {
            curToken = tokenStream.listIterator(nextIndexSave);
            if (elementsValue()) {
                return true;
            }
        }
        return false;
    }

    protected boolean elementsValue() {
        return value();
    }

    protected boolean elementsValueElements() {
        return value() && term(TokenType.COMMA) && elements();
    }

    protected boolean array() {
        int nextIndexSave = curToken.nextIndex();
        if (arrayEmpty()) {
            return true;
        } else {
            curToken = tokenStream.listIterator(nextIndexSave);
            if (arrayElements()) {
                return true;
            }
        }
        return false;
    }

    protected boolean arrayEmpty() {
        return term(TokenType.LBRACKET) && term(TokenType.RBRACKET);
    }

    protected boolean arrayElements() {
        return term(TokenType.LBRACKET) && elements() && term(TokenType.RBRACKET);
    }

    protected boolean object() {
        int nextIndexSave = curToken.nextIndex();
        if (objectEmpty()) {
            return true;
        } else {
            curToken = tokenStream.listIterator(nextIndexSave);
            if (objectMembers()) {
                return true;
            }
        }
        return false;
    }

    protected boolean objectEmpty() {
        return term(TokenType.LCURL) && term(TokenType.RCURL);
    }

    protected boolean objectMembers() {
        return term(TokenType.LCURL) && members() && term(TokenType.RCURL);
    }

    protected boolean members() {
        int nextIndexSave = curToken.nextIndex();
        if (membersPairMembers()) {
            return true;
        } else {
            curToken = tokenStream.listIterator(nextIndexSave);
            if (membersPair()) {
                return true;
            }
        }
        return false;
    }

    protected boolean membersPair() {
        return pair();
    }

    protected boolean membersPairMembers() {
        return pair() && term(TokenType.COMMA) && members();
    }
}