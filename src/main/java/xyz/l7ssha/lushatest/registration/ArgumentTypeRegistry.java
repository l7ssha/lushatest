package xyz.l7ssha.lushatest.registration;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.commands.argument.InventorySlotModeArgument;

public class ArgumentTypeRegistry {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, LushaTestMod.MOD_ID);

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> INVENTORY_SLOT_MODE = ARGUMENT_TYPE_REGISTRY.register(
            "inventory_slot_argument", () ->
                    ArgumentTypeInfos.registerByClass(InventorySlotModeArgument.class,
                            SingletonArgumentInfo.contextFree(InventorySlotModeArgument::new)));

    public static void setUp(IEventBus modEventBus) {
        ARGUMENT_TYPE_REGISTRY.register(modEventBus);
    }
}
