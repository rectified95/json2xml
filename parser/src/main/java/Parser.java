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
        return parseObject();
    }

    protected boolean term(TokenType tokenType) {
        return curToken.getType().equals(tokenType);
    }

    protected ObjectAstNode parseObject() {
        if (!term(TokenType.LCURL)) {
            error();
        }
        getNext();
        if (term(TokenType.RCURL)) {
            return new ObjectAstNode(new MembersAstNode(Collections.emptyList()));
        }
        MembersAstNode members = parseMembers();
        if (members == null) {
            error();
        }
        if (!term(TokenType.RCURL)) {
            error();
        }
        return new ObjectAstNode(members);
    }

    protected PairAstNode parsePair() {
        if (!term(TokenType.STRING)) {
            error();
        }
        StringAstNode stringAstNode = new StringAstNode(curToken.getValue());
        getNext();
        if (!term(TokenType.COLON)) {
            error();
        }
        getNext();
        ValueAstNode valueAstNode = parseValue();
        if (valueAstNode == null) {
            error();
        }
        return new PairAstNode(stringAstNode, valueAstNode);
    }

    protected ValueAstNode parseValue() {
        if (term(TokenType.STRING)) {
            return new ValueAstNode(new StringAstNode(curToken.getValue()));
        } else if (term(TokenType.LBRACKET)) {
            ArrayAstNode arrayAstNode = parseArray();
            if (arrayAstNode == null) {
                error();
            }
            return new ValueAstNode(arrayAstNode);
        } else if (term(TokenType.NUMBER)) {
            NumberAstNode numberAstNode = new NumberAstNode(curToken.getValue());
            return new ValueAstNode(numberAstNode);
        } else if(term(TokenType.LCURL)) {
            ObjectAstNode objectAstNode = parseObject();
            if (objectAstNode == null) {
                error();
            }
            return new ValueAstNode(objectAstNode);
        } else if (term(TokenType.KEYWORD)) {
            return new ValueAstNode(
                    new KeywordAstNode(KeywordAstNode.Keyword.valueOf(curToken.getValue().toUpperCase()))
            );
        }

        return null;
    }

    protected MembersAstNode parseMembers() {
        List<PairAstNode> members = new LinkedList<>();
        PairAstNode pairNode = parsePair();
        if (pairNode == null) {
            error();
        }
        members.add(pairNode);
        getNext();
        if (term(TokenType.COMMA)) {
            getNext();
            MembersAstNode membersAstNode = parseMembers();
            if (membersAstNode == null) {
                error();
            }
            members.addAll(membersAstNode.getMembers());
        }
        return new MembersAstNode(members);
    }

    protected ElementsAstNode parseElements() {
        List<ValueAstNode> valueAstNodes = new LinkedList<>();
        ValueAstNode valueAstNode = parseValue();
        if (valueAstNode == null) {
            error();
        }
        valueAstNodes.add(valueAstNode);
        getNext();
        if (term(TokenType.COMMA)) {
            getNext();
            ElementsAstNode elementsAstNode = parseElements();
            if (elementsAstNode == null) {
                error();
            }
            valueAstNodes.addAll(elementsAstNode.getValues());
        }
        return new ElementsAstNode(valueAstNodes);
    }

    protected ArrayAstNode parseArray() {
        getNext();
        ElementsAstNode elementsAstNode = parseElements();
        if (elementsAstNode == null) {
            error();
        }
        if (!term(TokenType.RBRACKET)) {
            error();
        }
        return new ArrayAstNode(elementsAstNode);
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
