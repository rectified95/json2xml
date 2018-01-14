package renderer;

import parser.ast.ArrayAstNode;
import parser.ast.AstNode;
import parser.ast.KeywordAstNode;
import parser.ast.NumberAstNode;
import parser.ast.ObjectAstNode;
import parser.ast.PairAstNode;
import parser.ast.StringAstNode;
import parser.ast.ValueAstNode;

import java.util.Arrays;

public class XmlRenderer implements Renderer {

    public String renderWrapper(ObjectAstNode node) {
        StringBuilder sb = new StringBuilder();
        ObjectAstNode nodeWrapper = new ObjectAstNode(Arrays.asList(
                new PairAstNode(new StringAstNode("root", 0), new ValueAstNode(node, 0))
        ), 0);
        sb.append(render(nodeWrapper));
        System.out.println(sb.toString());
        return sb.toString();
    }
    // TODO potem zrobic zeby do pliku szło
    public String render(ObjectAstNode node) {
        StringBuilder sb = new StringBuilder();
        for (PairAstNode pair : node.getMembers()) {
            if (!(pair.getValue().getValue() instanceof ArrayAstNode)) {
                sb.append(pair.getKey().printIndent());
                sb.append("<");
                sb.append(pair.getKey().getValue());
                sb.append(">");
                if (pair.getValue().getValue() instanceof ObjectAstNode
                        || pair.getValue().getValue() instanceof ArrayAstNode) {
                    sb.append("\n");
                }
            }
            if (pair.getValue().getValue() instanceof ArrayAstNode) {
                sb.append(render((ArrayAstNode) pair.getValue().getValue(), pair.getKey()));
            } else {
                sb.append(render(pair.getValue().getValue()));
            }
            if (!(pair.getValue().getValue() instanceof ArrayAstNode)) {
                if (pair.getValue().getValue() instanceof ObjectAstNode
                        || pair.getValue().getValue() instanceof ArrayAstNode) {
                    sb.append(pair.getKey().printIndent());
                }
                sb.append("</");
                sb.append(pair.getKey().getValue());
                sb.append(">");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String render(ArrayAstNode node, StringAstNode key) {
        StringBuilder sb = new StringBuilder();
        for (ValueAstNode val : node.getChildren()) {
            sb.append(node.printIndent());
            sb.append("<");
            sb.append(key.getValue());
            sb.append(">");
            if (val.getValue() instanceof ObjectAstNode
                    || val.getValue() instanceof ArrayAstNode) {
                sb.append("\n");
            }
            sb.append(render(val.getValue()));
            if (val.getValue() instanceof ObjectAstNode
                    || val.getValue() instanceof ArrayAstNode) {
                sb.append(node.printIndent());
            }
            sb.append("</");
            sb.append(key.getValue());
            sb.append(">");
            sb.append("\n");
        }
        return sb.toString();
    }

    public String render(StringAstNode node) {
        return node.getValue();
    }

    public String render(NumberAstNode node) {
        return node.getValue();
    }

    public String render(KeywordAstNode node) {
        return node.getValue().toString();
    }

    public <T extends AstNode> String render(T node) {
        if (node instanceof StringAstNode) {
            return render((StringAstNode) node);
        } else if (node instanceof NumberAstNode) {
            return render((NumberAstNode) node);
        } else if (node instanceof KeywordAstNode) {
            return render((KeywordAstNode) node);
        } else if (node instanceof ObjectAstNode) {
            return render((ObjectAstNode) node);
        }
        return "";
    }
}
