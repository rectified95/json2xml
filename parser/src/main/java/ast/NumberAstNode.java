package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class NumberAstNode extends AstNode {
    private static final String nodeName = "NUMBER";
    private String value;

    public NumberAstNode(String value) {
        this.value = value;
    }

    public NumberAstNode(String value, int level) {
        super(level);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        sb.append(nodeName);
        sb.append("\n");
        return sb.toString();
    }
}
