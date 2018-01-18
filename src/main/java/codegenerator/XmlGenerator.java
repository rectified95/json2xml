package codegenerator;

import parser.ast.*;

import java.util.List;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
public class XmlGenerator {
    private static final String ARRAY_NAME_SUFFIX = "Array";
    // TODO na to cale gowno z instanceof przydalby sie jakis wrapper dla kazdego noda z jednolitym interfejsem
    public AbstractXmlNode generate(ObjectAstNode jsonNode) {
        XmlNode node = (XmlNode) generateXmlTagFromJsonPair(new PairAstNode(
                new StringAstNode("root"), new ValueAstNode(jsonNode)), 0);
        node.setIndent(0);
        node.getChildren().stream().forEach(c -> c.setParent(node));
        node.setParent(null);
        return node;
    }

    private AbstractXmlNode generateXmlTagFromJsonPair(PairAstNode jsonPair, int indent) {
        // TODO poustawiac name etc.
        AbstractXmlNode xmlNode = generateXmlTagFromJsonValue(jsonPair.getValue(), jsonPair.getKey().getValue(), indent + 1);
        if (xmlNode != null) {
            xmlNode.setName(jsonPair.getKey().getValue());
        }
        // NULLABLE
        return xmlNode;
    }

    private AbstractXmlNode generateXmlTagFromJsonValue(ValueAstNode astNode, String tagName, int indent) {
        XmlLeafNode leafNode = generateSimpleXmlTag(astNode.getValue(), indent);
        if (leafNode != null) {
            leafNode.setName(tagName);
            return leafNode;
        }
        XmlNode xmlNode = generateNestedXmlTag(astNode.getValue(), tagName, indent);
        xmlNode.getChildren().stream().forEach(c -> c.setParent(xmlNode));
        if (xmlNode != null) {
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
            XmlNode xmlNode = generateNestedXmlTagFromJsonObject((ObjectAstNode) astNode, indent + 1);
            xmlNode.setName(tagName);
            return xmlNode;
        } else if (astNode instanceof ArrayAstNode) {
            // TODO troche słabo ze nie moge mie listy tagow tutaj tylko owrapowane :/
            XmlNode xmlNode = generateNestedXmlTagFromJsonArray((ArrayAstNode) astNode, tagName, indent + 1);
            return xmlNode;
        }
        return null;
    }

    private XmlNode generateNestedXmlTagFromJsonObject(ObjectAstNode jsonNode, int indent) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        for (PairAstNode jsonPair : jsonNode.getMembers()) {
            xmlChildren.add(generateXmlTagFromJsonPair(jsonPair, indent));
        }
        return xmlNode;
    }

    private XmlNode generateNestedXmlTagFromJsonArray(ArrayAstNode jsonNode, String tagName, int indent) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        List<ValueAstNode> jsonChildren = jsonNode.getChildren();
        for (ValueAstNode jsonValue: jsonChildren) {
            xmlChildren.add(generateXmlTagFromJsonValue(jsonValue, tagName, indent));
        }
        xmlNode.getChildren().stream().forEach(n -> n.setName(tagName + ARRAY_NAME_SUFFIX));
        return xmlNode;
    }
}
