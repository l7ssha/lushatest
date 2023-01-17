package xyz.l7ssha.lushatest.component.configurator;

import mekanism.api.IConfigurable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.component.ICapabilityComponent;

public class ConfiguratorComponent implements ICapabilityComponent<IConfigurable> {
    public static Capability<IConfigurable> CONFIGURATOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    private final IConfigurable configurable;

    public ConfiguratorComponent(IConfigurable configurable) {
        this.configurable = configurable;
    }

    @Override
    public Capability<IConfigurable> getType() {
        return CONFIGURATOR_CAPABILITY;
    }

    @Override
    public LazyOptional<IConfigurable> getCapability(@Nullable Direction side) {
        return LazyOptional.of(() -> this.configurable);
    }

    @Override
    public IConfigurable getComponent() {
        return this.configurable;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {

    }

    @Override
    public void load(CompoundTag tag) {

    }
}
