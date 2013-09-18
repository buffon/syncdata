package com.chen.sync.database;

import android.content.Context;
import com.chen.sync.database.base.BaseDao;
import com.chen.sync.domain.Sync;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13-9-18
 * Time: 下午4:30
 * To change this template use File | Settings | File Templates.
 */
public class DaoImpl extends BaseDao {

    public Timestamp getLatestUpdateTime(){
        return null;
    }

    public void save(Context context,Sync c){
        try {
            getDao(context,Sync.class).create(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
