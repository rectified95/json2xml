package codegenerator;

import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import lombok.extern.slf4j.Slf4j;
import parser.Parser;
import parser.ast.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
@Slf4j
public class XmlTreeGenerator {
    private static final String ARRAY_NAME_SUFFIX = "Array";
    private static final String SKIP_RULE_LABEL = "SKIP";
    private static final String ATTRIBUTE_RULE_LABEL = "ATTRIBUTE";

    public static AbstractXmlNode generate(ObjectAstNode jsonNode, String configString) {
        XmlNode node = (XmlNode) generateXmlTagFromJsonPair(new PairAstNode(
                new StringAstNode("root"), new ValueAstNode(jsonNode)), 0);
        node.setIndent(0);
        node.getChildren().stream().forEach(c -> c.setParent(node));
        node.setParent(null);
        ObjectAstNode configNode = new Parser(new Tokenizer(new StringSource(configString))).parse();
        applyConfiguration(node, configNode);
        return node;
    }

    private static AbstractXmlNode generateXmlTagFromJsonPair(PairAstNode jsonPair, int indent) {
        AbstractXmlNode xmlNode = generateXmlTagFromJsonValue(jsonPair.getValue(), jsonPair.getKey().getValue(), indent);
        if (xmlNode != null) {
            xmlNode.setName(jsonPair.getKey().getValue());
            xmlNode.setIndent(indent);
        }
        return xmlNode;
    }

    private static AbstractXmlNode generateXmlTagFromJsonValue(ValueAstNode astNode, String tagName, int indent) {
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

    private static <T extends AstNode> XmlLeafNode generateSimpleXmlTag(T astNode, int indent) {
        if (astNode instanceof StringAstNode) {
            return new XmlLeafNode(((StringAstNode) astNode).getValue());
        } else if (astNode instanceof NumberAstNode) {
            return new XmlLeafNode(((NumberAstNode) astNode).getValue());
        } else if (astNode instanceof KeywordAstNode) {
            return new XmlLeafNode(((KeywordAstNode) astNode).getValue().name().toLowerCase());
        }
        return null;
    }

    private static <T extends AstNode> XmlNode generateNestedXmlTag(T astNode, String tagName, int indent) {
        if (astNode instanceof ObjectAstNode) {
            XmlNode xmlNode = generateNestedXmlTagFromJsonObject((ObjectAstNode) astNode, indent);
            xmlNode.setName(tagName);
            xmlNode.setIndent(indent);
            return xmlNode;
        } else if (astNode instanceof ArrayAstNode) {
            XmlNode xmlNode = generateNestedXmlTagFromJsonArray((ArrayAstNode) astNode, tagName, indent);
            return xmlNode;
        }
        return null;
    }

    private static XmlNode generateNestedXmlTagFromJsonObject(ObjectAstNode jsonNode, int indent) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        for (PairAstNode jsonPair : jsonNode.getMembers()) {
            xmlChildren.add(generateXmlTagFromJsonPair(jsonPair, indent + 1));
        }
        return xmlNode;
    }

    private static XmlNode generateNestedXmlTagFromJsonArray(ArrayAstNode jsonNode, String tagName, int indent) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        List<ValueAstNode> jsonChildren = jsonNode.getChildren();
        for (ValueAstNode jsonValue: jsonChildren) {
            xmlChildren.add(generateXmlTagFromJsonValue(jsonValue, tagName, indent + 1));
        }
        xmlNode.getChildren().stream().forEach(n -> n.setName(tagName + ARRAY_NAME_SUFFIX));
        return xmlNode;
    }

    private static void applyConfiguration(XmlNode xmlTree, ObjectAstNode configNode) {
        for (PairAstNode pair : configNode.getMembers()) {
            AbstractXmlNode xmlNode = findXmlNode(xmlTree, pair.getKey().getValue().split("\\."));
            if (xmlNode == null) {
                log.warn("node not found, ignoring rule: " + pair.getKey().getValue()
                        + " " + ((StringAstNode) pair.getValue().getValue()).getValue());
                continue;
            }
            String ruleValue = ((StringAstNode) pair.getValue().getValue()).getValue();
            if (ruleValue.equals(SKIP_RULE_LABEL)) {
                xmlNode.getParent().getChildren().remove(xmlNode);
            } else if (ruleValue.equals(ATTRIBUTE_RULE_LABEL)) {
                if (xmlNode instanceof XmlLeafNode) {
                    xmlNode.getParent().getAttributes().add(new XmlAttribute(
                            xmlNode.getName(), ((XmlLeafNode) xmlNode).getNodeContent()));
                    xmlNode.getParent().getChildren().remove(xmlNode);
                } else {
                    log.warn("cannot use non-leaf node as tag attribute, ignoring rule: " + pair.getKey().getValue()
                            + " " + ((StringAstNode) pair.getValue().getValue()).getValue());
                }
            } else if (ruleValue.split(":")[0].equals("RENAME")) {
                xmlNode.setName(ruleValue.split(":")[1]);
            }
        }
    }

    private static AbstractXmlNode findXmlNode(AbstractXmlNode node, String[] nodePath) {
        if (nodePath.length == 1 && node.getName().equals(nodePath[0])) {
            return node;
        } else if (nodePath.length > 1) {
            if (node.getName().equals(nodePath[0])) {
                AbstractXmlNode xmlNode;
                for (AbstractXmlNode child : node.getChildren()) {
                    if ((xmlNode = findXmlNode(child, Arrays.copyOfRange(nodePath, 1, nodePath.length))) != null) {
                        return xmlNode;
                    }
                }
            }
        }
        return null;
    }
}
