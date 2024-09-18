package com.example.sqlitemodule;

import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteOpenHelper;

import android.content.Context;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
//    独立于类实例的静态变量，能够在构造函数内引用
    public static final int DATABASE_VERSION = 3;

//    public static final String SYSDATA =
//        "CREATE TABLE SysData (" +
//            "id INTEGER PRIMARY KEY," +
//            "TreatId TEXT," + // --关联的患者ID
//            "MacAddress TEXT NOT NULL," + // --泵的mac地址
//            "DeviceCode INTEGER NOT NULL," + // --泵号
//            "Hex TEXT NOT NULL," + // --十六进制原始数据
//            "Battery INTEGER NOT NULL," + //  --电量
//            "BcdTime TEXT NOT NULL," + //  --系统时间
//            "BoxVolume REAL NOT NULL," + //  --当前药量
//            "InfuseMode INTEGER NOT NULL," + //  --输注模式
//            "InfuseModeText TEXT NOT NULL," + //  --输注模式含义
//            "RemainTime TEXT NOT NULL," + //  --剩余时间
//            "BaseRate REAL NOT NULL," + //  --基础率
//            "DayTotled REAL NOT NULL," + //  --日已输注总量
//            "MaxBase REAL NOT NULL," + //  --基础量最大限制值
//            "MaxBolus REAL NOT NULL," + //  --大剂量最大限制值
//            "MaxDaily REAL NOT NULL," + //  --日总量最大限制值
//            "SysFlag  INTEGER NOT NULL," + //  --系统设置状态
//            "InsulinCOEF REAL NOT NULL," + //  --胰岛素敏感系数
//            "ChoCOEF REAL NOT NULL," + //  --CHO系数
//            "TargetBlood REAL NOT NULL," + //  --目标血糖值
//            "TestEn INTEGER NOT NULL," + //  --血糖测试提醒
//            "TestTime INTEGER NOT NULL," + //  --血糖测试时间
//            "ErrFlag INTEGER NOT NULL," + //  --报警代码
//            "CheckSum TEXT," + //  --校验和，APP不用管
//            "AlarmState INTEGER NOT NULL," + // -- 报警状态
//            "SetInsulinType TEXT," +
//            "SetInsulinTotal TEXT," +
//            "UploadDataTime TEXT," +
//            "LowVolumeAlarm INTEGER NOT NULL," + //  --低药量报警值
//            "CreateDate DATE NOT NULL," + //  --创建时间
//            "ErrNum INTEGER NOT NULL," + //  --报警记录编号
//            "BaseRateNum INTEGER NOT NULL," + //  --基础率记录编号
//            "GasedNum INTEGER NOT NULL," + //  --排气记录编号
//            "DayTotledNum INTEGER NOT NULL," + //  --日总量记录编号
//            "IsUploaded INTEGER NOT NULL" + //  --是否已上传, 0未上传, 1已上传
//        ")";
//
//    public static final String BasalRev =
//        "CREATE TABLE BasalRev (" +
//            "Id INTEGER PRIMARY KEY," +
//            "UserId TEXT," + // --住院号
//            "DeviceCode INTEGER NOT NULL," + // --泵号
//            "BaseRate REAL NOT NULL," + // --基础率
//            "RecordTime DATE NOT NULL," + // --记录时间
//            "Hex TEXT NOT NULL," + // --十六进制原始数据
//            "CreateDate DATE," + // --创建时间
//            "IsUploaded TINYINT NOT NULL" + // --是否已上传, 0未上传, 1已上传
//        ")";
//
//    public static final String DailyRev =
//        "CREATE TABLE DailyRev (" +
//            "Id INTEGER PRIMARY KEY," +
//            "UserId TEXT ," + // --住院号
//            "DeviceCode INTEGER NOT NULL," + // --泵号
//            "DayTotled REAL NOT NULL," + // --日总量
//            "Totled1 REAL NOT NULL," + //--24小时输注量-1
//            "Totled2 REAL NOT NULL," + //--24小时输注量-2
//            "Totled3 REAL NOT NULL," + //--24小时输注量-3
//            "Totled4 REAL NOT NULL," + //--24小时输注量-4
//            "Totled5 REAL NOT NULL," + //--24小时输注量-5
//            "Totled6 REAL NOT NULL," + //--24小时输注量-6
//            "Totled7 REAL NOT NULL," + //--24小时输注量-7
//            "Totled8 REAL NOT NULL," + //--24小时输注量-8
//            "Totled9 REAL NOT NULL," + //--24小时输注量-9
//            "Totled10 REAL NOT NULL," + // --24小时输注量-10
//            "Totled11 REAL NOT NULL," + // --24小时输注量-11
//            "Totled12 REAL NOT NULL," + // --24小时输注量-12
//            "Totled13 REAL NOT NULL," + // --24小时输注量-13
//            "Totled14 REAL NOT NULL," + // --24小时输注量-14
//            "Totled15 REAL NOT NULL," + // --24小时输注量-15
//            "Totled16 REAL NOT NULL," + // --24小时输注量-16
//            "Totled17 REAL NOT NULL," + // --24小时输注量-17
//            "Totled18 REAL NOT NULL," + // --24小时输注量-18
//            "Totled19 REAL NOT NULL," + // --24小时输注量-19
//            "Totled20 REAL NOT NULL," + // --24小时输注量-20
//            "Totled21 REAL NOT NULL," + // --24小时输注量-21
//            "Totled22 REAL NOT NULL," + // --24小时输注量-22
//            "Totled23 REAL NOT NULL," + // --24小时输注量-23
//            "Totled24 REAL NOT NULL," + // --24小时输注量-24
//            "RecordTime DATE NOT NULL," + // --记录时间
//            "Hex TEXT NOT NULL," + // --十六进制原始数据
//            "CreateDate DATE," + // --创建时间
//            "IsUploaded TINYINT NOT NULL" + // --是否已上传, 0未上传, 1已上传
//        ")";
//
//    public static final String FillRev =
//        "CREATE TABLE FillRev (" +
//            "Id INTEGER PRIMARY KEY," +
//            "UserId TEXT," + //--住院号
//            "DeviceCode INTEGER NOT NULL," + // --泵号
//            "GasedVolume REAL NOT NULL," + // --排气值
//            "RecordTime DATE NOT NULL," + // --记录时间
//            "Hex TEXT NOT NULL," + // --十六进制原始数据
//            "CreateDate DATE," + // --创建时间
//            "IsUploaded TINYINT NOT NULL" + // --是否已上传, 0未上传, 1已上传
//        ")";
//
//    public static final String AlarmRev =
//        "CREATE TABLE AlarmRev (" +
//            "Id INTEGER PRIMARY KEY," +
//            "UserId TEXT ," + // --住院号
//            "DeviceCode INTEGER NOT NULL," + // --泵号
//            "ErrFlag INTEGER NOT NULL," + // --报警代码
//            "ErrFlagText TEXT NOT NULL," + // --报警信息
//            "RecordTime DATE NOT NULL," + // --记录时间
//            "Hex TEXT NOT NULL," + // --十六进制原始数据
//            "CreateDate DATE," + // --创建时间
//            "IsUploaded TINYINT NOT NULL" + // --是否已上传, 0未上传, 1已上传
//        ")";
//
//
//    public static final String LogRev =
//        "CREATE TABLE Log (" +
//            "Id INTEGER PRIMARY KEY," +
//            "Type TINYINT NOT NULL," + // --日志类型 (0: BLE, 1: SIGNALR, 2: NETWORK, 3: SYSTEM, 4: SQL, 5: APP)"
//            "Level TINYINT NOT NULL," + //--日志级别 (0: DEBUG, 1: INFO, 2: WARNING, 3: ERROR)
//            "APPDeviceId TEXT NOT NULL," + //--Uniapp产生的设备唯一标记
//            "Cid TEXT," + //--客户端推送标识push_clientid
//            "DeviceCode INTEGER NOT NULL," + //--泵号
//            "MacAddress TEXT NOT NULL," + //--MAC地
//            "UserId TEXT," + //--住院号
//            "Data TEXT NOT NULL," + //--日志内容
//            "RecordTime DATE NOT NULL," + //--创建时间（毫秒数）
//            "Url TEXT," + //--页面路由
//            "WifiEnabled TINYINT NOT NULL," + // --Wifi是否开启
//            "BluetoothEnabled TINYINT NOT NULL," + // --蓝牙是否开启
//            "LocationEnabled TINYINT NOT NULL," + // --定位是否开启
//            "PermissionError TEXT," + // --蓝牙、WIFI、定位没有权限或配置错误
//            "IsUploaded TINYINT NOT NULL" +  // --是否已上传, 0未上传, 1已上传
//        ")";
//
//
//    public static final String LowVolumeAlarmRev =
//        "CREATE TABLE LowVolumeAlarmRev (" +
//            "Id INTEGER PRIMARY KEY," +
//            "UserId TEXT," +
//            "DeviceCode INTEGER NOT NULL," +
//            "LowVolumeAlarm INTEGER NOT NULL," + // --低药量报警设置值
//            "RecordTime DATE NOT NULL," +
//            "Hex TEXT NOT NULL," +
//            "CreateDate DATE," +
//            "IsUploaded TINYINT NOT NULL" +
//        ")";

    public DatabaseHelper(Context context, String DatabasePath, String password) {
        super(context, DatabasePath, password, null, DATABASE_VERSION, 0, null, null, false);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Database", "onCreate: ");
//        创建数据表
//        db.execSQL(SYSDATA);
//        db.execSQL(BasalRev);
//        db.execSQL(DailyRev);
//        db.execSQL(FillRev);
//        db.execSQL(AlarmRev);
//        db.execSQL(LogRev);
//        db.execSQL(LowVolumeAlarmRev);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}

