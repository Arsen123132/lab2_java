package ua.lab2.devices.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lab2.devices.model.Device;
import ua.lab2.devices.model.DeviceParams;

class DeviceComparatorTest {

    private List<Device> devices;

    private static Device device(String name, String origin, String price, int power) {
        DeviceParams params = new DeviceParams();
        params.setEnergyConsumption(power);

        Device device = new Device();
        device.setName(name);
        device.setOrigin(origin);
        device.setPrice(new BigDecimal(price));
        device.setParams(params);
        return device;
    }

    private List<String> sortedNames(DeviceComparator comparator) {
        List<Device> copy = new ArrayList<>(devices);
        copy.sort(comparator);
        return copy.stream().map(Device::getName).collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() {
        devices = List.of(
                device("mouse", "China", "500.00", 2),
                device("Keyboard", "China", "1500.00", 5),
                device("Webcam", "Switzerland", "1500.00", 3));
    }

    @Test
    void byNameIgnoresCase() {
        assertEquals(List.of("Keyboard", "mouse", "Webcam"),
                sortedNames(DeviceComparator.BY_NAME));
    }

    @Test
    void byPriceSortsAscending() {
        assertEquals("mouse", sortedNames(DeviceComparator.BY_PRICE).get(0));
    }

    @Test
    void byPriceTreatsEqualPricesAsEqual() {
        assertEquals(0, DeviceComparator.BY_PRICE.compare(devices.get(1), devices.get(2)));
    }

    @Test
    void byEnergyConsumptionSortsAscending() {
        assertEquals(List.of("mouse", "Webcam", "Keyboard"),
                sortedNames(DeviceComparator.BY_ENERGY_CONSUMPTION));
    }

    @Test
    void byOriginThenNameUsesNameAsTieBreaker() {
        assertEquals(List.of("Keyboard", "mouse", "Webcam"),
                sortedNames(DeviceComparator.BY_ORIGIN_THEN_NAME));
    }
}