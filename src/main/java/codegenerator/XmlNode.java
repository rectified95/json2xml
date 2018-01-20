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
    private List<XmlAttribute> attributes = new LinkedList<>();
    private String name;
    private String value;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printIndent());
        sb.append("<" + name);
        for (XmlAttribute attribute : attributes) {
            sb.append(attribute.toString());
        }
        sb.append(">\n");
        for (AbstractXmlNode childNode : children) {
            sb.append(childNode.toString());
        }
        sb.append(printIndent());
        sb.append("</" + name + ">\n");
        return sb.toString();
    }
}
