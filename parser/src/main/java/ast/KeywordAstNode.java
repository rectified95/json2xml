package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class KeywordAstNode extends AstNode {
    private Keyword value;

    public KeywordAstNode(Keyword value) {
        this.value = value;
    }

    public Keyword getValue() {
        return value;
    }

    public enum Keyword {
        TRUE, FALSE, NULL;
    }
}
