package com.ps.wb.base.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// exportSchema = false 尽量写，内部需要检测，如果没有写，会抛出异常，因为内部要记录升级的所有副本
@Database(entities = {Address.class,Order.class,Message.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class
                    , "AppDB")

                    // 可以强制在主线程运行数据库操作
                    .allowMainThreadQueries()

                    // 暴力升级 不管三七二十一 强制执行（数据会丢失）(慎用)
                    // .fallbackToDestructiveMigration()

                    // 稳定的方式升级
//                    .addMigrations(MIGRATION_1_2)

                    .build();
        }
        return instance;
    }
    public abstract MessageDao messageDao();
    public abstract AddressDao addressDao();
    public abstract OrderDao orderDao();
    // 下面是稳定升级的方式
//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // 在这里用SQL脚本完成数据的变化
//            database.execSQL("alter table student add column flag integer not null default 1");
//        }
//    };

    // ROOM 是不能降级的，我非要删除一个字段，却要保证数据的稳定性，这个是特殊情况
    // 特殊手法降级
//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            // SQL 四步法
//
//            // 1.先建立临时表
//            //  database.execSQL("create table student_temp (uid integer primary key not null,name text,pwd text,addressId)");
//
//            // 2.把之前表的数据（SQL语句的细节，同学们可以上网查询下）
//            // database.execSQL("insert into student_temp (uid,name,pwd,addressid) " + " select uid,name,pwd,addressid from student");
//
//            // 3.删除student 旧表
//            // database.execSQL("drop table student");
//
//            // 4.修改 临时表 为 新表 student
//            // database.execSQL("alter table student_temp rename to student");
//        }
//    };

    //
}
