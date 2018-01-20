package codegenerator;

import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import parser.Parser;
import parser.ast.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
public class XmlTreeGenerator {
    private static final String ARRAY_NAME_SUFFIX = "Array";
    private XmlNode xmlTree;
    // TODO na to cale gowno z instanceof przydalby sie jakis wrapper dla kazdego noda z jednolitym interfejsem
    public AbstractXmlNode generate(ObjectAstNode jsonNode) {
        XmlNode node = (XmlNode) generateXmlTagFromJsonPair(new PairAstNode(
                new StringAstNode("root"), new ValueAstNode(jsonNode)), 0);
        node.setIndent(0);
        node.getChildren().stream().forEach(c -> c.setParent(node));
        node.setParent(null);
        this.xmlTree = node;
        applyConfiguration();
        return node;
    }

    private AbstractXmlNode generateXmlTagFromJsonPair(PairAstNode jsonPair, int indent) {
        AbstractXmlNode xmlNode = generateXmlTagFromJsonValue(jsonPair.getValue(), jsonPair.getKey().getValue(), indent);
        if (xmlNode != null) {
            xmlNode.setName(jsonPair.getKey().getValue());
            xmlNode.setIndent(indent);
        }
        // NULLABLE
        return xmlNode;
    }

    private AbstractXmlNode generateXmlTagFromJsonValue(ValueAstNode astNode, String tagName, int indent) {
        XmlLeafNode leafNode = generateSimpleXmlTag(astNode.getValue(), indent);
        if (leafNode != null) {
            leafNode.setName(tagName);
            leafNode.setIndent(indent);
            return leafNode;
        }
        XmlNode xmlNode = generateNestedXmlTag(astNode.getValue(), tagName, indent);
        xmlNode.getChildren().stream().forEach(c -> c.setParent(xmlNode));
        if (xmlNode != null) {
            xmlNode.setIndent(indent);
            return xmlNode;
        }
        return null;
    }

    private <T extends AstNode> XmlLeafNode generateSimpleXmlTag(T astNode, int indent) {
        if (astNode instanceof StringAstNode) {
            return new XmlLeafNode(((StringAstNode) astNode).getValue());
        } else if (astNode instanceof NumberAstNode) {
            return new XmlLeafNode(((NumberAstNode) astNode).getValue());
        } else if (astNode instanceof KeywordAstNode) {
            return new XmlLeafNode(((KeywordAstNode) astNode).getValue().name().toLowerCase());
        }
        return null;
    }

    private <T extends AstNode> XmlNode generateNestedXmlTag(T astNode, String tagName, int indent) {
        if (astNode instanceof ObjectAstNode) {
            XmlNode xmlNode = generateNestedXmlTagFromJsonObject((ObjectAstNode) astNode, indent);
            xmlNode.setName(tagName);
            xmlNode.setIndent(indent);
            return xmlNode;
        } else if (astNode instanceof ArrayAstNode) {
            // TODO troche słabo ze nie moge mie listy tagow tutaj tylko owrapowane :/
            XmlNode xmlNode = generateNestedXmlTagFromJsonArray((ArrayAstNode) astNode, tagName, indent);
            return xmlNode;
        }
        return null;
    }

    private XmlNode generateNestedXmlTagFromJsonObject(ObjectAstNode jsonNode, int indent) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        for (PairAstNode jsonPair : jsonNode.getMembers()) {
            xmlChildren.add(generateXmlTagFromJsonPair(jsonPair, indent + 1));
        }
        return xmlNode;
    }

    private XmlNode generateNestedXmlTagFromJsonArray(ArrayAstNode jsonNode, String tagName, int indent) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        List<ValueAstNode> jsonChildren = jsonNode.getChildren();
        for (ValueAstNode jsonValue: jsonChildren) {
            xmlChildren.add(generateXmlTagFromJsonValue(jsonValue, tagName, indent + 1));
        }
        xmlNode.getChildren().stream().forEach(n -> n.setName(tagName + ARRAY_NAME_SUFFIX));
        return xmlNode;
    }

    private void applyConfiguration() {
        String config = "{\"root.objectKey.a\":\"ATTRIBUTE\", \"root.objectKey.b\":\"ATTRIBUTE\", \"root.objectKey.XD\":\"SKIP\"}";
        ObjectAstNode configNode = new Parser(new Tokenizer(new StringSource(config))).parse();
        for (PairAstNode pair : configNode.getMembers()) {
            AbstractXmlNode xmlNode = findXmlNode(xmlTree, pair.getKey().getValue().split("\\."));
            if (xmlNode == null) {
                System.out.println("node not found, ignoring rule: " + pair.getKey().getValue()
                        + " " + ((StringAstNode) pair.getValue().getValue()).getValue());
                continue;
            }
            String ruleValue = ((StringAstNode) pair.getValue().getValue()).getValue();
            if (ruleValue.equals("SKIP")) {
                xmlNode.getParent().getChildren().remove(xmlNode);
            } else if (ruleValue.equals("ATTRIBUTE")) {
                if (xmlNode instanceof XmlLeafNode) {
                    xmlNode.getParent().getAttributes().add(new XmlAttribute(
                            xmlNode.getName(), ((XmlLeafNode) xmlNode).getNodeContent()));
                    xmlNode.getParent().getChildren().remove(xmlNode);
                } else {
                    System.out.println("cannot use non-leaf node as tag attribute, ignoring rule: " + pair.getKey().getValue()
                            + " " + ((StringAstNode) pair.getValue().getValue()).getValue());
                }
            }
        }
    }

    private AbstractXmlNode findXmlNode(AbstractXmlNode node, String[] nodePath) {
        // todo <~> check for nodePath being null or empty
        if (nodePath.length == 1) {
            if (node.getName().equals(nodePath[0])) {
                return node;
            } else {
                return null;
            }
        } else if (nodePath.length > 1) {
            if (node.getName().equals(nodePath[0])) {
                AbstractXmlNode xmlNode;
                for (AbstractXmlNode child : node.getChildren()) {
                    if ((xmlNode = findXmlNode(child, Arrays.copyOfRange(nodePath, 1, nodePath.length))) != null) {
                        return xmlNode;
                    }
                }
                return null;
            }
        }
        return null;
    }
}
