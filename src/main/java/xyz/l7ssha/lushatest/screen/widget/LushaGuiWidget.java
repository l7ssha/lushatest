package xyz.l7ssha.lushatest.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;
import xyz.l7ssha.lushatest.screen.LushaContainerScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class LushaGuiWidget extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    protected final List<Widget> children = new ArrayList<>();

    protected final LushaContainerScreen<?> screen;
    protected final int x;
    protected final int y;

    public LushaGuiWidget(int x, int y, LushaContainerScreen<?> screen) {
        this.x = x;
        this.y = y;
        this.screen = screen;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float tick) {
        for (final var child : this.children) {
            child.render(stack, mouseX, mouseY, tick);

            if (child instanceof AbstractWidget widget) {
                widget.renderToolTip(stack, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94739_) {
        return children
                .stream()
                .filter(child -> child instanceof GuiEventListener)
                .anyMatch(widget -> ((GuiEventListener) widget).mouseClicked(mouseX, mouseY, p_94739_));
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
