package runner;

import codegenerator.AbstractXmlNode;
import codegenerator.XmlTreeGenerator;
import lexer.source.impl.FileSource;
import lexer.tokenizer.Tokenizer;
import lombok.extern.slf4j.Slf4j;
import parser.Parser;
import parser.ast.ObjectAstNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
@Slf4j
public class ConversionRunner {
    public static void main(String[] args) {
        convertAndSendToFile(args[0], args[1], args[2]);
    }

    public static String convert(String srcFile, String configFile) {
        try {
            Tokenizer tokenizer = new Tokenizer(new FileSource(srcFile));
            Parser parser = new Parser(tokenizer);
            ObjectAstNode objectAstNode = parser.parse();
            String configString = new String(Files.readAllBytes(Paths.get(configFile)));
            AbstractXmlNode xmlNode = XmlTreeGenerator.generate(objectAstNode, configString);
            return xmlNode.toString();
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public static void convertAndSendToFile(String srcFile, String destFile, String configString) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(destFile))){
            bw.write(convert(srcFile, configString));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertAndSendToConsole(String srcFile, String configString) {
        System.out.println(convert(srcFile, configString));
    }
}
