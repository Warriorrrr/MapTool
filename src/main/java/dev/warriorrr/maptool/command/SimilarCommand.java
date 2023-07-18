package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import dev.warriorrr.maptool.object.MapWrapper;
import net.querz.nbt.tag.CompoundTag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			final MapWrapper map = mapTool.readMap(args[0]);

			final Map<Path, MapWrapper> same = findSimilarMaps(mapTool, map.xCenter(), map.zCenter());

			if (same.isEmpty()) {
				System.out.println("This map does not have any similar maps");
			} else {
				System.out.println("The following maps share x and z centers:");

				for (Path path : same.keySet()) {
					System.out.println(" - " + path.getFileName().toString().replace("map_", "").replace(".dat", ""));
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

	public static Map<Path, MapWrapper> findSimilarMaps(MapTool mapTool, int xCenter, int zCenter) throws IOException {
		final Map<Path, MapWrapper> same = new HashMap<>();

		// Check all map data files to find ones with the same center
		for (Path path : mapTool.mapDataFiles().toList()) {
			final MapWrapper map = mapTool.readMap(path);
			if (map.xCenter() == xCenter && map.zCenter() == zCenter)
				same.put(path, map);
		}

		return same;
	}
}
