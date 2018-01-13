package lexer.source.impl;

import lexer.source.Source;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Igor Klemenski on 12.12.17.
 */
// TODO refactor into iterative buffered file reading
public class FileSource implements Source {
    private String fileString;
    private StringSource source;

    public FileSource(String filePath) throws IOException, URISyntaxException {
        source = new StringSource();
//        fileString = new String(Files.readAllBytes(Paths.get(new URI("file:///" + filePath))));
        fileString = new String(Files.readAllBytes(Paths.get(filePath)));
        source.setInputString(fileString);
    }

    @Override
    public char getNext() {
        return source.getNext();
    }

    @Override
    public char getNextNonBlank() {
        return source.getNextNonBlank();
    }

    @Override
    public char getCurrent() {
        return source.getCurrent();
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public boolean hasNextNonBlank() {
        return source.hasNextNonBlank();
    }

    @Override
    public void reverseCursor() {
        source.reverseCursor();
    }

    @Override
    public void rollbackCursor() {

    }

    @Override
    public int getLineNumber() {
        return source.getLineNumber();
    }

    @Override
    public int getLineIdx() {
        return source.getLineIdx();
    }
}
