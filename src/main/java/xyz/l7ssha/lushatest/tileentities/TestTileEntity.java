package xyz.l7ssha.lushatest.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.energy.EnergyCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageComponentStackHandlerBuilder;
import xyz.l7ssha.lushatest.core.LushaComponentTickerBlockEntity;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;

public class TestTileEntity extends LushaComponentTickerBlockEntity<TestTileEntity> {
    public final static int ENERGY_STORAGE_MAX = 2_147_483_647; // Integer.MAX_VALUE;
    public final static int ENERGY_STORAGE_REQUIRED_TO_RUN = ENERGY_STORAGE_MAX / 3; // Integer.MAX_VALUE;

    public TestTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TEST_BLOCK_ENTITY.get(), pos, state);

        this.addComponent(new EnergyCapabilityComponent<TestTileEntity>(ENERGY_STORAGE_MAX));
        this.addComponent(
                new StorageCapabilityComponent(
                        new StorageComponentStackHandlerBuilder()
                                .setSize(2)
                                .addSlot(0, new StorageComponentStackHandlerBuilder.SlotConfigBuilder().setSlotLimit(1).setAllowExtract(false).setAllowInsert(false))
                                .addSlot(1, new StorageComponentStackHandlerBuilder.SlotConfigBuilder().setSlotLimit(64).setAllowExtract(true).setAllowInsert(false))
                                .build(),
                        this
                )
        );
    }

    public IEnergyStorage getEnergyStorage() {
        return this.getComponent(CapabilityEnergy.ENERGY).orElseThrow().getComponent();
    }

    public IItemHandler getStackHandler() {
        return this.getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow().getComponent();
    }

    @Override
    public void tick(@NotNull Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull TestTileEntity tile) {
        super.tick(world, blockPos, blockState, tile);

        if (world.isClientSide()) {
            return;
        }

        processMachine();
    }

    protected void processMachine() {
        final var storageComponent = (StorageCapabilityComponent) this.getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow();

        final var container = storageComponent.getAsSimpleContainer();

        assert level != null;
        final var recipe = level
                .getRecipeManager()
                .getRecipeFor(TestTileEntityRecipe.Type.INSTANCE, container, level);

        if (recipe.isEmpty()) {
            return;
        }

        if (!this.canProcess(recipe.get(), container)) {
            return;
        }

        this.getEnergyStorage().extractEnergy(recipe.get().getRecipeCost(), false);
        this.getStackHandler().insertItem(1, recipe.get().getResultItem(), false);
    }

    protected boolean canProcess(TestTileEntityRecipe recipe, SimpleContainer container) {
        final var inputSlotStack = container.getItem(0);
        final var outputSlotStack = container.getItem(1);

        final var energyStored = this.getEnergyStorage().getEnergyStored();

        return energyStored > recipe.getRecipeCost()
                && energyStored > ENERGY_STORAGE_MAX / 2
                && (inputSlotStack.getItem().equals(outputSlotStack.getItem()) || outputSlotStack.getItem().equals(Items.AIR))
                && inputSlotStack.getCount() > 0
                && outputSlotStack.getCount() < this.getStackHandler().getSlotLimit(1);
    }

    public int getCurrentProgressPercentage() {
        final var storageComponent = (StorageCapabilityComponent) this.getComponent(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow();

        final var container = storageComponent.getAsSimpleContainer();

        final var recipe = level
                .getRecipeManager()
                .getRecipeFor(TestTileEntityRecipe.Type.INSTANCE, container, level);

        if (recipe.isEmpty()) {
            return 0;
        }

        final var currentEnergy = Math.max(0, (this.getEnergyStorage().getEnergyStored() - ENERGY_STORAGE_REQUIRED_TO_RUN));

        final var calculatedPercentage = (int) ((double) currentEnergy / (double) recipe.get().getRecipeCost() * 100);
        return Math.min(100, calculatedPercentage);
    }
}
