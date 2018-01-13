package parser;

import lexer.tokenizer.Tokenizer;
import lexer.tokenizer.token.Token;
import lexer.tokenizer.token.TokenType;
import parser.ast.ArrayAstNode;
import parser.ast.KeywordAstNode;
import parser.ast.NumberAstNode;
import parser.ast.ObjectAstNode;
import parser.ast.PairAstNode;
import parser.ast.StringAstNode;
import parser.ast.ValueAstNode;
import parser.exception.ParserException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class Parser {
    private static Tokenizer tokenizer;
    private Token curToken;

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
            return null;
        }
        getNext();
        if (term(TokenType.RCURL)) {
            return new ObjectAstNode(Collections.emptyList(), level);
        }
        List<PairAstNode> members = parseMembers(level + 1);
        if (members == null || !term(TokenType.RCURL)) {
            error();
        }
        return new ObjectAstNode(members, level);
    }

    protected List<PairAstNode> parseMembers(int level) {
        List<PairAstNode> members = new LinkedList<>();
        PairAstNode pairNode = parsePair(level);
        if (pairNode == null) {
            error();
        }
        members.add(pairNode);
        getNext();
        while (term(TokenType.COMMA)) {
            getNext();
            PairAstNode pair = parsePair(level);
            if (pair == null) {
                error();
            }
            members.add(pair);
            getNext();
        }
        return members;
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
        }
        ArrayAstNode arrayAstNode = parseArray(level);
        if (arrayAstNode != null) {
            return new ValueAstNode(arrayAstNode);
        }

        if (term(TokenType.NUMBER)) {
            NumberAstNode numberAstNode = new NumberAstNode(curToken.getValue(), level);
            return new ValueAstNode(numberAstNode);
        }

        ObjectAstNode objectAstNode = parseObject(level);
        if (objectAstNode != null) {
            return new ValueAstNode(objectAstNode, level);
        }

        if (term(TokenType.KEYWORD)) {
            return new ValueAstNode(
                    new KeywordAstNode(KeywordAstNode.Keyword.valueOf(curToken.getValue().toUpperCase()), level)
            );
        }
        return null;
    }

    protected ArrayAstNode parseArray(int level) {
        if (!term(TokenType.LBRACKET)) {
            return null;
        }
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

    protected List<ValueAstNode> parseElements(int level) {
        List<ValueAstNode> valueAstNodes = new LinkedList<>();
        ValueAstNode valueAstNode = parseValue(level);
        if (valueAstNode == null) {
            error();
        }
        valueAstNodes.add(valueAstNode);
        getNext();
        while (term(TokenType.COMMA)) {
            getNext();
            ValueAstNode arrayValue = parseValue(level);
            if (arrayValue == null) {
                error();
            }
            valueAstNodes.add(arrayValue);
            getNext();
        }
        return valueAstNodes;
    }

    protected void getNext() {
        curToken = tokenizer.getNextToken();
    }

    protected void error() {
        throw new ParserException("bad token: " + curToken + " on line " +
                tokenizer.getSource().getLineNumber() + ":" + tokenizer.getSource().getLineIdx());
    }

    public Token getCurToken() {
        return curToken;
    }
}
