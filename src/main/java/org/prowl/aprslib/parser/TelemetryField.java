package org.prowl.aprslib.parser;

import java.util.Objects;

public class TelemetryField extends APRSData {
    private static final long serialVersionUID = 1L;
    protected String objectName;

    protected TelemetryField() {
    }

    /**
     * Parse and APRS telemetry field
     *
     * @return new ObjectPacket instance with the parsed data
     */
    public TelemetryField(byte[] msgBody) throws Exception {
        this.objectName = new String(msgBody, 1, 9).trim();
        this.setLastCursorPosition(10);
    }

    public TelemetryField(String objectName, boolean live, Position position, String comment) {
        this.objectName = objectName;
        this.comment = comment;
    }

    /**
     * @return the objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @param objectName the objectName to set
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return new String(rawBytes);
    }


    /**
     * @param o
     * @return int
     */
    @Override
    public int compareTo(APRSData o) {
        if (this.hashCode() > o.hashCode()) {
            return 1;
        }
        if (this.hashCode() == o.hashCode()) {
            return 0;
        }
        return -1;
    }


    /**
     * @return boolean
     */
    @Override
    public boolean hasFault() {
        return this.hasFault;
    }


    /**
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof TelemetryField)) {
            return false;
        }
        TelemetryField objectField = (TelemetryField) o;
        return Objects.equals(objectName, objectField.objectName);
    }


    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(objectName);
    }

}
