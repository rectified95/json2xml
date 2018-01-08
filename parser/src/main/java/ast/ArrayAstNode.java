package ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ArrayAstNode extends AstNode {
    private List<ValueAstNode> elements;

    public ArrayAstNode(List<ValueAstNode> children) {
        this.elements = children;
    }

    public ArrayAstNode(List<ValueAstNode> children, int level) {
        super(level);
        this.elements = children;
    }

    public List<ValueAstNode> getChildren() {
        return elements;
    }

    @Override
    protected String print() {
        return null;
    }
}
