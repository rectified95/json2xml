package source;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
public interface Source {
    char getNext();
    char getNextNonBlank();
    char getCurrent();
    boolean hasNext();
    boolean hasNextNonBlank();
    void reverseCursor();
    void rollbackCursor();
    int getLineNumber();
    int getLineIdx();
}
