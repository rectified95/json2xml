package codegenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
@Getter
public class XmlLeafNode extends AbstractXmlNode {
    private String nodeContent;

    public XmlLeafNode(String nodeContent) {
        this.nodeContent = nodeContent;
        this.children = Collections.unmodifiableList(Collections.emptyList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        sb.append("<" + name + ">");
        sb.append(nodeContent);
        sb.append("</" + name + ">\n");
        return sb.toString();
    }
}
