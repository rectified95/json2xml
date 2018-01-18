package codegenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Igor Klemenski on 18.01.18.
 */
@Getter
@AllArgsConstructor
public class XmlLeafNode extends AbstractXmlNode {
    private String nodeContent;
}
