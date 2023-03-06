package fr.practices.sed;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.Objects;

public class StreamEditor {
    private final LineDeleteCommand command;
    public record LineDeleteCommand(int lineNumber) {
        public LineDeleteCommand {
            if (lineNumber < 0) {
                throw new IllegalArgumentException("Can't accept number < 1");
            }
        }
    }

    public StreamEditor() {
        this.command = new LineDeleteCommand(0);
    }

    public StreamEditor(LineDeleteCommand command) {
        this.command = command;
    }

    public static LineDeleteCommand lineDelete(int lineNumber) {
        if (lineNumber < 1) {
            throw new IllegalArgumentException("Can't accept number < 1");
        }
        return new LineDeleteCommand(lineNumber);
    }

    public void transform(LineNumberReader lineNumberReader, Writer writer) throws IOException {
        Objects.requireNonNull(lineNumberReader, "Can't be null");
        Objects.requireNonNull(writer, "Can't accept null");
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            var currentLineNo = lineNumberReader.getLineNumber();
            if (currentLineNo != command.lineNumber()) {
                writer.append(line).append("\n");
            }
        }
    }
}
