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
    public sealed interface Action {
        // Les lambdas sont utilises dans le cas ou le code est plus important que le nom
        enum DeleteAction implements Action { DELETE }
        record PrintAction(String text) implements Action {}
    }

    @FunctionalInterface
    public interface Command {
        Action execute(int lineNo, String line);

        default Command andThen(Command nextCommand) {
            Objects.requireNonNull(nextCommand);
            return (int lineNo, String line) -> {
                var prev = execute(lineNo, line);
                switch (prev) {
                    case Action.DeleteAction deleteAction -> { return deleteAction; }
                    case Action.PrintAction printAction -> line = printAction.text();
                }
                return nextCommand.execute(lineNo, line);
            };
        }
    }
    private final Command command;

    public StreamEditor() {
        this.command = lineDelete(0);
    }

    public StreamEditor(Command command) {
        this.command = command;
    }

    public static Command lineDelete(int lineNumber) {
        if (lineNumber < 0) {
            throw new IllegalArgumentException("Can't accept number < 1");
        }
        return (lineNo, line) -> lineNumber == lineNo ? Action.DeleteAction.DELETE : new Action.PrintAction(line);
    }

    public static Command findAndDelete(Pattern pattern) {
        Objects.requireNonNull(pattern);
        return (lineNo, line) -> {
            var matcher = pattern.matcher(line);
            return matcher.find() ? Action.DeleteAction.DELETE : new Action.PrintAction(line);
        };
    }

    public static Command substitute(Pattern pattern, String replacement) {
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(replacement);
        return (lineNo, line) -> {
            var matcher = pattern.matcher(line);
            return new Action.PrintAction(matcher.replaceAll(replacement));
        };
    }

    public void transform(LineNumberReader lineNumberReader, Writer writer) throws IOException {
        Objects.requireNonNull(lineNumberReader, "Can't be null");
        Objects.requireNonNull(writer, "Can't accept null");
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            var currentLineNo = lineNumberReader.getLineNumber();
            var action = command.execute(currentLineNo, line);
            switch (action) {
                case Action.DeleteAction ignored -> {}
                case Action.PrintAction printAction -> writer.append(printAction.text()).append("\n");
            }
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
            new StreamEditor(lineDelete(2)).transform(lineNumberReader,writer);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
