import source.impl.StringSource;
import tokenizer.Tokenizer;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserWrapper {
    public static void main(String[] a) {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("\"positive\" : 766");
        Parser parser = new Parser(tokenizer);
        if (parser.pair()) {
            System.out.println("pair parser successfully");
        } else {
            System.out.println("fuckup alert");
        }
    }
}
