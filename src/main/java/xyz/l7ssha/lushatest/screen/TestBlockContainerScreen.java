package xyz.l7ssha.lushatest.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.container.TestBlockContainer;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;
import xyz.l7ssha.lushatest.utils.Utils;

public class TestBlockContainerScreen extends AbstractContainerScreen<TestBlockContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(LushaTestMod.MOD_ID,
            "textures/gui/test_block_container.png");

    public TestBlockContainerScreen(TestBlockContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        final var energyStored = this.menu.getContainerData().get(0);
        final var scaledHeight = (int) Utils.mapNumber(energyStored, 0, TestTileEntity.ENERGY_STORAGE_MAX, 0, 62);

        bindTexture();
        blit(stack, this.leftPos + 118, this.topPos + 75 - scaledHeight, 176, 62 - scaledHeight, 30, scaledHeight);

        this.font.draw(stack, new TextComponent(energyStored + ""), this.leftPos + 118, this.topPos + 78, 0x404040);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }
}
