package xyz.l7ssha.lushatest.component.storage;

import java.util.Arrays;

public enum InventoryConfigMode {
    NONE(0, "None"),
    INPUT(1, "Input"),
    INPUT_OUTPUT(2, "Input/Output"),
    OUTPUT(3, "Output");

    private final int index;
    private final String label;

    InventoryConfigMode(int value, String label) {
        this.index = value;
        this.label = label;
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
}
