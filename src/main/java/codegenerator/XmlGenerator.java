package codegenerator;

import parser.ast.*;

import java.util.List;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
public class XmlGenerator {
    private static final String ARRAY_NAME_SUFFIX = "Array";
    // TODO na to cale gowno z instanceof przydalby sie jakis wrapper dla kazdego noda z jednolitym interfejsem
    // TODO dodac wrapper 'root' - zrobic trik ze jest para root: object
    public AbstractXmlNode generate(ObjectAstNode jsonNode) {
        return generateXmlTagFromJsonPair(new PairAstNode(
                new StringAstNode("root"), new ValueAstNode(jsonNode)));
    }
    private AbstractXmlNode generateXmlTagFromJsonPair(PairAstNode jsonPair) {
        // TODO pustawiac name etc. uzyc astNode
        AstNode astNode = jsonPair.getValue().getValue();
        AbstractXmlNode xmlNode = generateXmlTagFromJsonValue(jsonPair.getValue(), jsonPair.getKey().getValue());
        if (xmlNode != null) {
            xmlNode.setName(jsonPair.getKey().getValue());
        }
        // NULLABLE
        return xmlNode;
    }

    private AbstractXmlNode generateXmlTagFromJsonValue(ValueAstNode astNode, String tagName) {
        XmlLeafNode leafNode = generateSimpleXmlTag(astNode.getValue());
        if (leafNode != null) {
            return leafNode;
        }
        XmlNode xmlNode = generateNestedXmlTag(astNode.getValue(), tagName);
        if (xmlNode != null) {
            return xmlNode;
        }
        return null;
    }

    private <T extends AstNode> XmlLeafNode generateSimpleXmlTag(T astNode) {
        if (astNode instanceof StringAstNode) {
            return new XmlLeafNode(((StringAstNode) astNode).getValue());
        } else if (astNode instanceof NumberAstNode) {
            return new XmlLeafNode(((NumberAstNode) astNode).getValue());
        } else if (astNode instanceof KeywordAstNode) {
            return new XmlLeafNode(((KeywordAstNode) astNode).getValue().name().toLowerCase());
        }
        return null;
    }

    private <T extends AstNode> XmlNode generateNestedXmlTag(T astNode, String tagName) {
        if (astNode instanceof ObjectAstNode) {
            XmlNode xmlNode = generateNestedXmlTagFromJsonObject((ObjectAstNode) astNode);
            xmlNode.setName(tagName);
            return xmlNode;
        } else if (astNode instanceof ArrayAstNode) {
            // TODO troche słabo ze nie moge mie listy tagow tutaj tylko owrapowane :/
            XmlNode xmlNode = generateNestedXmlTagFromJsonArray((ArrayAstNode) astNode, tagName);
            return xmlNode;
        }
        return null;
    }

    private XmlNode generateNestedXmlTagFromJsonObject(ObjectAstNode jsonNode) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        for (PairAstNode jsonPair : jsonNode.getMembers()) {
            xmlChildren.add(generateXmlTagFromJsonPair(jsonPair));
        }
        return xmlNode;
    }

    private XmlNode generateNestedXmlTagFromJsonArray(ArrayAstNode jsonNode, String tagName) {
        XmlNode xmlNode = new XmlNode();
        List<AbstractXmlNode> xmlChildren = xmlNode.getChildren();
        List<ValueAstNode> jsonChildren = jsonNode.getChildren();
        for (ValueAstNode jsonValue: jsonChildren) {
            xmlChildren.add(generateXmlTagFromJsonValue(jsonValue, tagName));
        }
        xmlNode.getChildren().stream().forEach(n -> n.setName(tagName + ARRAY_NAME_SUFFIX));
        return xmlNode;
    }
}
