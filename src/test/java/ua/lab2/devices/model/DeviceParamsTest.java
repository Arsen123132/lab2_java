package ua.lab2.devices.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DeviceParamsTest {

    private static DeviceParams createParams() {
        DeviceParams params = new DeviceParams();
        params.setPeripheral(true);
        params.setEnergyConsumption(5);
        params.setCooler(false);
        params.setGroup(DeviceGroup.INPUT_OUTPUT);
        params.addPort(Port.USB);
        return params;
    }

    @Test
    void gettersReturnValuesSetBefore() {
        DeviceParams params = createParams();

        assertTrue(params.isPeripheral());
        assertEquals(5, params.getEnergyConsumption());
        assertFalse(params.hasCooler());
        assertEquals(DeviceGroup.INPUT_OUTPUT, params.getGroup());
        assertEquals(List.of(Port.USB), params.getPorts());
    }

    @Test
    void portsAreEmptyByDefault() {
        assertTrue(new DeviceParams().getPorts().isEmpty());
    }

    @Test
    void setPortsTreatsNullAsEmptyList() {
        DeviceParams params = createParams();
        params.setPorts(null);

        assertTrue(params.getPorts().isEmpty());
    }

    @Test
    void setPortsCopiesGivenList() {
        List<Port> source = new ArrayList<>(List.of(Port.COM));
        DeviceParams params = new DeviceParams();
        params.setPorts(source);
        source.add(Port.LPT);

        assertEquals(List.of(Port.COM), params.getPorts());
    }

    @Test
    void getPortsReturnsUnmodifiableList() {
        DeviceParams params = createParams();

        assertThrows(UnsupportedOperationException.class,
                () -> params.getPorts().add(Port.COM));
    }

    @Test
    void equalsAndHashCodeDependOnAllFields() {
        DeviceParams first = createParams();
        DeviceParams second = createParams();

        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        second.setPeripheral(false);
        assertNotEquals(first, second);

        second = createParams();
        second.setEnergyConsumption(6);
        assertNotEquals(first, second);

        second = createParams();
        second.setCooler(true);
        assertNotEquals(first, second);

        second = createParams();
        second.setGroup(DeviceGroup.MULTIMEDIA);
        assertNotEquals(first, second);

        second = createParams();
        second.addPort(Port.LPT);
        assertNotEquals(first, second);
    }

        @Test
    void equalsReturnsFalseForNullAndOtherClass() {
        DeviceParams params = createParams();

        // equals() must be called on the tested object itself
        assertFalse(params.equals(null));
        assertFalse(params.equals("params"));
    }

    @Test
    void toStringContainsFieldValues() {
        String text = createParams().toString();

        assertTrue(text.contains("energyConsumption=5"));
        assertTrue(text.contains("INPUT_OUTPUT"));
        assertTrue(text.contains("USB"));
    }

    
}