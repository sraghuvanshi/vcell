/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.vcellij.api;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-07-26")
public class VariableInfo implements org.apache.thrift.TBase<VariableInfo, VariableInfo._Fields>, java.io.Serializable, Cloneable, Comparable<VariableInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("VariableInfo");

  private static final org.apache.thrift.protocol.TField VARIABLE_VTU_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("variableVtuName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField VARIABLE_DISPLAY_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("variableDisplayName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField DOMAIN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("domainName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField VARIABLE_DOMAIN_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("variableDomainType", org.apache.thrift.protocol.TType.I32, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new VariableInfoStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new VariableInfoTupleSchemeFactory();

  public java.lang.String variableVtuName; // required
  public java.lang.String variableDisplayName; // required
  public java.lang.String domainName; // required
  /**
   * 
   * @see DomainType
   */
  public DomainType variableDomainType; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VARIABLE_VTU_NAME((short)1, "variableVtuName"),
    VARIABLE_DISPLAY_NAME((short)2, "variableDisplayName"),
    DOMAIN_NAME((short)3, "domainName"),
    /**
     * 
     * @see DomainType
     */
    VARIABLE_DOMAIN_TYPE((short)4, "variableDomainType");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // VARIABLE_VTU_NAME
          return VARIABLE_VTU_NAME;
        case 2: // VARIABLE_DISPLAY_NAME
          return VARIABLE_DISPLAY_NAME;
        case 3: // DOMAIN_NAME
          return DOMAIN_NAME;
        case 4: // VARIABLE_DOMAIN_TYPE
          return VARIABLE_DOMAIN_TYPE;
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
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VARIABLE_VTU_NAME, new org.apache.thrift.meta_data.FieldMetaData("variableVtuName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.VARIABLE_DISPLAY_NAME, new org.apache.thrift.meta_data.FieldMetaData("variableDisplayName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DOMAIN_NAME, new org.apache.thrift.meta_data.FieldMetaData("domainName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , "DomainName")));
    tmpMap.put(_Fields.VARIABLE_DOMAIN_TYPE, new org.apache.thrift.meta_data.FieldMetaData("variableDomainType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, DomainType.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(VariableInfo.class, metaDataMap);
  }

  public VariableInfo() {
  }

  public VariableInfo(
    java.lang.String variableVtuName,
    java.lang.String variableDisplayName,
    java.lang.String domainName,
    DomainType variableDomainType)
  {
    this();
    this.variableVtuName = variableVtuName;
    this.variableDisplayName = variableDisplayName;
    this.domainName = domainName;
    this.variableDomainType = variableDomainType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public VariableInfo(VariableInfo other) {
    if (other.isSetVariableVtuName()) {
      this.variableVtuName = other.variableVtuName;
    }
    if (other.isSetVariableDisplayName()) {
      this.variableDisplayName = other.variableDisplayName;
    }
    if (other.isSetDomainName()) {
      this.domainName = other.domainName;
    }
    if (other.isSetVariableDomainType()) {
      this.variableDomainType = other.variableDomainType;
    }
  }

  public VariableInfo deepCopy() {
    return new VariableInfo(this);
  }

  @Override
  public void clear() {
    this.variableVtuName = null;
    this.variableDisplayName = null;
    this.domainName = null;
    this.variableDomainType = null;
  }

  public java.lang.String getVariableVtuName() {
    return this.variableVtuName;
  }

  public VariableInfo setVariableVtuName(java.lang.String variableVtuName) {
    this.variableVtuName = variableVtuName;
    return this;
  }

  public void unsetVariableVtuName() {
    this.variableVtuName = null;
  }

  /** Returns true if field variableVtuName is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableVtuName() {
    return this.variableVtuName != null;
  }

  public void setVariableVtuNameIsSet(boolean value) {
    if (!value) {
      this.variableVtuName = null;
    }
  }

  public java.lang.String getVariableDisplayName() {
    return this.variableDisplayName;
  }

  public VariableInfo setVariableDisplayName(java.lang.String variableDisplayName) {
    this.variableDisplayName = variableDisplayName;
    return this;
  }

  public void unsetVariableDisplayName() {
    this.variableDisplayName = null;
  }

  /** Returns true if field variableDisplayName is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableDisplayName() {
    return this.variableDisplayName != null;
  }

  public void setVariableDisplayNameIsSet(boolean value) {
    if (!value) {
      this.variableDisplayName = null;
    }
  }

  public java.lang.String getDomainName() {
    return this.domainName;
  }

  public VariableInfo setDomainName(java.lang.String domainName) {
    this.domainName = domainName;
    return this;
  }

  public void unsetDomainName() {
    this.domainName = null;
  }

  /** Returns true if field domainName is set (has been assigned a value) and false otherwise */
  public boolean isSetDomainName() {
    return this.domainName != null;
  }

  public void setDomainNameIsSet(boolean value) {
    if (!value) {
      this.domainName = null;
    }
  }

  /**
   * 
   * @see DomainType
   */
  public DomainType getVariableDomainType() {
    return this.variableDomainType;
  }

  /**
   * 
   * @see DomainType
   */
  public VariableInfo setVariableDomainType(DomainType variableDomainType) {
    this.variableDomainType = variableDomainType;
    return this;
  }

  public void unsetVariableDomainType() {
    this.variableDomainType = null;
  }

  /** Returns true if field variableDomainType is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableDomainType() {
    return this.variableDomainType != null;
  }

  public void setVariableDomainTypeIsSet(boolean value) {
    if (!value) {
      this.variableDomainType = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VARIABLE_VTU_NAME:
      if (value == null) {
        unsetVariableVtuName();
      } else {
        setVariableVtuName((java.lang.String)value);
      }
      break;

    case VARIABLE_DISPLAY_NAME:
      if (value == null) {
        unsetVariableDisplayName();
      } else {
        setVariableDisplayName((java.lang.String)value);
      }
      break;

    case DOMAIN_NAME:
      if (value == null) {
        unsetDomainName();
      } else {
        setDomainName((java.lang.String)value);
      }
      break;

    case VARIABLE_DOMAIN_TYPE:
      if (value == null) {
        unsetVariableDomainType();
      } else {
        setVariableDomainType((DomainType)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VARIABLE_VTU_NAME:
      return getVariableVtuName();

    case VARIABLE_DISPLAY_NAME:
      return getVariableDisplayName();

    case DOMAIN_NAME:
      return getDomainName();

    case VARIABLE_DOMAIN_TYPE:
      return getVariableDomainType();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VARIABLE_VTU_NAME:
      return isSetVariableVtuName();
    case VARIABLE_DISPLAY_NAME:
      return isSetVariableDisplayName();
    case DOMAIN_NAME:
      return isSetDomainName();
    case VARIABLE_DOMAIN_TYPE:
      return isSetVariableDomainType();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof VariableInfo)
      return this.equals((VariableInfo)that);
    return false;
  }

  public boolean equals(VariableInfo that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_variableVtuName = true && this.isSetVariableVtuName();
    boolean that_present_variableVtuName = true && that.isSetVariableVtuName();
    if (this_present_variableVtuName || that_present_variableVtuName) {
      if (!(this_present_variableVtuName && that_present_variableVtuName))
        return false;
      if (!this.variableVtuName.equals(that.variableVtuName))
        return false;
    }

    boolean this_present_variableDisplayName = true && this.isSetVariableDisplayName();
    boolean that_present_variableDisplayName = true && that.isSetVariableDisplayName();
    if (this_present_variableDisplayName || that_present_variableDisplayName) {
      if (!(this_present_variableDisplayName && that_present_variableDisplayName))
        return false;
      if (!this.variableDisplayName.equals(that.variableDisplayName))
        return false;
    }

    boolean this_present_domainName = true && this.isSetDomainName();
    boolean that_present_domainName = true && that.isSetDomainName();
    if (this_present_domainName || that_present_domainName) {
      if (!(this_present_domainName && that_present_domainName))
        return false;
      if (!this.domainName.equals(that.domainName))
        return false;
    }

    boolean this_present_variableDomainType = true && this.isSetVariableDomainType();
    boolean that_present_variableDomainType = true && that.isSetVariableDomainType();
    if (this_present_variableDomainType || that_present_variableDomainType) {
      if (!(this_present_variableDomainType && that_present_variableDomainType))
        return false;
      if (!this.variableDomainType.equals(that.variableDomainType))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetVariableVtuName()) ? 131071 : 524287);
    if (isSetVariableVtuName())
      hashCode = hashCode * 8191 + variableVtuName.hashCode();

    hashCode = hashCode * 8191 + ((isSetVariableDisplayName()) ? 131071 : 524287);
    if (isSetVariableDisplayName())
      hashCode = hashCode * 8191 + variableDisplayName.hashCode();

    hashCode = hashCode * 8191 + ((isSetDomainName()) ? 131071 : 524287);
    if (isSetDomainName())
      hashCode = hashCode * 8191 + domainName.hashCode();

    hashCode = hashCode * 8191 + ((isSetVariableDomainType()) ? 131071 : 524287);
    if (isSetVariableDomainType())
      hashCode = hashCode * 8191 + variableDomainType.getValue();

    return hashCode;
  }

  @Override
  public int compareTo(VariableInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetVariableVtuName()).compareTo(other.isSetVariableVtuName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableVtuName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableVtuName, other.variableVtuName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetVariableDisplayName()).compareTo(other.isSetVariableDisplayName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableDisplayName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableDisplayName, other.variableDisplayName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetDomainName()).compareTo(other.isSetDomainName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDomainName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.domainName, other.domainName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetVariableDomainType()).compareTo(other.isSetVariableDomainType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableDomainType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableDomainType, other.variableDomainType);
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
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("VariableInfo(");
    boolean first = true;

    sb.append("variableVtuName:");
    if (this.variableVtuName == null) {
      sb.append("null");
    } else {
      sb.append(this.variableVtuName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("variableDisplayName:");
    if (this.variableDisplayName == null) {
      sb.append("null");
    } else {
      sb.append(this.variableDisplayName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("domainName:");
    if (this.domainName == null) {
      sb.append("null");
    } else {
      sb.append(this.domainName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("variableDomainType:");
    if (this.variableDomainType == null) {
      sb.append("null");
    } else {
      sb.append(this.variableDomainType);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (variableVtuName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableVtuName' was not present! Struct: " + toString());
    }
    if (variableDisplayName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableDisplayName' was not present! Struct: " + toString());
    }
    if (domainName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'domainName' was not present! Struct: " + toString());
    }
    if (variableDomainType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableDomainType' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class VariableInfoStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public VariableInfoStandardScheme getScheme() {
      return new VariableInfoStandardScheme();
    }
  }

  private static class VariableInfoStandardScheme extends org.apache.thrift.scheme.StandardScheme<VariableInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, VariableInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VARIABLE_VTU_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.variableVtuName = iprot.readString();
              struct.setVariableVtuNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // VARIABLE_DISPLAY_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.variableDisplayName = iprot.readString();
              struct.setVariableDisplayNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DOMAIN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.domainName = iprot.readString();
              struct.setDomainNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // VARIABLE_DOMAIN_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.variableDomainType = org.vcell.vcellij.api.DomainType.findByValue(iprot.readI32());
              struct.setVariableDomainTypeIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, VariableInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.variableVtuName != null) {
        oprot.writeFieldBegin(VARIABLE_VTU_NAME_FIELD_DESC);
        oprot.writeString(struct.variableVtuName);
        oprot.writeFieldEnd();
      }
      if (struct.variableDisplayName != null) {
        oprot.writeFieldBegin(VARIABLE_DISPLAY_NAME_FIELD_DESC);
        oprot.writeString(struct.variableDisplayName);
        oprot.writeFieldEnd();
      }
      if (struct.domainName != null) {
        oprot.writeFieldBegin(DOMAIN_NAME_FIELD_DESC);
        oprot.writeString(struct.domainName);
        oprot.writeFieldEnd();
      }
      if (struct.variableDomainType != null) {
        oprot.writeFieldBegin(VARIABLE_DOMAIN_TYPE_FIELD_DESC);
        oprot.writeI32(struct.variableDomainType.getValue());
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class VariableInfoTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public VariableInfoTupleScheme getScheme() {
      return new VariableInfoTupleScheme();
    }
  }

  private static class VariableInfoTupleScheme extends org.apache.thrift.scheme.TupleScheme<VariableInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, VariableInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.variableVtuName);
      oprot.writeString(struct.variableDisplayName);
      oprot.writeString(struct.domainName);
      oprot.writeI32(struct.variableDomainType.getValue());
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, VariableInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.variableVtuName = iprot.readString();
      struct.setVariableVtuNameIsSet(true);
      struct.variableDisplayName = iprot.readString();
      struct.setVariableDisplayNameIsSet(true);
      struct.domainName = iprot.readString();
      struct.setDomainNameIsSet(true);
      struct.variableDomainType = org.vcell.vcellij.api.DomainType.findByValue(iprot.readI32());
      struct.setVariableDomainTypeIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
