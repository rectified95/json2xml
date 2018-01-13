package parser.ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ArrayAstNode extends AstNode {
    private static final String nodeName = "ARRAY";
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        sb.append(nodeName);
        sb.append("\n");
        for (ValueAstNode v : elements) {
            sb.append(v.toString());
        }
        return sb.toString();
    }
}
