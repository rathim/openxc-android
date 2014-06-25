package com.openxc.messages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

public class DiagnosticRequest extends DiagnosticMessage {

    public static final String MULTIPLE_RESPONSES_KEY = "multiple_responses";
    public static final String FREQUENCY_KEY = "frequency";
    public static final String NAME_KEY = NamedVehicleMessage.NAME_KEY;

    public static final String[] sRequiredFieldsValues = new String[] {
            ID_KEY, BUS_KEY, MODE_KEY };
    public static final Set<String> sRequiredFields = new HashSet<String>(
            Arrays.asList(sRequiredFieldsValues));

    @SerializedName(MULTIPLE_RESPONSES_KEY)
    private boolean mMultipleResponses = false;

    @SerializedName(FREQUENCY_KEY)
    // Frequency is an optional field, so it is stored as a Double so it can
    // be nulled.
    private Double mFrequency;

    @SerializedName(NAME_KEY)
    private String mName;

    public DiagnosticRequest(int busId, int id, int mode) {
        super(busId, id, mode);
    }

    public DiagnosticRequest(int busId, int id, int mode, int pid) {
        super(busId, id, mode, pid);
    }

    public void setMultipleResponses(boolean multipleResponses) {
        mMultipleResponses = multipleResponses;
    }

    public void setFrequency(Double frequency) {
        mFrequency = frequency;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean getMultipleResponses() {
        return mMultipleResponses;
    }

    public Double getFrequency() {
        return mFrequency;
    }

    public String getName() {
        return mName;
    }

    public static boolean containsRequiredFields(Set<String> fields) {
        return fields.containsAll(sRequiredFields);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !super.equals(obj)) {
            return false;
        }

        final DiagnosticRequest other = (DiagnosticRequest) obj;
        return mMultipleResponses == other.mMultipleResponses
                // TODO because of floating point precision this comparison
                // doesn't always work. should we store it as a long?
                // && mFrequency == other.mFrequency
                && ((mName == null && other.mName == null)
                    || mName.equals(other.mName));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("timestamp", getTimestamp())
            .add("bus", getBusId())
            .add("id", getId())
            .add("mode", getMode())
            .add("pid", getPid())
            .add("payload", Arrays.toString(getPayload()))
            .add("multiple_responses", getMultipleResponses())
            .add("frequency", getFrequency())
            .add("name", getName())
            .add("extras", getExtras())
            .toString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeByte((byte) (getMultipleResponses() ? 1 : 0));
        out.writeValue(getFrequency());
        out.writeString(getName());
    }

    @Override
    protected void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mMultipleResponses = in.readByte() != 0;
        mFrequency = (Double) in.readValue(Double.class.getClassLoader());
        mName = in.readString();
    }

    protected DiagnosticRequest(Parcel in)
            throws UnrecognizedMessageTypeException {
        readFromParcel(in);
    }

    protected DiagnosticRequest() { }
}
