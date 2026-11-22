<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <!-- Muenchian grouping: index devices by their group -->
    <xsl:key name="devices-by-group" match="Device" use="Type/Group"/>

    <xsl:template match="/Devices">
        <GroupedDevices>
            <!-- first device of every group represents the group -->
            <xsl:for-each
                    select="Device[generate-id() = generate-id(key('devices-by-group', Type/Group)[1])]">
                <xsl:sort select="Type/Group"/>
                <Group name="{Type/Group}"
                       count="{count(key('devices-by-group', Type/Group))}">
                    <xsl:for-each select="key('devices-by-group', Type/Group)">
                        <xsl:sort select="Name"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </Group>
            </xsl:for-each>
        </GroupedDevices>
    </xsl:template>

    <!-- the group is now the parent element, so it is dropped from Type -->
    <xsl:template match="Device">
        <Device id="{@id}">
            <xsl:copy-of select="Name | Origin | Price"/>
            <Type>
                <xsl:copy-of select="Type/*[not(self::Group)]"/>
            </Type>
            <xsl:copy-of select="Critical"/>
        </Device>
    </xsl:template>
</xsl:stylesheet>