package ua.lab2.devices.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Technical parameters of a computer component
 * (the {@code Type} element in the XML document).
 */
public class DeviceParams {

    private boolean peripheral;
    private int energyConsumption;
    private boolean cooler;
    private DeviceGroup group;
    private List<Port> ports = new ArrayList<>();

    /**
     * Tells whether the component is a peripheral one.
     *
     * @return {@code true} if the component is peripheral
     */
    public boolean isPeripheral() {
        return peripheral;
    }

    public void setPeripheral(boolean peripheral) {
        this.peripheral = peripheral;
    }

    /**
     * Returns power consumption of the component.
     *
     * @return power consumption in watts
     */
    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    /**
     * Tells whether the component has a cooler.
     *
     * @return {@code true} if a cooler is present
     */
    public boolean hasCooler() {
        return cooler;
    }

    public void setCooler(boolean cooler) {
        this.cooler = cooler;
    }

    public DeviceGroup getGroup() {
        return group;
    }

    public void setGroup(DeviceGroup group) {
        this.group = group;
    }

    /**
     * Returns ports of the component.
     *
     * @return unmodifiable list of ports, empty if there are none
     */
    public List<Port> getPorts() {
        return Collections.unmodifiableList(ports);
    }

    /**
     * Replaces ports of the component with a copy of the given list.
     *
     * @param ports new ports, {@code null} is treated as an empty list
     */
    public void setPorts(List<Port> ports) {
        this.ports = ports == null ? new ArrayList<>() : new ArrayList<>(ports);
    }

    /**
     * Adds a single port to the component.
     *
     * @param port port to add
     */
    public void addPort(Port port) {
        ports.add(port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceParams that = (DeviceParams) o;
        return peripheral == that.peripheral
                && energyConsumption == that.energyConsumption
                && cooler == that.cooler
                && group == that.group
                && ports.equals(that.ports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peripheral, energyConsumption, cooler, group, ports);
    }

    @Override
    public String toString() {
        return "DeviceParams{peripheral=" + peripheral
                + ", energyConsumption=" + energyConsumption
                + ", cooler=" + cooler
                + ", group=" + group
                + ", ports=" + ports + '}';
    }
}