package ua.lab2.devices.comparator;

import java.util.Comparator;
import ua.lab2.devices.model.Device;

/**
 * Ready-made orderings of devices.
 */
public enum DeviceComparator implements Comparator<Device> {

    BY_NAME(Comparator.comparing(Device::getName, String.CASE_INSENSITIVE_ORDER)),

    BY_PRICE(Comparator.comparing(Device::getPrice)),

    BY_ENERGY_CONSUMPTION(Comparator.comparingInt(
            device -> device.getParams().getEnergyConsumption())),

    BY_ORIGIN_THEN_NAME(Comparator
            .comparing(Device::getOrigin, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(Device::getName, String.CASE_INSENSITIVE_ORDER));

    private final Comparator<Device> delegate;

    DeviceComparator(Comparator<Device> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int compare(Device first, Device second) {
        return delegate.compare(first, second);
    }
}