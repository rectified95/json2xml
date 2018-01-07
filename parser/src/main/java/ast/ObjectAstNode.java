package ast;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ObjectAstNode extends AstNode {
    private MembersAstNode members;

    public ObjectAstNode(MembersAstNode members) {
        this.members = members;
    }

    public MembersAstNode getMembers() {
        return members;
    }
    // moze list of pairnode??
}
