package fr.practices.sed;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.Objects;

public class StreamEditor {
    public void transform(LineNumberReader lineNumberReader, Writer writer) throws IOException {
        Objects.requireNonNull(lineNumberReader, "Can't be null");
        Objects.requireNonNull(writer, "Can't accept null");
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            writer.append(line).append("\n");
        }
    }
}
