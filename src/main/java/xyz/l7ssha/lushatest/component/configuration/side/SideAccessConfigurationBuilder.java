package xyz.l7ssha.lushatest.component.configuration.side;

import net.minecraft.core.Direction;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.utils.IBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SideAccessConfigurationBuilder implements IBuilder<SideAccessConfiguration> {
    private final Map<Direction, DirectionAccessConfigurationBuilder> sideConfiguration;

    public SideAccessConfigurationBuilder() {
        this.sideConfiguration = new HashMap<>();
    }

    public SideAccessConfiguration build() {
        return new SideAccessConfiguration(
                sideConfiguration.entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build())
                )
        );
    }

    public SideAccessConfigurationBuilder addSide(Direction direction, DirectionAccessConfigurationBuilder configuration) {
        this.sideConfiguration.put(direction, configuration);

        return this;
    }

    public SideAccessConfigurationBuilder setCommonSideConfig(DirectionAccessConfigurationBuilder sideConfigBuilder) {
        for (final var side : Direction.values()) {
            this.sideConfiguration.put(side, sideConfigBuilder);
        }

        return this;
    }

    public static class DirectionAccessConfigurationBuilder implements IBuilder<DirectionAccessConfiguration> {
        private final AccessModeConfig mode;

        public DirectionAccessConfigurationBuilder(AccessModeConfig mode) {
            this.mode = mode;
        }

        @Override
        public DirectionAccessConfiguration build() {
            return new DirectionAccessConfiguration(this.mode);
        }
    }
}
