package com.chen.sync.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13-9-18
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "sync")
public class Sync {

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private Timestamp updatetime;
    @DatabaseField
    private Character optype;
    @DatabaseField
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    public Character getOpType() {
        return optype;
    }

    public void setOpType(Character optype) {
        this.optype = optype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
