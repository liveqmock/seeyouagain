/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.xmn.saas.base.thrift.common;
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

public class ResponseList implements org.apache.thrift.TBase<ResponseList, ResponseList._Fields>, java.io.Serializable, Cloneable, Comparable<ResponseList> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ResponseList");

  private static final org.apache.thrift.protocol.TField DATA_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("dataInfo", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField DATA_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("dataList", org.apache.thrift.protocol.TType.LIST, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ResponseListStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ResponseListTupleSchemeFactory());
  }

  public ResponseData dataInfo; // required
  public List<ResponseSubList> dataList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DATA_INFO((short)1, "dataInfo"),
    DATA_LIST((short)3, "dataList");

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
        case 1: // DATA_INFO
          return DATA_INFO;
        case 3: // DATA_LIST
          return DATA_LIST;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DATA_INFO, new org.apache.thrift.meta_data.FieldMetaData("dataInfo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ResponseData.class)));
    tmpMap.put(_Fields.DATA_LIST, new org.apache.thrift.meta_data.FieldMetaData("dataList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ResponseSubList.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ResponseList.class, metaDataMap);
  }

  public ResponseList() {
  }

  public ResponseList(
    ResponseData dataInfo,
    List<ResponseSubList> dataList)
  {
    this();
    this.dataInfo = dataInfo;
    this.dataList = dataList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ResponseList(ResponseList other) {
    if (other.isSetDataInfo()) {
      this.dataInfo = new ResponseData(other.dataInfo);
    }
    if (other.isSetDataList()) {
      List<ResponseSubList> __this__dataList = new ArrayList<ResponseSubList>(other.dataList.size());
      for (ResponseSubList other_element : other.dataList) {
        __this__dataList.add(new ResponseSubList(other_element));
      }
      this.dataList = __this__dataList;
    }
  }

  public ResponseList deepCopy() {
    return new ResponseList(this);
  }

  @Override
  public void clear() {
    this.dataInfo = null;
    this.dataList = null;
  }

  public ResponseData getDataInfo() {
    return this.dataInfo;
  }

  public ResponseList setDataInfo(ResponseData dataInfo) {
    this.dataInfo = dataInfo;
    return this;
  }

  public void unsetDataInfo() {
    this.dataInfo = null;
  }

  /** Returns true if field dataInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetDataInfo() {
    return this.dataInfo != null;
  }

  public void setDataInfoIsSet(boolean value) {
    if (!value) {
      this.dataInfo = null;
    }
  }

  public int getDataListSize() {
    return (this.dataList == null) ? 0 : this.dataList.size();
  }

  public java.util.Iterator<ResponseSubList> getDataListIterator() {
    return (this.dataList == null) ? null : this.dataList.iterator();
  }

  public void addToDataList(ResponseSubList elem) {
    if (this.dataList == null) {
      this.dataList = new ArrayList<ResponseSubList>();
    }
    this.dataList.add(elem);
  }

  public List<ResponseSubList> getDataList() {
    return this.dataList;
  }

  public ResponseList setDataList(List<ResponseSubList> dataList) {
    this.dataList = dataList;
    return this;
  }

  public void unsetDataList() {
    this.dataList = null;
  }

  /** Returns true if field dataList is set (has been assigned a value) and false otherwise */
  public boolean isSetDataList() {
    return this.dataList != null;
  }

  public void setDataListIsSet(boolean value) {
    if (!value) {
      this.dataList = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DATA_INFO:
      if (value == null) {
        unsetDataInfo();
      } else {
        setDataInfo((ResponseData)value);
      }
      break;

    case DATA_LIST:
      if (value == null) {
        unsetDataList();
      } else {
        setDataList((List<ResponseSubList>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DATA_INFO:
      return getDataInfo();

    case DATA_LIST:
      return getDataList();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DATA_INFO:
      return isSetDataInfo();
    case DATA_LIST:
      return isSetDataList();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ResponseList)
      return this.equals((ResponseList)that);
    return false;
  }

  public boolean equals(ResponseList that) {
    if (that == null)
      return false;

    boolean this_present_dataInfo = true && this.isSetDataInfo();
    boolean that_present_dataInfo = true && that.isSetDataInfo();
    if (this_present_dataInfo || that_present_dataInfo) {
      if (!(this_present_dataInfo && that_present_dataInfo))
        return false;
      if (!this.dataInfo.equals(that.dataInfo))
        return false;
    }

    boolean this_present_dataList = true && this.isSetDataList();
    boolean that_present_dataList = true && that.isSetDataList();
    if (this_present_dataList || that_present_dataList) {
      if (!(this_present_dataList && that_present_dataList))
        return false;
      if (!this.dataList.equals(that.dataList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(ResponseList other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDataInfo()).compareTo(other.isSetDataInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDataInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.dataInfo, other.dataInfo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDataList()).compareTo(other.isSetDataList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDataList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.dataList, other.dataList);
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
    StringBuilder sb = new StringBuilder("ResponseList(");
    boolean first = true;

    sb.append("dataInfo:");
    if (this.dataInfo == null) {
      sb.append("null");
    } else {
      sb.append(this.dataInfo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("dataList:");
    if (this.dataList == null) {
      sb.append("null");
    } else {
      sb.append(this.dataList);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (dataInfo != null) {
      dataInfo.validate();
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ResponseListStandardSchemeFactory implements SchemeFactory {
    public ResponseListStandardScheme getScheme() {
      return new ResponseListStandardScheme();
    }
  }

  private static class ResponseListStandardScheme extends StandardScheme<ResponseList> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ResponseList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DATA_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.dataInfo = new ResponseData();
              struct.dataInfo.read(iprot);
              struct.setDataInfoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DATA_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list28 = iprot.readListBegin();
                struct.dataList = new ArrayList<ResponseSubList>(_list28.size);
                for (int _i29 = 0; _i29 < _list28.size; ++_i29)
                {
                  ResponseSubList _elem30;
                  _elem30 = new ResponseSubList();
                  _elem30.read(iprot);
                  struct.dataList.add(_elem30);
                }
                iprot.readListEnd();
              }
              struct.setDataListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ResponseList struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.dataInfo != null) {
        oprot.writeFieldBegin(DATA_INFO_FIELD_DESC);
        struct.dataInfo.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.dataList != null) {
        oprot.writeFieldBegin(DATA_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.dataList.size()));
          for (ResponseSubList _iter31 : struct.dataList)
          {
            _iter31.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ResponseListTupleSchemeFactory implements SchemeFactory {
    public ResponseListTupleScheme getScheme() {
      return new ResponseListTupleScheme();
    }
  }

  private static class ResponseListTupleScheme extends TupleScheme<ResponseList> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ResponseList struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetDataInfo()) {
        optionals.set(0);
      }
      if (struct.isSetDataList()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetDataInfo()) {
        struct.dataInfo.write(oprot);
      }
      if (struct.isSetDataList()) {
        {
          oprot.writeI32(struct.dataList.size());
          for (ResponseSubList _iter32 : struct.dataList)
          {
            _iter32.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ResponseList struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.dataInfo = new ResponseData();
        struct.dataInfo.read(iprot);
        struct.setDataInfoIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list33 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.dataList = new ArrayList<ResponseSubList>(_list33.size);
          for (int _i34 = 0; _i34 < _list33.size; ++_i34)
          {
            ResponseSubList _elem35;
            _elem35 = new ResponseSubList();
            _elem35.read(iprot);
            struct.dataList.add(_elem35);
          }
        }
        struct.setDataListIsSet(true);
      }
    }
  }

}

