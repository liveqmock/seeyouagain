package com.xmniao.xmn.core.thrift.sellerCard;

/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;

public class FailureException extends TException implements org.apache.thrift.TBase<FailureException, FailureException._Fields>, java.io.Serializable, Cloneable, Comparable<FailureException> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("FailureException");

  private static final org.apache.thrift.protocol.TField STATE_FIELD_DESC = new org.apache.thrift.protocol.TField("state", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("info", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new FailureExceptionStandardSchemeFactory());
    schemes.put(TupleScheme.class, new FailureExceptionTupleSchemeFactory());
  }

  public int state; // required
  public String info; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATE((short)1, "state"),
    INFO((short)2, "info");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // STATE
          return STATE;
        case 2: // INFO
          return INFO;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __STATE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATE, new org.apache.thrift.meta_data.FieldMetaData("state", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.INFO, new org.apache.thrift.meta_data.FieldMetaData("info", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(FailureException.class, metaDataMap);
  }

  public FailureException() {
  }

  public FailureException(
    int state,
    String info)
  {
    this();
    this.state = state;
    setStateIsSet(true);
    this.info = info;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public FailureException(FailureException other) {
    __isset_bitfield = other.__isset_bitfield;
    this.state = other.state;
    if (other.isSetInfo()) {
      this.info = other.info;
    }
  }

  public FailureException deepCopy() {
    return new FailureException(this);
  }

  @Override
  public void clear() {
    setStateIsSet(false);
    this.state = 0;
    this.info = null;
  }

  public int getState() {
    return this.state;
  }

  public FailureException setState(int state) {
    this.state = state;
    setStateIsSet(true);
    return this;
  }

  public void unsetState() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STATE_ISSET_ID);
  }

  /** Returns true if field state is set (has been assigned a value) and false otherwise */
  public boolean isSetState() {
    return EncodingUtils.testBit(__isset_bitfield, __STATE_ISSET_ID);
  }

  public void setStateIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STATE_ISSET_ID, value);
  }

  public String getInfo() {
    return this.info;
  }

  public FailureException setInfo(String info) {
    this.info = info;
    return this;
  }

  public void unsetInfo() {
    this.info = null;
  }

  /** Returns true if field info is set (has been assigned a value) and false otherwise */
  public boolean isSetInfo() {
    return this.info != null;
  }

  public void setInfoIsSet(boolean value) {
    if (!value) {
      this.info = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case STATE:
      if (value == null) {
        unsetState();
      } else {
        setState((Integer)value);
      }
      break;

    case INFO:
      if (value == null) {
        unsetInfo();
      } else {
        setInfo((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATE:
      return Integer.valueOf(getState());

    case INFO:
      return getInfo();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case STATE:
      return isSetState();
    case INFO:
      return isSetInfo();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof FailureException)
      return this.equals((FailureException)that);
    return false;
  }

  public boolean equals(FailureException that) {
    if (that == null)
      return false;

    boolean this_present_state = true;
    boolean that_present_state = true;
    if (this_present_state || that_present_state) {
      if (!(this_present_state && that_present_state))
        return false;
      if (this.state != that.state)
        return false;
    }

    boolean this_present_info = true && this.isSetInfo();
    boolean that_present_info = true && that.isSetInfo();
    if (this_present_info || that_present_info) {
      if (!(this_present_info && that_present_info))
        return false;
      if (!this.info.equals(that.info))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(FailureException other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetState()).compareTo(other.isSetState());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetState()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.state, other.state);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetInfo()).compareTo(other.isSetInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.info, other.info);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("FailureException(");
    boolean first = true;

    sb.append("state:");
    sb.append(this.state);
    first = false;
    if (!first) sb.append(", ");
    sb.append("info:");
    if (this.info == null) {
      sb.append("null");
    } else {
      sb.append(this.info);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class FailureExceptionStandardSchemeFactory implements SchemeFactory {
    public FailureExceptionStandardScheme getScheme() {
      return new FailureExceptionStandardScheme();
    }
  }

  private static class FailureExceptionStandardScheme extends StandardScheme<FailureException> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, FailureException struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.state = iprot.readI32();
              struct.setStateIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.info = iprot.readString();
              struct.setInfoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, FailureException struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATE_FIELD_DESC);
      oprot.writeI32(struct.state);
      oprot.writeFieldEnd();
      if (struct.info != null) {
        oprot.writeFieldBegin(INFO_FIELD_DESC);
        oprot.writeString(struct.info);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class FailureExceptionTupleSchemeFactory implements SchemeFactory {
    public FailureExceptionTupleScheme getScheme() {
      return new FailureExceptionTupleScheme();
    }
  }

  private static class FailureExceptionTupleScheme extends TupleScheme<FailureException> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, FailureException struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetState()) {
        optionals.set(0);
      }
      if (struct.isSetInfo()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetState()) {
        oprot.writeI32(struct.state);
      }
      if (struct.isSetInfo()) {
        oprot.writeString(struct.info);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, FailureException struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.state = iprot.readI32();
        struct.setStateIsSet(true);
      }
      if (incoming.get(1)) {
        struct.info = iprot.readString();
        struct.setInfoIsSet(true);
      }
    }
  }

}

