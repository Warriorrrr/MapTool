package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class LookupCommand extends Command {
	private final MapFileCompleter completer = new MapFileCompleter(mapTool);

	public LookupCommand(MapTool mapTool) {
		super(mapTool);
	}

	@Override
	public void dispatch(String[] args) {
		if (args.length == 0) {
			System.out.println("Not enough arguments! Usage: lookup <id>");
			return;
		}

		final String fileName = "map_" + args[0] + ".dat";
		final Path dotDatPath = mapTool.dataPath().resolve(fileName);
		if (!Files.exists(dotDatPath)) {
			System.out.println("Could not find a " + fileName + " file in the data folder.");
			return;
		}

		try {
			final CompoundTag tag = ((CompoundTag) NBTUtil.read(dotDatPath.toFile()).getTag()).getCompoundTag("data");

			System.out.println("--- " + fileName + " ---");
			System.out.println("Dimension: " + tag.getString("dimension"));
			System.out.println("Locked: " + (tag.getBoolean("locked") ? "yes" : "no"));
			System.out.println("Scale: " + tag.getByte("scale"));
			System.out.println("xCenter: " + tag.getInt("xCenter"));
			System.out.println("zCenter: " + tag.getInt("zCenter"));
			System.out.println();
		} catch (IOException e) {
			System.out.println("An exception occurred when reading map data file");
			e.printStackTrace();
		}
	}

	@Override
	public List<String> completions(List<String> args) {
		if (args.size() == 1)
			return completer.completions(args.get(0));
		else
			return Collections.emptyList();
	}
}
