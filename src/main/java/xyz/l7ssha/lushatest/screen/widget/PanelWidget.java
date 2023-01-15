package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.LushaTestMod;

public abstract class PanelWidget extends LushaGuiWidget {
    private final static ResourceLocation textureLocation = new ResourceLocation(LushaTestMod.MOD_ID, "textures/gui/container_panel.png");

    protected final int x;
    protected final int y;
    private final Component labelComponent;

    public PanelWidget(final int x, final int y, Component component) {
        this.x = x;
        this.y = y;
        this.labelComponent = component;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float tick) {
        bindTexture();
        blit(stack, x - 48, y, 0, 0, 53, 76);

        Minecraft.getInstance().font.draw(stack, this.labelComponent, x - 40, y + 6, 0x0f0f0f);
        super.render(stack, mouseX, mouseY, tick);
    }

    protected void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, textureLocation);
    }
}
