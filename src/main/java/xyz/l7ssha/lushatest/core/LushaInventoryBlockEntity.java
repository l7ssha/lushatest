package xyz.l7ssha.lushatest.core;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class LushaInventoryBlockEntity extends LushaTestBlockEntity {
    protected final static String INVENTORY_TAG = "inventory";

    protected final ItemStackHandler stackHandler;

    protected final LazyOptional<ItemStackHandler> stackHandlerLazyOptional;

    public LushaInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);

        this.stackHandler = createStackHandler();
        this.stackHandler.setSize(size);
        this.stackHandlerLazyOptional = LazyOptional.of(() -> stackHandler);
    }

    protected ItemStackHandler createStackHandler() {
        return new ItemStackHandler() {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                var copiedStack = stack.copy();

                var returnStack = super.insertItem(slot, copiedStack, simulate);
                stack.shrink(stack.getCount() - returnStack.getCount());

                LushaInventoryBlockEntity.this.updateBlockEntity();
                return returnStack;
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                var returnStack = super.extractItem(slot, amount, simulate);
                LushaInventoryBlockEntity.this.updateBlockEntity();
                return returnStack;
            }
        };
    }

    public ItemStackHandler getStackHandler() {
        return this.stackHandler;
    }

    protected void updateBlockEntity() {
        requestModelDataUpdate();
        setChanged();
        if (level != null) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState());
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(INVENTORY_TAG, stackHandler.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.stackHandler.deserializeNBT(tag.getCompound(INVENTORY_TAG));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.stackHandlerLazyOptional.cast();
        }

        return super.getCapability(cap);
    }
}
