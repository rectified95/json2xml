package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class KeywordAstNode extends AstNode {
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
    protected String print() {
        return null;
    }

    public enum Keyword {
        TRUE, FALSE, NULL;
    }
}
