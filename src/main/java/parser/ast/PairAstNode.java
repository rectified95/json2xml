package parser.ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class PairAstNode extends AstNode {
    private static final String nodeName = "PAIR";
    StringAstNode key;
    ValueAstNode value;

    public PairAstNode(StringAstNode key, ValueAstNode value) {
        this.key = key;
        this.value = value;
    }

    public PairAstNode(StringAstNode key, ValueAstNode value, int level) {
        super(level);
        this.key = key;
        this.value = value;
    }

    public StringAstNode getKey() {
        return key;
    }

    public ValueAstNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        sb.append(nodeName);
        sb.append("\n");
        sb.append(key.toString());
        sb.append(value.toString());
        return sb.toString();
    }
}
