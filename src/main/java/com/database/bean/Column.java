package com.database.bean;

/**
 * Created by udbwcso on 2015/7/20.
 */
public class Column {
    /**
     * TABLE_CAT String => table catalog (may be <code>null</code>)
     */
    private String tableCat;

    /**
     * TABLE_SCHEM String => table schema (may be <code>null</code>)
     */
    private String tableSchem;

    /**
     * TABLE_NAME String => table name
     */
    private String tableName;

    /**
     * COLUMN_NAME String => column name
     */
    private String columnName;

    /**
     * DATA_TYPE int => SQL type from java.sql.Types
     */
    private int dataType;

    /**
     * TYPE_NAME String => Data source dependent type name,
     * for a UDT the type name is fully qualified
     */
    private String typeName;

    /**
     * COLUMN_SIZE int => column size.
     */
    private int columnSize;

    /**
     * BUFFER_LENGTH is not used.
     */
    private Integer bufferLength;

    /**
     * DECIMAL_DIGITS int => the number of fractional digits.
     * Null is returned for data types where DECIMAL_DIGITS is not applicable.
     */
    private Integer decimalDigits;

    /**
     * NUM_PREC_RADIX int => Radix (typically either 10 or 2)
     */
    private Integer numPrecRadix;

    /**
     * NULLABLE int => is NULL allowed.
     * columnNoNulls - might not allow <code>NULL</code> values
     * columnNullable - definitely allows <code>NULL</code> values
     * columnNullableUnknown - nullability unknown
     */
    private Integer nullAble;

    /**
     * REMARKS String => comment describing column (may be <code>null</code>)
     */
    private String remarks;

    /**
     * COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be <code>null</code>)
     */
    private String columnDef;

    /**
     * SQL_DATA_TYPE int => unused
     */
    private Integer sqlDataType;

    /**
     * SQL_DATETIME_SUB int => unused
     */
    private Integer sql_datetime_sub;

    /**
     * CHAR_OCTET_LENGTH int => for char types the
     * maximum number of bytes in the column
     */
    private Integer charOctetLength;

    /**
     * ORDINAL_POSITION int => index of column in table(starting at 1)
     */
    private Integer ordinalPosition;

    /**
     * IS_NULLABLE String  => ISO rules are used to determine the nullability for a column.
     * YES           --- if the column can include NULLs
     * NO            --- if the column cannot include NULLs
     * empty string  --- if the nullability for the column is unknown
     */
    private String isNullable;

    /**
     * SCOPE_CATALOG String => catalog of table that is the scope
     * of a reference attribute (<code>null</code> if DATA_TYPE isn't REF)
     */
    private String scopeCatalog;

    /**
     * SCOPE_SCHEMA String => schema of table that is the scope
     * of a reference attribute (<code>null</code> if the DATA_TYPE isn't REF)
     */
    private String scopeSchema;

    /**
     * SCOPE_TABLE String => table name that this the scope
     * of a reference attribute (<code>null</code> if the DATA_TYPE isn't REF)
     */
    private String scopeTable;

    /**
     * SOURCE_DATA_TYPE short => source type of a distinct type or user-generated
     * Ref type, SQL type from java.sql.Types (<code>null</code> if DATA_TYPE
     * isn't DISTINCT or user-generated REF)
     */
    private int sourceDataType;

    /**
     * IS_AUTOINCREMENT String  => Indicates whether this column is auto incremented
     * YES           --- if the column is auto incremented
     * NO            --- if the column is not auto incremented
     * empty string  --- if it cannot be determined whether the column is auto incremented
     */
    private String isAutoincrement;

    /**
     * IS_GENERATEDCOLUMN String  => Indicates whether this is a generated column
     * YES           --- if this a generated column
     * NO            --- if this not a generated column
     * empty string  --- if it cannot be determined whether this is a generated column
     */
    private String isGeneratedColumn;

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public Integer getNullAble() {
        return nullAble;
    }

    public void setNullAble(Integer nullAble) {
        this.nullAble = nullAble;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public Integer getSql_datetime_sub() {
        return sql_datetime_sub;
    }

    public void setSql_datetime_sub(Integer sql_datetime_sub) {
        this.sql_datetime_sub = sql_datetime_sub;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public int getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(int sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public String getIsGeneratedColumn() {
        return isGeneratedColumn;
    }

    public void setIsGeneratedColumn(String isGeneratedColumn) {
        this.isGeneratedColumn = isGeneratedColumn;
    }
}
