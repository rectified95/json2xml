package ast;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = treeDepth; i > 0; ++i) {
            sb.append(" ");
        }
        sb.append(print());
        for (int i = treeDepth; i > 0; ++i) {
            sb.append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }

    protected abstract String print();
}
