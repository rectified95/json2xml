package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class PairAstNode extends AstNode {
    StringAstNode key;
    ValueAstNode value;

    public PairAstNode(StringAstNode key, ValueAstNode value) {
        this.key = key;
        this.value = value;
    }

    public StringAstNode getKey() {
        return key;
    }

    public ValueAstNode getValue() {
        return value;
    }
}
