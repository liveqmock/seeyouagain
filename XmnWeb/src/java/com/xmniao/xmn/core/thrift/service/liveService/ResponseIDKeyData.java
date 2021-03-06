/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.xmniao.xmn.core.thrift.service.liveService;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseIDKeyData implements org.apache.thrift.TBase<ResponseIDKeyData, ResponseIDKeyData._Fields>, java.io.Serializable, Cloneable, Comparable<ResponseIDKeyData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ResponseIDKeyData");

  private static final org.apache.thrift.protocol.TField STATE_FIELD_DESC = new org.apache.thrift.protocol.TField("state", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("msg", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField RESULT_MAP_FIELD_DESC = new org.apache.thrift.protocol.TField("resultMap", org.apache.thrift.protocol.TType.MAP, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ResponseIDKeyDataStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ResponseIDKeyDataTupleSchemeFactory());
  }

  public int state; // required
  public String msg; // required
  public Map<String,Map<String,String>> resultMap; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATE((short)1, "state"),
    MSG((short)2, "msg"),
    RESULT_MAP((short)3, "resultMap");

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
        case 2: // MSG
          return MSG;
        case 3: // RESULT_MAP
          return RESULT_MAP;
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
    tmpMap.put(_Fields.MSG, new org.apache.thrift.meta_data.FieldMetaData("msg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.RESULT_MAP, new org.apache.thrift.meta_data.FieldMetaData("resultMap", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ResponseIDKeyData.class, metaDataMap);
  }

  public ResponseIDKeyData() {
  }

  public ResponseIDKeyData(
    int state,
    String msg,
    Map<String,Map<String,String>> resultMap)
  {
    this();
    this.state = state;
    setStateIsSet(true);
    this.msg = msg;
    this.resultMap = resultMap;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ResponseIDKeyData(ResponseIDKeyData other) {
    __isset_bitfield = other.__isset_bitfield;
    this.state = other.state;
    if (other.isSetMsg()) {
      this.msg = other.msg;
    }
    if (other.isSetResultMap()) {
      Map<String,Map<String,String>> __this__resultMap = new HashMap<String,Map<String,String>>(other.resultMap.size());
      for (Map.Entry<String, Map<String,String>> other_element : other.resultMap.entrySet()) {

        String other_element_key = other_element.getKey();
        Map<String,String> other_element_value = other_element.getValue();

        String __this__resultMap_copy_key = other_element_key;

        Map<String,String> __this__resultMap_copy_value = new HashMap<String,String>(other_element_value);

        __this__resultMap.put(__this__resultMap_copy_key, __this__resultMap_copy_value);
      }
      this.resultMap = __this__resultMap;
    }
  }

  public ResponseIDKeyData deepCopy() {
    return new ResponseIDKeyData(this);
  }

  @Override
  public void clear() {
    setStateIsSet(false);
    this.state = 0;
    this.msg = null;
    this.resultMap = null;
  }

  public int getState() {
    return this.state;
  }

  public ResponseIDKeyData setState(int state) {
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

  public String getMsg() {
    return this.msg;
  }

  public ResponseIDKeyData setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public void unsetMsg() {
    this.msg = null;
  }

  /** Returns true if field msg is set (has been assigned a value) and false otherwise */
  public boolean isSetMsg() {
    return this.msg != null;
  }

  public void setMsgIsSet(boolean value) {
    if (!value) {
      this.msg = null;
    }
  }

  public int getResultMapSize() {
    return (this.resultMap == null) ? 0 : this.resultMap.size();
  }

  public void putToResultMap(String key, Map<String,String> val) {
    if (this.resultMap == null) {
      this.resultMap = new HashMap<String,Map<String,String>>();
    }
    this.resultMap.put(key, val);
  }

  public Map<String,Map<String,String>> getResultMap() {
    return this.resultMap;
  }

  public ResponseIDKeyData setResultMap(Map<String,Map<String,String>> resultMap) {
    this.resultMap = resultMap;
    return this;
  }

  public void unsetResultMap() {
    this.resultMap = null;
  }

  /** Returns true if field resultMap is set (has been assigned a value) and false otherwise */
  public boolean isSetResultMap() {
    return this.resultMap != null;
  }

  public void setResultMapIsSet(boolean value) {
    if (!value) {
      this.resultMap = null;
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

    case MSG:
      if (value == null) {
        unsetMsg();
      } else {
        setMsg((String)value);
      }
      break;

    case RESULT_MAP:
      if (value == null) {
        unsetResultMap();
      } else {
        setResultMap((Map<String,Map<String,String>>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATE:
      return Integer.valueOf(getState());

    case MSG:
      return getMsg();

    case RESULT_MAP:
      return getResultMap();

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
    case MSG:
      return isSetMsg();
    case RESULT_MAP:
      return isSetResultMap();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ResponseIDKeyData)
      return this.equals((ResponseIDKeyData)that);
    return false;
  }

  public boolean equals(ResponseIDKeyData that) {
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

    boolean this_present_msg = true && this.isSetMsg();
    boolean that_present_msg = true && that.isSetMsg();
    if (this_present_msg || that_present_msg) {
      if (!(this_present_msg && that_present_msg))
        return false;
      if (!this.msg.equals(that.msg))
        return false;
    }

    boolean this_present_resultMap = true && this.isSetResultMap();
    boolean that_present_resultMap = true && that.isSetResultMap();
    if (this_present_resultMap || that_present_resultMap) {
      if (!(this_present_resultMap && that_present_resultMap))
        return false;
      if (!this.resultMap.equals(that.resultMap))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(ResponseIDKeyData other) {
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
    lastComparison = Boolean.valueOf(isSetMsg()).compareTo(other.isSetMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.msg, other.msg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetResultMap()).compareTo(other.isSetResultMap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResultMap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resultMap, other.resultMap);
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
    StringBuilder sb = new StringBuilder("ResponseIDKeyData(");
    boolean first = true;

    sb.append("state:");
    sb.append(this.state);
    first = false;
    if (!first) sb.append(", ");
    sb.append("msg:");
    if (this.msg == null) {
      sb.append("null");
    } else {
      sb.append(this.msg);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("resultMap:");
    if (this.resultMap == null) {
      sb.append("null");
    } else {
      sb.append(this.resultMap);
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

  private static class ResponseIDKeyDataStandardSchemeFactory implements SchemeFactory {
    public ResponseIDKeyDataStandardScheme getScheme() {
      return new ResponseIDKeyDataStandardScheme();
    }
  }

  private static class ResponseIDKeyDataStandardScheme extends StandardScheme<ResponseIDKeyData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ResponseIDKeyData struct) throws org.apache.thrift.TException {
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
          case 2: // MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.msg = iprot.readString();
              struct.setMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // RESULT_MAP
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map10 = iprot.readMapBegin();
                struct.resultMap = new HashMap<String,Map<String,String>>(2*_map10.size);
                for (int _i11 = 0; _i11 < _map10.size; ++_i11)
                {
                  String _key12;
                  Map<String,String> _val13;
                  _key12 = iprot.readString();
                  {
                    org.apache.thrift.protocol.TMap _map14 = iprot.readMapBegin();
                    _val13 = new HashMap<String,String>(2*_map14.size);
                    for (int _i15 = 0; _i15 < _map14.size; ++_i15)
                    {
                      String _key16;
                      String _val17;
                      _key16 = iprot.readString();
                      _val17 = iprot.readString();
                      _val13.put(_key16, _val17);
                    }
                    iprot.readMapEnd();
                  }
                  struct.resultMap.put(_key12, _val13);
                }
                iprot.readMapEnd();
              }
              struct.setResultMapIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ResponseIDKeyData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATE_FIELD_DESC);
      oprot.writeI32(struct.state);
      oprot.writeFieldEnd();
      if (struct.msg != null) {
        oprot.writeFieldBegin(MSG_FIELD_DESC);
        oprot.writeString(struct.msg);
        oprot.writeFieldEnd();
      }
      if (struct.resultMap != null) {
        oprot.writeFieldBegin(RESULT_MAP_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.MAP, struct.resultMap.size()));
          for (Map.Entry<String, Map<String,String>> _iter18 : struct.resultMap.entrySet())
          {
            oprot.writeString(_iter18.getKey());
            {
              oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, _iter18.getValue().size()));
              for (Map.Entry<String, String> _iter19 : _iter18.getValue().entrySet())
              {
                oprot.writeString(_iter19.getKey());
                oprot.writeString(_iter19.getValue());
              }
              oprot.writeMapEnd();
            }
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ResponseIDKeyDataTupleSchemeFactory implements SchemeFactory {
    public ResponseIDKeyDataTupleScheme getScheme() {
      return new ResponseIDKeyDataTupleScheme();
    }
  }

  private static class ResponseIDKeyDataTupleScheme extends TupleScheme<ResponseIDKeyData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ResponseIDKeyData struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetState()) {
        optionals.set(0);
      }
      if (struct.isSetMsg()) {
        optionals.set(1);
      }
      if (struct.isSetResultMap()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetState()) {
        oprot.writeI32(struct.state);
      }
      if (struct.isSetMsg()) {
        oprot.writeString(struct.msg);
      }
      if (struct.isSetResultMap()) {
        {
          oprot.writeI32(struct.resultMap.size());
          for (Map.Entry<String, Map<String,String>> _iter20 : struct.resultMap.entrySet())
          {
            oprot.writeString(_iter20.getKey());
            {
              oprot.writeI32(_iter20.getValue().size());
              for (Map.Entry<String, String> _iter21 : _iter20.getValue().entrySet())
              {
                oprot.writeString(_iter21.getKey());
                oprot.writeString(_iter21.getValue());
              }
            }
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ResponseIDKeyData struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.state = iprot.readI32();
        struct.setStateIsSet(true);
      }
      if (incoming.get(1)) {
        struct.msg = iprot.readString();
        struct.setMsgIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TMap _map22 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.MAP, iprot.readI32());
          struct.resultMap = new HashMap<String,Map<String,String>>(2*_map22.size);
          for (int _i23 = 0; _i23 < _map22.size; ++_i23)
          {
            String _key24;
            Map<String,String> _val25;
            _key24 = iprot.readString();
            {
              org.apache.thrift.protocol.TMap _map26 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
              _val25 = new HashMap<String,String>(2*_map26.size);
              for (int _i27 = 0; _i27 < _map26.size; ++_i27)
              {
                String _key28;
                String _val29;
                _key28 = iprot.readString();
                _val29 = iprot.readString();
                _val25.put(_key28, _val29);
              }
            }
            struct.resultMap.put(_key24, _val25);
          }
        }
        struct.setResultMapIsSet(true);
      }
    }
  }

}

