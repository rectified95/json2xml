package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class KeywordAstNode extends AstNode {
    private static final String nodeName = "KEYWORD";
    private Keyword value;

    public KeywordAstNode(Keyword value) {
        this.value = value;
    }

    public KeywordAstNode(Keyword value, int level) {
        super(level);
        this.value = value;
    }

    public Keyword getValue() {
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

    public enum Keyword {
        TRUE, FALSE, NULL;
    }
}
