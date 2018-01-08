package ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ObjectAstNode extends AstNode {
    private List<PairAstNode> members;

    public ObjectAstNode(List<PairAstNode> members) {
        this.members = members;
    }

    public ObjectAstNode(List<PairAstNode> members, int level) {
        super(level);
        this.members = members;
    }

    public List<PairAstNode> getMembers() {
        return members;
    }

    @Override
    protected String print() {
        return null;
    }
}
