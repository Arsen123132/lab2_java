package ua.lab2.devices.parser;

class SaxDeviceParserTest extends DeviceParserContractTest {

    @Override
    protected DeviceParser createParser() {
        return new SaxDeviceParser();
    }
}