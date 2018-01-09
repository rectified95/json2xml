package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ValueAstNode<T extends AstNode> extends AstNode {
    private static final String nodeName = "VALUE";
    private T value;

    public ValueAstNode(T value) {
        this.value = value;
    }

    public ValueAstNode(T value, int level) {
        super(level);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
