package parser.ast;

import java.util.List;

/**
 * Created by Igor Klemenski on 07.01.18.
 */
public class ObjectAstNode extends AstNode {
    private static final String nodeName = "OBJECT";
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        sb.append(nodeName);
        sb.append("\n");
        for (PairAstNode pair : members) {
            sb.append(pair.toString());
        }
        return sb.toString();
    }
}
