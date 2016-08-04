package com.database.bean;

import java.util.List;

/**
 * Created by udbwcso on 2015/7/20.
 */
public class Table {

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
     * TABLE_TYPE String => table type.
     * Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
     * "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     */
    private String tableType;

    /**
     * REMARKS String => explanatory comment on the table
     */
    private String remarks;

    /**
     * TYPE_CAT String => the types catalog (may be <code>null</code>)
     */
    private String typeCat;

    /**
     * TYPE_SCHEM String => the types schema (may be <code>null</code>)
     */
    private String typeSchem;

    /**
     * TYPE_NAME String => type name (may be <code>null</code>)
     */
    private String typeName;

    /**
     * SELF_REFERENCING_COL_NAME String => name of the designated
     * "identifier" column of a typed table (may be <code>null</code>)
     */
    private String selfReferencingColName;

    /**
     * REF_GENERATION String => specifies how values in
     * SELF_REFERENCING_COL_NAME are created. Values are
     * "SYSTEM", "USER", "DERIVED". (may be <code>null</code>)
     */
    private String refGeneration;

    private List<Column> columnList;

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneration() {
        return refGeneration;
    }

    public void setRefGeneration(String refGeneration) {
        this.refGeneration = refGeneration;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }
}
