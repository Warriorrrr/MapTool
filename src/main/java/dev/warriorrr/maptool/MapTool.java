package dev.warriorrr.maptool;

import dev.warriorrr.maptool.command.Command;
import dev.warriorrr.maptool.command.ExportCommand;
import dev.warriorrr.maptool.command.LookupCommand;
import dev.warriorrr.maptool.command.PurgeCommand;
import dev.warriorrr.maptool.command.SimilarCommand;
import dev.warriorrr.maptool.command.ViewCommand;
import dev.warriorrr.maptool.object.MapWrapper;
import dev.warriorrr.maptool.object.PurgeState;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class MapTool {
	private final Map<String, Command> commands = new HashMap<>();
	private final Path dataPath;
	private PurgeState purgeState = null;

	public MapTool(final Path dataPath) {
		this.dataPath = dataPath;

		commands.put("lookup", new LookupCommand(this));
		commands.put("view", new ViewCommand(this));
		commands.put("export", new ExportCommand(this));
		commands.put("similar", new SimilarCommand(this));
		commands.put("purge", new PurgeCommand(this));
	}

	public Map<String, Command> commands() {
		return this.commands;
	}

	public void receiveCommand(String line) {
		final String[] args = line.split(" ");
		if (args.length == 0)
			return;

		if (this.purgeState != null) {
			this.purgeState.input(this, args);
			return;
		}

		final Command command = commands.get(args[0].toLowerCase(Locale.ROOT));
		if (command == null) {
			System.out.println("Unknown command: " + args[0]);
			return;
		}

		try {
			command.dispatch(Arrays.copyOfRange(args, 1, args.length));
		} catch (Exception e) {
			System.out.printf("Command '%s' threw exception when parsing command '%s'%n\n", args[0], Arrays.toString(args));
			e.printStackTrace();
		}
	}

	/**
	 * @return A stream of all the map_x.dat files.
	 */
	public Stream<Path> mapDataFiles() throws IOException {
		return Files.list(this.dataPath).filter(path -> {
			final String fileName = path.getFileName().toString();

			return fileName.startsWith("map_") && fileName.endsWith(".dat");
		});
	}

	public Path dataPath() {
		return this.dataPath;
	}

	public MapWrapper readMap(final String id) throws IOException, FileNotFoundException {
		final String fileName = "map_" + id + ".dat";
		final Path path = this.dataPath.resolve(fileName);

		return readMap(path);
	}

	public MapWrapper readMap(final Path path) throws IOException, FileNotFoundException {
		if (!Files.exists(path))
			throw new FileNotFoundException("Map " + path.getFileName() + " does not exist!");

		final String fileName = path.getFileName().toString();
		final int id = Integer.parseInt(fileName.replace("map_", "").replace(".dat", ""));

		return MapWrapper.map(id, ((CompoundTag) NBTUtil.read(path.toFile()).getTag()).getCompoundTag("data"));
	}

	public void setPurgeState(final PurgeState state) {
		this.purgeState = state;
	}
}
