package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LushaGuiWidget extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    protected final List<Widget> children = new ArrayList<>();

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float tick) {
        for (final var child: this.children) {
            child.render(stack, mouseX, mouseY, tick);

            if (child instanceof AbstractButton button) {
                button.renderToolTip(stack, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94739_) {
        return children.stream().filter(child -> child instanceof AbstractButton).anyMatch(button -> ((AbstractButton)button).mouseClicked(mouseX, mouseY, p_94739_));
    }

    @Override
    public @NotNull NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {}
}
