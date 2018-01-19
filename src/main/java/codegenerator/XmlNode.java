package codegenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class XmlNode extends AbstractXmlNode {
    private List<AbstractXmlNode> children = new LinkedList<>();
    private List<XmlAttribute> attributes;
    private String name;
    private String value;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        // TODO add attribute rendering
        sb.append("<" + name + ">\n");
        for (AbstractXmlNode childNode : children) {
            sb.append(childNode.toString());
        }
        sb.append(printIndent());
        sb.append("</" + name + ">\n");
        return sb.toString();
    }
}
