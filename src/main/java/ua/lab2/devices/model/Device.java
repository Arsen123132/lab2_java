package ua.lab2.devices.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Computer component described by the {@code Device} element
 * of the XML document.
 */
public class Device {

    private String id;
    private String name;
    private String origin;
    private BigDecimal price;
    private DeviceParams params;
    private boolean critical;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the country of production.
     *
     * @return country name
     */
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Returns the price of the component.
     *
     * @return price in hryvnias
     */
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public DeviceParams getParams() {
        return params;
    }

    public void setParams(DeviceParams params) {
        this.params = params;
    }

    /**
     * Tells whether the component is critical for the computer operation.
     *
     * @return {@code true} if the computer cannot work without it
     */
    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Device device = (Device) o;
        return critical == device.critical
                && Objects.equals(id, device.id)
                && Objects.equals(name, device.name)
                && Objects.equals(origin, device.origin)
                && Objects.equals(price, device.price)
                && Objects.equals(params, device.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, origin, price, params, critical);
    }

    @Override
    public String toString() {
        return "Device{id='" + id + "', name='" + name
                + "', origin='" + origin + "', price=" + price
                + ", params=" + params + ", critical=" + critical + '}';
    }
}