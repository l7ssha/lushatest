package xyz.l7ssha.lushatest.component.configuration.side;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import xyz.l7ssha.lushatest.component.ICompoundTaggable;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.core.LushaTestBlockEntity;
import xyz.l7ssha.lushatest.utils.TileEntityUpdatetableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SideAccessConfiguration implements ICompoundTaggable {
    private final static String SIDES_CONFIGURATION_KEY = "sides_configuration";

    private final Map<Direction, DirectionAccessConfiguration> sideConfiguration;

    public SideAccessConfiguration(Map<Direction, DirectionAccessConfiguration> sideConfiguration, LushaTestBlockEntity blockEntity) {
        this.sideConfiguration = new TileEntityUpdatetableMap<>(blockEntity);
        this.sideConfiguration.putAll(sideConfiguration);
    }

    public SideAccessConfiguration(CompoundTag tag, LushaTestBlockEntity blockEntity) {
        this(new HashMap<>(), blockEntity);

        load(tag);
    }

    public Map<Direction, DirectionAccessConfiguration> getSideConfiguration() {
        return sideConfiguration;
    }

    public DirectionAccessConfiguration getSideConfiguration(Direction direction) {
        return this.sideConfiguration.getOrDefault(direction, new DirectionAccessConfiguration(AccessModeConfig.NONE));
    }

    public Map<Direction, AccessModeConfig> getSideConfigurationDirectly() {
        return sideConfiguration.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entrySet -> entrySet.getValue().getMode()));
    }

    @Override
    public CompoundTag save() {
        final var sidesConfiguration = new CompoundTag();

        for (final var sideEntry : this.sideConfiguration.entrySet()) {
            sidesConfiguration.put(sideEntry.getKey().getName(), sideEntry.getValue().save());
        }

        final var outputTag = new CompoundTag();
        outputTag.put(SIDES_CONFIGURATION_KEY, sidesConfiguration);

        return outputTag;
    }

    @Override
    public void load(CompoundTag tag) {
        final var sidesTag = tag.getCompound(SIDES_CONFIGURATION_KEY);

        for (final var direction : Direction.values()) {
            this.sideConfiguration.put(direction, new DirectionAccessConfiguration(sidesTag.getCompound(direction.getName())));
        }
    }
}
