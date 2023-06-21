package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimilarCommand extends Command {
	private final MapFileCompleter completer = new MapFileCompleter(mapTool);

	public SimilarCommand(MapTool mapTool) {
		super(mapTool);
	}

	@Override
	public void dispatch(String[] args) {
		if (args.length == 0) {
			System.out.println("Not enough arguments! Usage: similar <id>");
			return;
		}

		try {
			final CompoundTag tag = mapTool.readMap(args[0]);

			final int xCenter = tag.getInt("xCenter");
			final int zCenter = tag.getInt("zCenter");

			final Set<String> same = new HashSet<>();

			// Check all map data files to find ones with the same center
			for (Path path : mapTool.mapDataFiles().toList()) {
				// Ignore our original map
				if (path.getFileName().toString().contains(args[0]))
					continue;

				final CompoundTag map = mapTool.readMap(path);
				if (map.getInt("xCenter") == xCenter && map.getInt("zCenter") == zCenter)
					same.add(path.getFileName().toString().replace("map_", "").replace(".dat", ""));
			}

			if (same.isEmpty()) {
				System.out.println("This map does not have any similar maps");
			} else {
				System.out.println("The following maps share x and z centers:");

				for (String id : same) {
					System.out.println(" - " + id);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("An IO exception occurred when checking similar maps");
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
