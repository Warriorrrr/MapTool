package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapTool;
import dev.warriorrr.maptool.console.MapFileCompleter;
import dev.warriorrr.maptool.object.MapWrapper;
import dev.warriorrr.maptool.object.PurgeState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PurgeCommand extends Command {
    private final MapFileCompleter completer = new MapFileCompleter(mapTool);

    public PurgeCommand(MapTool mapTool) {
        super(mapTool);
    }

    @Override
    public void dispatch(String[] args) {
        if (args.length == 0) {
            System.out.println("Invalid usage: Usage: /purge <id>");
            return;
        }

        try {
            final MapWrapper map = mapTool.readMap(args[0]);

            System.out.println("Starting purge, checking for maps similar to " + map.id());
            final Map<Path, MapWrapper> same = SimilarCommand.findSimilarMaps(mapTool, map.xCenter(), map.zCenter());

            final PurgeState state = new PurgeState(map, same);
            mapTool.setPurgeState(state);
            state.next(mapTool);

        } catch (IOException e) {
            e.printStackTrace();
            return;
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
