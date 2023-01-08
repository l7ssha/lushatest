package xyz.l7ssha.lushatest.component;

import java.util.Arrays;
import java.util.List;

public enum AccessModeConfig {
    NONE(0, "None", "N"),
    INPUT(1, "Input", "I"),
    INPUT_OUTPUT(2, "Input/Output", "IO"),
    OUTPUT(3, "Output", "O");

    private final int index;
    private final String label;

    private final String shortLabel;

    AccessModeConfig(int value, String label, String shortLabel) {
        this.index = value;
        this.label = label;
        this.shortLabel = shortLabel;
    }

    public static AccessModeConfig fromIndex(int index) {
        return Arrays.stream(AccessModeConfig.values()).filter(inventoryConfigMode -> inventoryConfigMode.index == index).findFirst().orElseThrow();
    }

    public static AccessModeConfig fromLabel(String label) {
        return Arrays.stream(AccessModeConfig.values()).filter(inventoryConfigMode -> inventoryConfigMode.label.equalsIgnoreCase(label)).findFirst().orElseThrow();
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

    public AccessModeConfig next() {
        if (this == OUTPUT) {
            return NONE;
        }

        return AccessModeConfig.fromIndex(this.index + 1);
    }

    public AccessModeConfig next(List<AccessModeConfig> allowedValues) {
        if (allowedValues.isEmpty()) {
            throw new RuntimeException("allowedValues should have at least one value");
        }

        final var nextValue = next();
        if (allowedValues.contains(nextValue)) {
            return nextValue;
        }

        return nextValue.next(allowedValues);
    }
}
