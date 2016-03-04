package com.Lbins.TreeHm.dao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.Lbins.TreeHm.dao.RecordMsg;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RECORD_MSG.
*/
public class RecordMsgDao extends AbstractDao<RecordMsg, String> {

    public static final String TABLENAME = "RECORD_MSG";

    /**
     * Properties of entity RecordMsg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Mm_msg_id = new Property(0, String.class, "mm_msg_id", true, "MM_MSG_ID");
        public final static Property Mm_emp_id = new Property(1, String.class, "mm_emp_id", false, "MM_EMP_ID");
        public final static Property Mm_msg_type = new Property(2, String.class, "mm_msg_type", false, "MM_MSG_TYPE");
        public final static Property Mm_msg_title = new Property(3, String.class, "mm_msg_title", false, "MM_MSG_TITLE");
        public final static Property Mm_msg_content = new Property(4, String.class, "mm_msg_content", false, "MM_MSG_CONTENT");
        public final static Property Mm_msg_picurl = new Property(5, String.class, "mm_msg_picurl", false, "MM_MSG_PICURL");
        public final static Property Dateline = new Property(6, String.class, "dateline", false, "DATELINE");
        public final static Property Is_del = new Property(7, String.class, "is_del", false, "IS_DEL");
        public final static Property Is_top = new Property(8, String.class, "is_top", false, "IS_TOP");
        public final static Property Top_num = new Property(9, String.class, "top_num", false, "TOP_NUM");
        public final static Property Provinceid = new Property(10, String.class, "provinceid", false, "PROVINCEID");
        public final static Property Cityid = new Property(11, String.class, "cityid", false, "CITYID");
        public final static Property Countryid = new Property(12, String.class, "countryid", false, "COUNTRYID");
        public final static Property Area = new Property(13, String.class, "area", false, "AREA");
        public final static Property AccessToken = new Property(14, String.class, "accessToken", false, "ACCESS_TOKEN");
        public final static Property Mm_emp_mobile = new Property(15, String.class, "mm_emp_mobile", false, "MM_EMP_MOBILE");
        public final static Property Mm_emp_nickname = new Property(16, String.class, "mm_emp_nickname", false, "MM_EMP_NICKNAME");
        public final static Property Mm_emp_type = new Property(17, String.class, "mm_emp_type", false, "MM_EMP_TYPE");
        public final static Property Mm_emp_cover = new Property(18, String.class, "mm_emp_cover", false, "MM_EMP_COVER");
        public final static Property Mm_emp_company_type = new Property(19, String.class, "mm_emp_company_type", false, "MM_EMP_COMPANY_TYPE");
        public final static Property Mm_emp_company = new Property(20, String.class, "mm_emp_company", false, "MM_EMP_COMPANY");
        public final static Property Mm_level_id = new Property(21, String.class, "mm_level_id", false, "MM_LEVEL_ID");
        public final static Property Is_chengxin = new Property(22, String.class, "is_chengxin", false, "IS_CHENGXIN");
        public final static Property Is_miaomu = new Property(23, String.class, "is_miaomu", false, "IS_MIAOMU");
        public final static Property Mm_level_num = new Property(24, String.class, "mm_level_num", false, "MM_LEVEL_NUM");
        public final static Property Is_read = new Property(25, String.class, "is_read", false, "IS_READ");
    };

    private DaoSession daoSession;


    public RecordMsgDao(DaoConfig config) {
        super(config);
    }
    
    public RecordMsgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RECORD_MSG' (" + //
                "'MM_MSG_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: mm_msg_id
                "'MM_EMP_ID' TEXT," + // 1: mm_emp_id
                "'MM_MSG_TYPE' TEXT," + // 2: mm_msg_type
                "'MM_MSG_TITLE' TEXT," + // 3: mm_msg_title
                "'MM_MSG_CONTENT' TEXT," + // 4: mm_msg_content
                "'MM_MSG_PICURL' TEXT," + // 5: mm_msg_picurl
                "'DATELINE' TEXT," + // 6: dateline
                "'IS_DEL' TEXT," + // 7: is_del
                "'IS_TOP' TEXT," + // 8: is_top
                "'TOP_NUM' TEXT," + // 9: top_num
                "'PROVINCEID' TEXT," + // 10: provinceid
                "'CITYID' TEXT," + // 11: cityid
                "'COUNTRYID' TEXT," + // 12: countryid
                "'AREA' TEXT," + // 13: area
                "'ACCESS_TOKEN' TEXT," + // 14: accessToken
                "'MM_EMP_MOBILE' TEXT," + // 15: mm_emp_mobile
                "'MM_EMP_NICKNAME' TEXT," + // 16: mm_emp_nickname
                "'MM_EMP_TYPE' TEXT," + // 17: mm_emp_type
                "'MM_EMP_COVER' TEXT," + // 18: mm_emp_cover
                "'MM_EMP_COMPANY_TYPE' TEXT," + // 19: mm_emp_company_type
                "'MM_EMP_COMPANY' TEXT," + // 20: mm_emp_company
                "'MM_LEVEL_ID' TEXT," + // 21: mm_level_id
                "'IS_CHENGXIN' TEXT," + // 22: is_chengxin
                "'IS_MIAOMU' TEXT," + // 23: is_miaomu
                "'MM_LEVEL_NUM' TEXT," + // 24: mm_level_num
                "'IS_READ' TEXT);"); // 25: is_read
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RECORD_MSG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RecordMsg entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getMm_msg_id());
 
        String mm_emp_id = entity.getMm_emp_id();
        if (mm_emp_id != null) {
            stmt.bindString(2, mm_emp_id);
        }
 
        String mm_msg_type = entity.getMm_msg_type();
        if (mm_msg_type != null) {
            stmt.bindString(3, mm_msg_type);
        }
 
        String mm_msg_title = entity.getMm_msg_title();
        if (mm_msg_title != null) {
            stmt.bindString(4, mm_msg_title);
        }
 
        String mm_msg_content = entity.getMm_msg_content();
        if (mm_msg_content != null) {
            stmt.bindString(5, mm_msg_content);
        }
 
        String mm_msg_picurl = entity.getMm_msg_picurl();
        if (mm_msg_picurl != null) {
            stmt.bindString(6, mm_msg_picurl);
        }
 
        String dateline = entity.getDateline();
        if (dateline != null) {
            stmt.bindString(7, dateline);
        }
 
        String is_del = entity.getIs_del();
        if (is_del != null) {
            stmt.bindString(8, is_del);
        }
 
        String is_top = entity.getIs_top();
        if (is_top != null) {
            stmt.bindString(9, is_top);
        }
 
        String top_num = entity.getTop_num();
        if (top_num != null) {
            stmt.bindString(10, top_num);
        }
 
        String provinceid = entity.getProvinceid();
        if (provinceid != null) {
            stmt.bindString(11, provinceid);
        }
 
        String cityid = entity.getCityid();
        if (cityid != null) {
            stmt.bindString(12, cityid);
        }
 
        String countryid = entity.getCountryid();
        if (countryid != null) {
            stmt.bindString(13, countryid);
        }
 
        String area = entity.getArea();
        if (area != null) {
            stmt.bindString(14, area);
        }
 
        String accessToken = entity.getAccessToken();
        if (accessToken != null) {
            stmt.bindString(15, accessToken);
        }
 
        String mm_emp_mobile = entity.getMm_emp_mobile();
        if (mm_emp_mobile != null) {
            stmt.bindString(16, mm_emp_mobile);
        }
 
        String mm_emp_nickname = entity.getMm_emp_nickname();
        if (mm_emp_nickname != null) {
            stmt.bindString(17, mm_emp_nickname);
        }
 
        String mm_emp_type = entity.getMm_emp_type();
        if (mm_emp_type != null) {
            stmt.bindString(18, mm_emp_type);
        }
 
        String mm_emp_cover = entity.getMm_emp_cover();
        if (mm_emp_cover != null) {
            stmt.bindString(19, mm_emp_cover);
        }
 
        String mm_emp_company_type = entity.getMm_emp_company_type();
        if (mm_emp_company_type != null) {
            stmt.bindString(20, mm_emp_company_type);
        }
 
        String mm_emp_company = entity.getMm_emp_company();
        if (mm_emp_company != null) {
            stmt.bindString(21, mm_emp_company);
        }
 
        String mm_level_id = entity.getMm_level_id();
        if (mm_level_id != null) {
            stmt.bindString(22, mm_level_id);
        }
 
        String is_chengxin = entity.getIs_chengxin();
        if (is_chengxin != null) {
            stmt.bindString(23, is_chengxin);
        }
 
        String is_miaomu = entity.getIs_miaomu();
        if (is_miaomu != null) {
            stmt.bindString(24, is_miaomu);
        }
 
        String mm_level_num = entity.getMm_level_num();
        if (mm_level_num != null) {
            stmt.bindString(25, mm_level_num);
        }
 
        String is_read = entity.getIs_read();
        if (is_read != null) {
            stmt.bindString(26, is_read);
        }
    }

    @Override
    protected void attachEntity(RecordMsg entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RecordMsg readEntity(Cursor cursor, int offset) {
        RecordMsg entity = new RecordMsg( //
            cursor.getString(offset + 0), // mm_msg_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // mm_emp_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // mm_msg_type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // mm_msg_title
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // mm_msg_content
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // mm_msg_picurl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // dateline
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // is_del
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // is_top
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // top_num
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // provinceid
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // cityid
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // countryid
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // area
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // accessToken
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // mm_emp_mobile
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // mm_emp_nickname
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // mm_emp_type
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // mm_emp_cover
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // mm_emp_company_type
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // mm_emp_company
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // mm_level_id
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // is_chengxin
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // is_miaomu
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // mm_level_num
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25) // is_read
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RecordMsg entity, int offset) {
        entity.setMm_msg_id(cursor.getString(offset + 0));
        entity.setMm_emp_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMm_msg_type(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMm_msg_title(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMm_msg_content(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMm_msg_picurl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDateline(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIs_del(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIs_top(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTop_num(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setProvinceid(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCityid(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCountryid(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setArea(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAccessToken(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setMm_emp_mobile(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setMm_emp_nickname(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setMm_emp_type(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setMm_emp_cover(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setMm_emp_company_type(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setMm_emp_company(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setMm_level_id(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setIs_chengxin(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setIs_miaomu(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setMm_level_num(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setIs_read(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(RecordMsg entity, long rowId) {
        return entity.getMm_msg_id();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(RecordMsg entity) {
        if(entity != null) {
            return entity.getMm_msg_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
