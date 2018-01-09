import ast.*;
import exception.ParserException;
import source.impl.StringSource;
import tokenizer.Tokenizer;
import tokenizer.token.Token;
import tokenizer.token.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class Parser {
    private static final List<Character> specialCharList =
            Arrays.asList('{', '}', '[', ']', ':', ',', '"');
    private static Tokenizer tokenizer;
    private Token curToken;

    public Parser() {
        tokenizer = new Tokenizer(new StringSource(""));
    }

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public ObjectAstNode parse() {
        getNext();
        return parseObject(1);
    }

    protected boolean term(TokenType tokenType) {
        return curToken.getType().equals(tokenType);
    }

    protected ObjectAstNode parseObject(int level) {
        if (!term(TokenType.LCURL)) {
            error();
        }
        getNext();
        if (term(TokenType.RCURL)) {
            return new ObjectAstNode(Collections.emptyList(), level);
        }
        List<PairAstNode> members = parseMembers(level + 1);
        if (members == null) {
            error();
        }
        if (!term(TokenType.RCURL)) {
            error();
        }
        return new ObjectAstNode(members, level);
    }

    protected PairAstNode parsePair(int level) {
        if (!term(TokenType.STRING)) {
            error();
        }
        StringAstNode stringAstNode = new StringAstNode(curToken.getValue(), level + 1);
        getNext();
        if (!term(TokenType.COLON)) {
            error();
        }
        getNext();
        ValueAstNode valueAstNode = parseValue(level + 1);
        if (valueAstNode == null) {
            error();
        }
        return new PairAstNode(stringAstNode, valueAstNode, level);
    }

    protected ValueAstNode parseValue(int level) {
        if (term(TokenType.STRING)) {
            return new ValueAstNode(new StringAstNode(curToken.getValue(), level), level);
        } else if (term(TokenType.LBRACKET)) {
            ArrayAstNode arrayAstNode = parseArray(level);
            if (arrayAstNode == null) {
                error();
            }
            return new ValueAstNode(arrayAstNode);
        } else if (term(TokenType.NUMBER)) {
            NumberAstNode numberAstNode = new NumberAstNode(curToken.getValue(), level);
            return new ValueAstNode(numberAstNode);
        } else if(term(TokenType.LCURL)) {
            ObjectAstNode objectAstNode = parseObject(level);
            if (objectAstNode == null) {
                error();
            }
            return new ValueAstNode(objectAstNode, level);
        } else if (term(TokenType.KEYWORD)) {
            return new ValueAstNode(
                    new KeywordAstNode(KeywordAstNode.Keyword.valueOf(curToken.getValue().toUpperCase()), level)
            );
        }

        return null;
    }

    protected List<PairAstNode> parseMembers(int level) {
        List<PairAstNode> members = new LinkedList<>();
        PairAstNode pairNode = parsePair(level);
        if (pairNode == null) {
            error();
        }
        members.add(pairNode);
        getNext();
        if (term(TokenType.COMMA)) {
            getNext();
            List<PairAstNode> pairs = parseMembers(level);
            if (pairs == null) {
                error();
            }
            members.addAll(pairs);
        }
        return members;
    }

    protected List<ValueAstNode> parseElements(int level) {
        List<ValueAstNode> valueAstNodes = new LinkedList<>();
        ValueAstNode valueAstNode = parseValue(level);
        if (valueAstNode == null) {
            error();
        }
        valueAstNodes.add(valueAstNode);
        getNext();
        if (term(TokenType.COMMA)) {
            getNext();
            List<ValueAstNode> arrayElements = parseElements(level);
            if (arrayElements == null) {
                error();
            }
            valueAstNodes.addAll(arrayElements);
        }
        return valueAstNodes;
    }

    protected ArrayAstNode parseArray(int level) {
        getNext();
        List<ValueAstNode> elementsAstNode = parseElements(level + 1);
        if (elementsAstNode == null) {
            error();
        }
        if (!term(TokenType.RBRACKET)) {
            error();
        }
        return new ArrayAstNode(elementsAstNode, level);
    }

    public Token getCurToken() {
        return curToken;
    }

    protected void getNext() {
        curToken = tokenizer.getNextToken();
    }
    
    protected void error() {
        throw new ParserException("bad token: " + curToken + " on line " +
                tokenizer.getSource().getLineNumber() + ":" + tokenizer.getSource().getLineIdx());
    }
}
