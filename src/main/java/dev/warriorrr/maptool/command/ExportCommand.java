package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapColor;
import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends Command {
	private final MapFileCompleter completer = new MapFileCompleter(mapTool);

	public ExportCommand(MapTool mapTool) {
		super(mapTool);
	}

	@Override
	public void dispatch(String[] args) {
		if (args.length == 0) {
			System.out.println("Not enough arguments! Usage: export <id>");
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

			final byte[] colors = tag.getByteArray("colors");
			final int width = 128;
			final BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);

			for (int heightOffset = 0; heightOffset < 128; heightOffset++) {
				for (int widthOffset = 0; widthOffset < 128; widthOffset++) {
					final byte colorId = colors[widthOffset + heightOffset * width];

					final Color color = MapColor.lookup(colorId);
					image.setRGB(widthOffset, heightOffset, color.getRGB());
				}
			}

			final Path target = Paths.get("map_" + args[0] + ".png");
			if (!Files.exists(target))
				Files.createFile(target);

			image.flush();
			ImageIO.write(image, "png", target.toFile());

			System.out.println("Successfully exported image to " + target.toAbsolutePath());
		} catch (IOException e) {
			System.out.println("An exception occurred when exporting map file");
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
