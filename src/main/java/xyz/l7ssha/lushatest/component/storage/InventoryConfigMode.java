package xyz.l7ssha.lushatest.component.storage;

import java.util.Arrays;

public enum InventoryConfigMode {
    NONE(0, "None", "N"),
    INPUT(1, "Input", "I"),
    INPUT_OUTPUT(2, "Input/Output", "IO"),
    OUTPUT(3, "Output", "O");

    private final int index;
    private final String label;

    private final String shortLabel;

    InventoryConfigMode(int value, String label, String shortLabel) {
        this.index = value;
        this.label = label;
        this.shortLabel = shortLabel;
    }

    public static InventoryConfigMode fromIndex(int index) {
        return Arrays.stream(InventoryConfigMode.values()).filter(inventoryConfigMode -> inventoryConfigMode.index == index).findFirst().orElseThrow();
    }

    public static InventoryConfigMode fromLabel(String label) {
        return Arrays.stream(InventoryConfigMode.values()).filter(inventoryConfigMode -> inventoryConfigMode.label.equalsIgnoreCase(label)).findFirst().orElseThrow();
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public boolean isAllowInsert() {
        return switch (this) {
            case INPUT, INPUT_OUTPUT -> true;
            default -> false;
        };
    }

    public boolean isAllowExtract() {
        return switch (this) {
            case OUTPUT, INPUT_OUTPUT -> true;
            default -> false;
        };
    }

    public InventoryConfigMode next() {
        if (this == OUTPUT) {
            return NONE;
        }

        return InventoryConfigMode.fromIndex(this.index + 1);
    }
}
