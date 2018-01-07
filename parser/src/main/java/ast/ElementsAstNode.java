package ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ElementsAstNode extends AstNode {
    private List<ValueAstNode> values;

    public ElementsAstNode(List<ValueAstNode> values) {
        this.values = values;
    }

    public List<ValueAstNode> getValues() {
        return values;
    }
}
