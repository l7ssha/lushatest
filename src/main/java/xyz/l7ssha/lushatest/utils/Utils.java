package xyz.l7ssha.lushatest.utils;

public final class Utils {
    public static double mapNumber(double value, double rangeMin, double rangeMax, double resultMin, double resultMax) {
        return (value - rangeMin) / (rangeMax - rangeMin) * (resultMax - resultMin) + resultMin;
    }

    public static String getStoredEnergyText(int storedEnergy, double storedEnergyPercentage) {
        if (storedEnergy > 1_000_000_000) {
            return "%.2f B RF (%.1f%%)".formatted(((double) storedEnergy / 1_000_000_000), storedEnergyPercentage);
        }

        if (storedEnergy > 1_000_000) {
            return "%.2f M RF (%.1f%%)".formatted(((double) storedEnergy / 1_000_000), storedEnergyPercentage);
        }

        if (storedEnergy > 1_000) {
            return "%.2f k RF (%.1f%%)".formatted(((double) storedEnergy / 1_000), storedEnergyPercentage);
        }

        return "Empty";
    }
}
