package dev.warriorrr.maptool.console;

import dev.warriorrr.maptool.MapTool;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Path;

public class MapToolConsole {
	private final MapTool mapTool;

	public MapToolConsole(MapTool mapTool) {
		this.mapTool = mapTool;
	}

	public void readCommands() {
		Terminal terminal;
		try {
			terminal = TerminalBuilder.builder().system(true).build();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		LineReader lineReader = LineReaderBuilder.builder()
				.terminal(terminal)
				.appName("map-tool")
				.completer(new CommandCompleter(mapTool))
				.variable(LineReader.HISTORY_FILE, Path.of(".command_history"))
				.build();

		try {
			while (true) {
				String line = lineReader.readLine("> ");
				if (line.isEmpty())
					continue;

				if ("quit".equalsIgnoreCase(line) || "exit".equalsIgnoreCase(line) || "bye".equalsIgnoreCase(line))
					break;

				mapTool.receiveCommand(line);
			}
		} catch (UserInterruptException | EndOfFileException ignored) {
			// Process was interrupted by user, likely ctrl+c, ignore exceptions & just exit.
		} catch (Exception e) {
			System.out.println("An exception occurred when reading input");
			e.printStackTrace();
		}
	}
}
