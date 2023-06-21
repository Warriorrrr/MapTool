package dev.warriorrr.maptool;

import dev.warriorrr.maptool.console.MapToolConsole;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.PathConverter;
import joptsimple.util.PathProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		final OptionParser parser = new OptionParser();

		parser.acceptsAll(Arrays.asList("datafolder"), "Path to the data folder")
				.withRequiredArg()
				.withValuesConvertedBy(new PathConverter(PathProperties.DIRECTORY_EXISTING))
				.defaultsTo(Path.of("data"));

		final OptionSet options;
		try {
			options = parser.parse(args);
		} catch (OptionException e) {
			System.err.println("An exception occurred while parsing options");
			e.printStackTrace();
			return;
		}

		Path dataFolder = null;
		if (!options.has("datafolder") || !Files.exists((Path) options.valueOf("datafolder"))) {
			System.out.println("Please enter the relative/absolute path for the servers data folder (typically located at serverroot/world/data)");
			System.out.println("Current working directory: " + System.getProperty("user.dir"));

			while (dataFolder == null || !Files.isDirectory(dataFolder)) {
				dataFolder = Paths.get(new Scanner(System.in).next());

				if (!Files.isDirectory(dataFolder))
					System.out.println("The specified path is not a directory, please try again.");
			}
		} else
			dataFolder = (Path) options.valueOf("datafolder");

		System.out.println("Using data folder: " + dataFolder.toAbsolutePath());

		final MapTool tool = new MapTool(dataFolder);

		new MapToolConsole(tool).readCommands(); // Read commands until termination
	}
}