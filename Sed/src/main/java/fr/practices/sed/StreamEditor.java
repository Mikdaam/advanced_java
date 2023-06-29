package fr.practices.sed;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

public class StreamEditor {
    public enum Action { DELETE, PRINT }
    public interface Command {
        Action execute(int lineNo, String line);
    }
    public record LineDeleteCommand(int lineNumber) implements Command {
        public LineDeleteCommand {
            if (lineNumber < 0) {
                throw new IllegalArgumentException("Can't accept number < 1");
            }
        }

        @Override
        public Action execute(int lineNo, String line) {
            return lineNumber == lineNo ? Action.DELETE : Action.PRINT;
        }
    }
    public record FindAndDelete(Pattern pattern) implements Command {
        public FindAndDelete {
            Objects.requireNonNull(pattern);
        }
        @Override
        public Action execute(int lineNo, String line) {
            var matcher = pattern.matcher(line);
            return matcher.find() ? Action.DELETE : Action.PRINT;
        }
    }
    private final Command command;

    public StreamEditor() {
        this.command = new LineDeleteCommand(0);
    }

    public StreamEditor(Command command) {
        this.command = command;
    }

    public static LineDeleteCommand lineDelete(int lineNumber) {
        if (lineNumber < 1) {
            throw new IllegalArgumentException("Can't accept number < 1");
        }
        return new LineDeleteCommand(lineNumber);
    }

    public static FindAndDelete findAndDelete(Pattern pattern) {
        Objects.requireNonNull(pattern);
        return new FindAndDelete(pattern);
    }

    public void transform(LineNumberReader lineNumberReader, Writer writer) throws IOException {
        Objects.requireNonNull(lineNumberReader, "Can't be null");
        Objects.requireNonNull(writer, "Can't accept null");
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            var currentLineNo = lineNumberReader.getLineNumber();
            var action = command.execute(currentLineNo, line);
            if (action == Action.DELETE) {
                continue;
            }
            writer.append(line).append("\n");
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java StreamEditor <filename>");
            return;
        }

        try (
                var reader = Files.newBufferedReader(Path.of(args[0]));
                var lineNumberReader = new LineNumberReader(reader);
                var writer = new OutputStreamWriter(System.out)
        ) {
            new StreamEditor(new LineDeleteCommand(2)).transform(lineNumberReader,writer);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
