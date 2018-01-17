package renderer;

import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import parser.Parser;
import parser.ast.ArrayAstNode;
import parser.ast.AstNode;
import parser.ast.KeywordAstNode;
import parser.ast.NumberAstNode;
import parser.ast.ObjectAstNode;
import parser.ast.PairAstNode;
import parser.ast.StringAstNode;
import parser.ast.ValueAstNode;

import java.util.Arrays;
import java.util.Map;

// FIXME excessive indent for case array(X, ObjectAstNode)
public class XmlRenderer implements Renderer {
    // TODO move config to separate
    // TODO pass breadbcrumbs between calls
    private static final String configJsonString = "{\"root.objectKey.a\":\"SKIP\", \"a.b\":\"SKIP\"}";
    private Map<String, String> configMap ;
    private ObjectAstNode configNode = new Parser(new Tokenizer(new StringSource(configJsonString))).parse();

    public XmlRenderer() {
        int i=0;
    }

    public String renderWrapper(ObjectAstNode node) {
        StringBuilder sb = new StringBuilder();
        ObjectAstNode nodeWrapper = new ObjectAstNode(Arrays.asList(
                new PairAstNode(new StringAstNode("root", 0), new ValueAstNode(node, 0))
        ), 0);
        String breadcrumb = "";
        sb.append(render(nodeWrapper, breadcrumb));
        System.out.println(sb.toString());
        return sb.toString();
    }
    // TODO potem zrobic zeby do pliku szło
    public String render(ObjectAstNode node, String breadcrumb) {
        StringBuilder sb = new StringBuilder();
        for (PairAstNode pair : node.getMembers()) {
            StringAstNode configVal = getConfigVal(breadcrumb + pair.getKey().getValue());
            if (configVal != null) {
                if (configVal.getValue().equals("SKIP")) {
                    continue;
                }
            }
            if (pair.getValue().getValue() instanceof StringAstNode
                    || pair.getValue().getValue() instanceof NumberAstNode
                    || pair.getValue().getValue() instanceof KeywordAstNode) {
//                if ()
            }
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
                sb.append(render((ArrayAstNode) pair.getValue().getValue(), pair.getKey(), breadcrumb + pair
                        .getKey().getValue() + "."));
            } else {
                sb.append(render(pair.getValue().getValue(), breadcrumb + "." + pair.getKey().getValue()));
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

    public String render(ArrayAstNode node, StringAstNode key, String breadcrumb) {
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
            if (val.getValue() instanceof ArrayAstNode) {
                sb.append(render((ArrayAstNode) val.getValue(), key, breadcrumb));
            } else {
                sb.append(render(val.getValue(), breadcrumb));
            }
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

    public String render(StringAstNode node, String breadcrumb) {
        return node.getValue();
    }

    public String render(NumberAstNode node, String breadcrumb) {
        return node.getValue();
    }

    public String render(KeywordAstNode node, String breadcrumb) {
        return node.getValue().toString();
    }

    public <T extends AstNode> String render(T node, String breadcrumb) {
        if (node instanceof StringAstNode) {
            return render((StringAstNode) node, breadcrumb);
        } else if (node instanceof NumberAstNode) {
            return render((NumberAstNode) node, breadcrumb);
        } else if (node instanceof KeywordAstNode) {
            return render((KeywordAstNode) node, breadcrumb);
        } else if (node instanceof ObjectAstNode) {
            return render((ObjectAstNode) node, breadcrumb);
        }
        return "";
    }

    private StringAstNode getConfigVal(String breadcrumb) {
        for (PairAstNode pair : configNode.getMembers()) {
            // dziwne ze ten cast przechodzi chyba moze byc runtime error
            if (pair.getKey().getValue().equals(breadcrumb)) {
                return (StringAstNode) pair.getValue().getValue();
            }
        }
        return null;
    }
}
