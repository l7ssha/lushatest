package xyz.l7ssha.lushatest.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.configuration.side.SideAccessConfigurationBuilder;
import xyz.l7ssha.lushatest.component.configuration.slot.SlotsConfigurationBuilder;
import xyz.l7ssha.lushatest.component.energy.EnergyCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;
import xyz.l7ssha.lushatest.container.data.TestBlockContainerData;
import xyz.l7ssha.lushatest.core.LushaComponentTickerBlockEntity;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;
import xyz.l7ssha.lushatest.registration.BlockEntityRegistry;

public class TestTileEntity extends LushaComponentTickerBlockEntity<TestTileEntity> implements MenuProvider, IActivableBlockEntity {
    public final static int ENERGY_STORAGE_MAX = 2_147_483_647; // Integer.MAX_VALUE;
    public final static int ENERGY_STORAGE_REQUIRED_TO_RUN = (int) (ENERGY_STORAGE_MAX * 0.5); // Integer.MAX_VALUE;
    private static final String CURRENT_PROGRESS_TAG = "current_progress";

    private boolean activeState = false;
    private int currentProgress = 0;

    public TestTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TEST_BLOCK_ENTITY.get(), pos, state);

        this.addComponent(
                new EnergyCapabilityComponent<TestTileEntity>(
                        ENERGY_STORAGE_MAX,
                        new SideAccessConfigurationBuilder().setCommonSideConfig(new SideAccessConfigurationBuilder.DirectionAccessConfigurationBuilder(AccessModeConfig.INPUT))
                                .build(this)
                )
        );
        this.addComponent(
                new StorageCapabilityComponent(
                        new SlotsConfigurationBuilder()
                                .setSize(2)
                                .addSlotConfiguration(0, new SlotsConfigurationBuilder.SlotAccessConfigurationBuilder(1, AccessModeConfig.NONE))
                                .addSlotConfiguration(0, new SlotsConfigurationBuilder.SlotAccessConfigurationBuilder(64, AccessModeConfig.OUTPUT))
                                .build(),
                        new SideAccessConfigurationBuilder().setCommonSideConfig(new SideAccessConfigurationBuilder.DirectionAccessConfigurationBuilder(AccessModeConfig.OUTPUT))
                                .build(this),
                        this
                )
        );
    }

    public void setActiveState(boolean activeState) {
        this.activeState = activeState;
        this.setChanged();
    }

    @Override
    public boolean getActiveState() {
        return this.activeState;
    }

    public IEnergyStorage getEnergyStorage() {
        return this.getRawComponent(ForgeCapabilities.ENERGY).orElseThrow().getComponent();
    }

    public IItemHandler getStackHandler() {
        return this.getRawComponent(ForgeCapabilities.ITEM_HANDLER).orElseThrow().getComponent();
    }

    public StorageCapabilityComponent getItemHandlerComponent() {
        return this.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER).orElseThrow();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        this.currentProgress = tag.getInt(CURRENT_PROGRESS_TAG);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt(CURRENT_PROGRESS_TAG, this.currentProgress);
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
        if (!activeState) {
            return;
        }

        final var storageComponent = this.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER).orElseThrow();
        final var container = storageComponent.getAsSimpleContainer();
        if (!canProcess(container)) {
            this.currentProgress = 0;
            return;
        }

        if (this.getEnergyStorage().getEnergyStored() < ENERGY_STORAGE_REQUIRED_TO_RUN) {
            return;
        }

        final var recipe = level
                .getRecipeManager()
                .getRecipeFor(TestTileEntityRecipe.Type.INSTANCE, container, level);
        if (recipe.isEmpty()) {
            return;
        }

        this.getEnergyStorage().extractEnergy(ENERGY_STORAGE_MAX / 256, false);
        if (this.currentProgress < recipe.orElseThrow().getRecipeCost()) {
            this.currentProgress += 1;
            return;
        }

        this.getStackHandler().insertItem(1, recipe.get().getResultItem(), false);
        this.currentProgress = 0;
    }

    protected boolean canProcess(SimpleContainer container) {
        final var inputSlotStack = container.getItem(0);
        final var outputSlotStack = container.getItem(1);

        return (inputSlotStack.getItem().equals(outputSlotStack.getItem()) || outputSlotStack.isEmpty())
                && inputSlotStack.getCount() > 0
                && outputSlotStack.getCount() < this.getStackHandler().getSlotLimit(1);
    }

    public int getCurrentProgressPercentage() {
        if (!this.activeState) {
            return 0;
        }

        final var storageComponent = this.<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER).orElseThrow();
        final var container = storageComponent.getAsSimpleContainer();

        final var recipe = level
                .getRecipeManager()
                .getRecipeFor(TestTileEntityRecipe.Type.INSTANCE, container, level);

        if (recipe.isEmpty()) {
            return 0;
        }

        final var calculatedPercentage = (int) ((double) currentProgress / (double) recipe.get().getRecipeCost() * 100);
        return Math.min(100, calculatedPercentage);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("lushatest.blockduplicator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new TestBlockContainerMenu(
                id,
                inventory,
                this.getStackHandler(),
                this,
                new TestBlockContainerData(this)
        );
    }
}
