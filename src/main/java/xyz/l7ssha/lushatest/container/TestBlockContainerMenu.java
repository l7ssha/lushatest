package xyz.l7ssha.lushatest.container;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.component.configuration.AccessModeConfig;
import xyz.l7ssha.lushatest.component.energy.EnergyCapabilityComponent;
import xyz.l7ssha.lushatest.component.storage.StorageCapabilityComponent;
import xyz.l7ssha.lushatest.container.slot.ItemRestrictedSlot;
import xyz.l7ssha.lushatest.container.slot.ReadonlySlot;
import xyz.l7ssha.lushatest.recipe.TestTileEntityRecipe;
import xyz.l7ssha.lushatest.registration.BlockRegistry;
import xyz.l7ssha.lushatest.registration.ContainerRegistry;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;

import java.util.List;
import java.util.Map;

public class TestBlockContainerMenu extends LushaTestContainerMenu {
    private static List<Item> ALLOWED_RECIPE_ITEMS;

    private final ContainerLevelAccess containerLevelAccess;
    private final ContainerData containerData;
    private final BlockEntity blockEntity;

    public TestBlockContainerMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, new ItemStackHandler(2), playerInv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public TestBlockContainerMenu(int id, @NotNull Inventory playerInv, IItemHandler slots, BlockEntity blockEntity, ContainerData containerData) {
        super(ContainerRegistry.TEST_BLOCK_CONTAINER.get(), id);
        this.containerLevelAccess = ContainerLevelAccess.create(playerInv.player.level, blockEntity.getBlockPos());
        this.containerData = containerData;
        this.blockEntity = blockEntity;

        addSlot(new ItemRestrictedSlot(slots, 0, 26, 36, getAllowedRecipeItems(playerInv.player.getLevel())));
        addSlot(new ReadonlySlot(slots, 1, 98, 36));

        addPlayerInv(playerInv);

        addDataSlot(DataSlot.forContainer(containerData, 0));
        addDataSlot(DataSlot.forContainer(containerData, 1));
    }

    public ContainerData getContainerData() {
        return this.containerData;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public Map<Direction, AccessModeConfig> getInventoryConfig() {
        final var configuration = ((TestTileEntity) blockEntity).<StorageCapabilityComponent>getComponent(ForgeCapabilities.ITEM_HANDLER).orElseThrow().getStackHandlerProvider().getSideAccessConfiguration();

        return configuration.getSideConfigurationDirectly();
    }

    public Map<Direction, AccessModeConfig> getEnergyConfig() {
        final var configuration = ((TestTileEntity) blockEntity).<EnergyCapabilityComponent<?>>getComponent(ForgeCapabilities.ENERGY).orElseThrow().getSideAccessConfiguration();

        return configuration.getSideConfigurationDirectly();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.containerLevelAccess, player, BlockRegistry.TEST_BLOCK.get());
    }

    private static List<Item> getAllowedRecipeItems(Level level) {
        if (ALLOWED_RECIPE_ITEMS == null) {
            ALLOWED_RECIPE_ITEMS = level.getRecipeManager()
                    .getAllRecipesFor(TestTileEntityRecipe.Type.INSTANCE)
                    .stream()
                    .parallel()
                    .map(recipe -> recipe.getInputItem().getItems()[0].getItem())
                    .toList();
        }

        return ALLOWED_RECIPE_ITEMS;
    }
}
