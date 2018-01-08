package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class StringAstNode extends AstNode {
    private String value;

    public StringAstNode(String value) {
        this.value = value;
    }

    public StringAstNode(String value, int level) {
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
