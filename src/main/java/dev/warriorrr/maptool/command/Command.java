package dev.warriorrr.maptool.command;

import dev.warriorrr.maptool.MapTool;

import java.util.Collections;
import java.util.List;

public abstract class Command {
	public final MapTool mapTool;

	public Command(MapTool mapTool) {
		this.mapTool = mapTool;
	}

	public abstract void dispatch(String[] args);

	public List<String> completions(List<String> args) {
		return Collections.emptyList();
	}
}
