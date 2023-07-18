package dev.warriorrr.maptool.object;

import net.querz.nbt.tag.CompoundTag;

/**
 * A wrapper for a map
 */
public class MapWrapper {
    private final int id;
    private final CompoundTag map;

    private MapWrapper(final int id, final CompoundTag map) {
        this.id = id;
        this.map = map;
    }

    public static MapWrapper map(final int id, final CompoundTag map) {
        return new MapWrapper(id, map);
    }

    public int id() {
        return this.id;
    }

    public String fileName() {
        return "map_" + id + ".dat";
    }

    public boolean isLocked() {
        return map.getBoolean("locked");
    }

    public String dimension() {
        return map.getString("dimension");
    }

    public int xCenter() {
        return map.getInt("xCenter");
    }

    public int zCenter() {
        return map.getInt("zCenter");
    }

    public int scale() {
        return map.getByte("scale");
    }

    public byte[] colors() {
        return map.getByteArray("colors");
    }

    public CompoundTag delegate() {
        return this.map;
    }
}
