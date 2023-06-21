package dev.warriorrr.maptool.console;


import dev.warriorrr.maptool.MapTool;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Used in command classes to autocomplete map ids
 */
public class MapFileCompleter {
	private final MapTool mapTool;

	public MapFileCompleter(MapTool mapTool) {
		this.mapTool = mapTool;
	}

	public List<String> completions(String line) {
		try {
			return mapTool.mapDataFiles()
					.map(path -> path.getFileName().toString())
					.map(name -> name.replace("map_", "").replace(".dat", ""))
					.filter(id -> id.startsWith(line))
					.toList();
		} catch (IOException ignored) {}

		return Collections.emptyList();
	}
}
