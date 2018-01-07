package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ValueAstNode<T extends AstNode> extends AstNode {
    private T value;

    public ValueAstNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
