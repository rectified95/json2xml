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
//@RequiredArgsConstructor
public class XmlNode extends AbstractXmlNode {
    private List<AbstractXmlNode> children = new LinkedList<>();
    private List<XmlAttribute> attributes;
    private String name;
    private String value;
}
