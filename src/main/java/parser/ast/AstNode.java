package parser.ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public abstract class AstNode {
    private int treeDepth;

    public AstNode() { }

    public AstNode(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public String printIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = treeDepth; i > 0; --i) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
