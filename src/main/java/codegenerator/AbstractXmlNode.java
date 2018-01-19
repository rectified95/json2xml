package codegenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractXmlNode {
    protected List<XmlAttribute> attributes;
    protected XmlNode parent;
    protected String name;
    protected int indent;

    protected String printIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; ++i) {
            sb.append("    ");
        }
        return sb.toString();
    }
}
