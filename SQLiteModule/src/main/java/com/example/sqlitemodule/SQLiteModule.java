package com.example.sqlitemodule;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.adapter.URIAdapter;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zetetic.database.DatabaseErrorHandler;
import net.zetetic.database.sqlcipher.SQLiteConnection;
import net.zetetic.database.sqlcipher.SQLiteDatabase;
import net.zetetic.database.sqlcipher.SQLiteDatabaseHook;


// 一定要继承UniModule
public class SQLiteModule extends UniModule {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database_r;
    private SQLiteDatabase database_w;
    private CountDownTimer countdownTimer_r;
    private CountDownTimer countdownTimer_w;

    /**
     *
     * @param path
     * @param password
     */
    @UniJSMethod(uiThread = false)
    public void init(String path, String password, UniJSCallback callback) {
//        在使用之前，加载SQLCipher
        System.loadLibrary("sqlcipher");

        if(mUniSDKInstance.getContext() != null && mUniSDKInstance.getContext() instanceof Activity) {
            Context context =  mUniSDKInstance.getContext();
            Uri uri = mUniSDKInstance.rewriteUri(Uri.parse(path), URIAdapter.FILE);

            try {
                // 初始化databaseHelper实例
                dbHelper = new DatabaseHelper(context, uri.getPath(), password);

                callback.invoke(new JSONObject() {{
                    put("status", true);
                }});
            } catch (Exception e){
                e.printStackTrace();
                callback.invoke(new JSONObject() {{
                    put("status", false);
                    put("errMsg", e.toString());
                }});
            }
        }
    }

    /**
     * 开启数据库，当长时间没有再次开启时关闭数据库
     * @param isReadOnly
     * @param callback
     */
    @UniJSMethod(uiThread = false)
    public void openDB(Boolean isReadOnly, UniJSCallback callback) {
        // 在使用之前，加载SQLCipher
        System.loadLibrary("sqlcipher");

        final SQLiteDatabase database = isReadOnly ? database_r : database_w;

        try {
            if( database == null || (database.isReadOnly() == isReadOnly && !database.isOpen()) ) {
                Log.i("Database", "database is not open" + isReadOnly);

                if(isReadOnly) {
                    database_r = dbHelper.getReadableDatabase();

                    if(countdownTimer_r != null) countdownTimer_r.cancel();
                    countdownTimer_r = new CountDownTimer(30 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            Log.i("Database", "1 database.close" + isReadOnly);
                            if(database_r.isOpen())
                                database_r.close();
                        }
                    }.start();
                } else {
                    database_w = dbHelper.getWritableDatabase();

                    if(countdownTimer_w != null) countdownTimer_w.cancel();
                    countdownTimer_w = new CountDownTimer(30 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            Log.i("Database", "1 database.close" + isReadOnly);
                            if(database_w.isOpen())
                                database_w.close();
                        }
                    }.start();
                }

                callback.invoke(new JSONObject() {{
                    put("status", true);
                }});

            } else {
                Log.i("Database", "database is opened");

                callback.invoke(new JSONObject() {{
                    put("status", true);
                }});

                if(isReadOnly) {
                    countdownTimer_r.cancel();
                    countdownTimer_r = new CountDownTimer(30 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            Log.i("Database", "2 database.close" + isReadOnly);
                            if(database_r.isOpen())
                                database_r.close();
                        }
                    }.start();
                } else {
                    countdownTimer_w.cancel();
                    countdownTimer_w = new CountDownTimer(30 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            Log.i("Database", "2 database.close" + isReadOnly);
                            if(database_w.isOpen())
                                database_w.close();
                        }
                    }.start();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(new JSONObject() {{
                put("status", false);
                put("errMsg", e.toString());
            }});
        }
    }

    /**
     * 执行增删改操作
     * @param sql
     */
    @UniJSMethod(uiThread = false)
    public void execSQL(String sql, Object[] bingArgs, UniJSCallback callback) {
        // 在使用之前，加载SQLCipher
        System.loadLibrary("sqlcipher");

        try {
            database_w.execSQL(sql, bingArgs);
            callback.invoke(new JSONObject() {{
                put("status", true);
            }});
        } catch (Exception e){
            e.printStackTrace();
            callback.invoke(new JSONObject() {{
                put("status", false);
                put("errMsg", e.toString());
            }});
        }
    }

    /**
     * 执行查询操作
     * @param sql
     */
    @UniJSMethod(uiThread = false)
    public void execQuerySQL(String sql, String[] selectionArgs, UniJSCallback callback) {
        // 在使用之前，加载SQLCipher
        System.loadLibrary("sqlcipher");

        try {
            Cursor cursor = database_r.rawQuery(sql, selectionArgs);
            List<Map<String, Object>> resultList = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    int type = cursor.getType(i);
                    switch (type) {
                        case Cursor.FIELD_TYPE_NULL:
                            row.put(columnName, null);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            row.put(columnName, cursor.getLong(i));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            row.put(columnName, cursor.getDouble(i));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            row.put(columnName, cursor.getString(i));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            row.put(columnName, cursor.getBlob(i));
                            break;
                    }
                }
                resultList.add(row);
            }
            cursor.close();
            callback.invoke(new JSONObject() {{
                put("status", true);
                put("data", resultList);
            }});
        } catch (Exception e){
            e.printStackTrace();
            callback.invoke(new JSONObject() {{
                put("status", false);
                put("errMsg", e.toString());
            }});
        }
    }


    @UniJSMethod(uiThread = false)
    public void exportDB(String exportPath, UniJSCallback callback) {

        try {
            if(mUniSDKInstance.getContext() != null && mUniSDKInstance.getContext() instanceof Activity) {
                Context context = mUniSDKInstance.getContext();
                Uri uri = mUniSDKInstance.rewriteUri(Uri.parse(exportPath), URIAdapter.FILE);

                File dbFile = context.getDatabasePath("MicroPump.db");
                File target = new File(uri.getPath());

                // 检查目标文件夹是否存在，如果不存在则创建
                if (!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }

                FileInputStream fis = new FileInputStream(dbFile);
                FileOutputStream fos = new FileOutputStream(target);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
                fos.close();
                fis.close();

                callback.invoke(new JSONObject() {{
                    put("status", true);
                }});

            } else {
                callback.invoke(new JSONObject() {{
                    put("status", false);
                    put("errMsg", "无法获取context");
                }});
            }

        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(new JSONObject() {{
                put("status", false);
                put("errMsg", e.toString());
            }});
        }
    }

//    public class TestModule extends UniModule {
//        // 使用UniJSMethod注解，才能使用js调用
//        @UniJSMethod(uiThread = true)
//        public void  add (JSONObject json, UniJSCallback callback) {
//            final int a = json.getInteger("a");
//            final int b = json.getInteger("b");
//            callback.invoke(new JSONObject() {{
//                put("code", 0);
//                put("result", a + b);
//            }});
//        }
//    }
}
