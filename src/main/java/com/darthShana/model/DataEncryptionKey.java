package com.darthShana.model;

import io.ebean.annotation.DbDefault;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "data_encryption_key")
public class DataEncryptionKey extends BaseModel {

    @NotNull
    private String tableName;
    @NotNull
    private String columnName;
    @NotNull
    private byte[] dataEncryptionKey;
    @NotNull
    private String kekId;

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

    public byte[] getDataEncryptionKey() {
        return dataEncryptionKey;
    }

    public void setDataEncryptionKey(byte[] dataEncryptionKey) {
        this.dataEncryptionKey = dataEncryptionKey;
    }

    public String getKekId() {
        return kekId;
    }

    public void setKekId(String kekId) {
        this.kekId = kekId;
    }
}
