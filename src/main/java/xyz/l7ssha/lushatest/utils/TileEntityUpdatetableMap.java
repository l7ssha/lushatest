package xyz.l7ssha.lushatest.utils;

import xyz.l7ssha.lushatest.core.LushaTestBlockEntity;

import java.util.HashMap;

public class TileEntityUpdatetableMap<K, V> extends HashMap<K, V> {
    private final LushaTestBlockEntity blockEntity;

    public TileEntityUpdatetableMap(LushaTestBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public V put(K key, V value) {
        final var returnValue = super.put(key, value);
        blockEntity.updateBlockEntity();
        return returnValue;
    }
}
