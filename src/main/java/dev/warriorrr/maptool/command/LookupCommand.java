package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import dev.warriorrr.maptool.object.MapWrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
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

		try {
			final MapWrapper map = mapTool.readMap(args[0]);

			System.out.println("--- " + map.fileName() + " ---");
			System.out.println("Dimension: " + map.dimension());
			System.out.println("Locked: " + (map.isLocked() ? "yes" : "no"));
			System.out.println("Scale: " + map.scale());
			System.out.println("xCenter: " + map.xCenter());
			System.out.println("zCenter: " + map.zCenter());
			System.out.println();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
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
