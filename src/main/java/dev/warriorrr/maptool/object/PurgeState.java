package dev.warriorrr.maptool.object;

import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.command.ViewCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurgeState {
    private final MapWrapper originalMap;
    private int purgeIndex = -1;
    private final List<Pair<Path, MapWrapper>> sameMaps = new ArrayList<>();

    public PurgeState(final MapWrapper originalMap, final Map<Path, MapWrapper> similar) {
        this.originalMap = originalMap;
        similar.forEach((path, map) -> sameMaps.add(Pair.pair(path, map)));
    }

    public MapWrapper originalMap() {
        return originalMap;
    }

    public int getPurgeIndex() {
        return purgeIndex;
    }

    public void setPurgeIndex(int purgeIndex) {
        this.purgeIndex = purgeIndex;
    }

    public List<Pair<Path, MapWrapper>> getSameMaps() {
        return sameMaps;
    }

    public void next(MapTool mapTool) {
        if (purgeIndex + 1 >= sameMaps.size()) {
            System.out.println("Finished purge");
            mapTool.setPurgeState(null);
            return;
        }

        final MapWrapper current = sameMaps.get(++purgeIndex).right();
        ViewCommand.printMap(current);

        System.out.println();
        System.out.println("Maps reviewed: " + (purgeIndex + 1) + " / " + sameMaps.size());
        System.out.println("Current map ID: " + current.id());
        System.out.println("Delete map? (y/n)");
    }

    public void input(final MapTool mapTool, final String[] input) {
        final Pair<Path, MapWrapper> next = sameMaps.get(purgeIndex);

        if (input[0].equalsIgnoreCase("y")) {
            System.out.println("Deleting " + next.right().fileName() + "...");

            try {
                Files.delete(next.left());
            } catch (IOException e) {
                System.out.println("An exception occurred when deleting " + next.right().fileName());
            }
            next(mapTool);
        } else if (input[0].equalsIgnoreCase("n")) {
            System.out.println("Skipping " + next.right().fileName() + "...");
            next(mapTool);
        } else {
            System.out.println("Unexpected input '" + input[0] + "', please enter either y or n.");
        }
    }
}
