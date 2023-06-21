package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapColor;
import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ViewCommand extends Command {
	private final MapFileCompleter completer = new MapFileCompleter(mapTool);

	public ViewCommand(MapTool mapTool) {
		super(mapTool);
	}

	@Override
	public void dispatch(String[] args) {
		if (args.length == 0) {
			System.out.println("Not enough arguments! Usage: view <id>");
			return;
		}

		try {
			final CompoundTag tag = mapTool.readMap(args[0]);

			final byte[] colors = tag.getByteArray("colors");
			final int width = 128;

			for (int heightOffset = 0; heightOffset < 128; heightOffset++) {
				for (int widthOffset = 0; widthOffset < 128; widthOffset++) {
					final byte colorId = colors[widthOffset + heightOffset * width];

					final Color color = MapColor.lookup(colorId);
					System.out.print("\033[38;2;" + color.getRed() + ";" + color.getGreen() + ";" + color.getBlue() + "m█");
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("An exception occurred when reading map data file");
			e.printStackTrace();
		} finally {
			// Reset colors
			System.out.print("\033[0m");
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
