package ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class MembersAstNode extends AstNode {
    private List<PairAstNode> members;

    public List<PairAstNode> getMembers() {
        return members;
    }

    public MembersAstNode(List<PairAstNode> pairNodes) {
        members = pairNodes;
    }
}
