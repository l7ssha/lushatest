package xyz.l7ssha.lushatest.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.LushaTestMod;
import xyz.l7ssha.lushatest.container.TestBlockContainerMenu;
import xyz.l7ssha.lushatest.screen.widget.config.EnergyConfigurationPanel;
import xyz.l7ssha.lushatest.screen.widget.config.StorageConfigurationPanel;
import xyz.l7ssha.lushatest.tileentities.TestTileEntity;
import xyz.l7ssha.lushatest.utils.Utils;

public class TestBlockContainerScreen extends LushaContainerScreen<TestBlockContainerMenu> {
    public TestBlockContainerScreen(TestBlockContainerMenu container, Inventory playerInv, Component title) {
        super(container, playerInv, title, new ResourceLocation(LushaTestMod.MOD_ID, "textures/gui/test_block_container.png"));
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        final var currentProgress = this.menu.getContainerData().get(1);
        if (mouseX > this.leftPos + 57 && mouseX < this.leftPos + 57 + 30 && mouseY > this.topPos + 35 && mouseY < this.topPos + 36 + 18) {
            this.renderTooltip(stack, Component.literal(currentProgress + "%"), mouseX, mouseY);
        }

        final var storedEnergyText = this.getStoredEnergyText();
        if (mouseX > this.leftPos + 136 && mouseX < this.leftPos + 146 + 25 && mouseY > this.topPos + 10 && mouseY < this.topPos + 36 + 36) {
            this.renderTooltip(stack, Component.literal(storedEnergyText), mouseX, mouseY);
        }

        this.addRenderableWidget(new StorageConfigurationPanel(this.leftPos, this.topPos, this, this.menu));
        this.addRenderableWidget(new EnergyConfigurationPanel(this.leftPos, this.topPos + 60, this, this.menu));
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        final var energyStored = this.menu.getContainerData().get(0);
        final var scaledEnergyStored = (int) Utils.mapNumber(energyStored, 0, TestTileEntity.ENERGY_STORAGE_MAX, 0, 62);

        bindTexture();
        blit(stack, this.leftPos + 137, this.topPos + 75 - scaledEnergyStored, 176, 62 - scaledEnergyStored, 30, scaledEnergyStored);

        final var currentProgress = this.menu.getContainerData().get(1);
        final var scaledCurrentProgress = (int) Utils.mapNumber(currentProgress, 0, 100, 0, 28);
        bindTexture();
        blit(stack, this.leftPos + 57, this.topPos + 36, 177,63, scaledCurrentProgress, 17);
    }

    protected String getStoredEnergyText() {
        final var storedEnergy = this.menu.getContainerData().get(0);

        return Utils.getStoredEnergyText(storedEnergy);
    }
}
