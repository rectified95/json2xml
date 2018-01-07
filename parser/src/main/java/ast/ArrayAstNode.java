package ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ArrayAstNode extends AstNode {
    private ElementsAstNode children;

    public ArrayAstNode(ElementsAstNode children) {
        this.children = children;
    }

    public ElementsAstNode getChildren() {
        return children;
    }
}
