package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class NumberAstNode extends AstNode {
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
    protected String print() {
        return null;
    }
}
