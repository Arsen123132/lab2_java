package ua.lab2.devices.parser;

class DomDeviceParserTest extends DeviceParserContractTest {

    @Override
    protected DeviceParser createParser() {
        return new DomDeviceParser();
    }
}