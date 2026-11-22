package ua.lab2.devices.parser;

class StaxDeviceParserTest extends DeviceParserContractTest {

    @Override
    protected DeviceParser createParser() {
        return new StaxDeviceParser();
    }
}