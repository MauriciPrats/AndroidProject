package multiplexaure.canvasapplication.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Multiplexor on 03/07/2015.
 */
public class MyDataAccess extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "project";
    private static final String USERS_TABLE_NAME = "user";
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + " (name TEXT PRIMARY KEY,password TEXT,color TEXT,avatar BLOB)";

    //No tiene primary key ya que se iran acumulando los logs de los usuarios
    private static final String USER_LOG_TABLE_NAME = "user_log";
    private static final String CREATE_USER_LOG_TABLE = "CREATE TABLE " + USER_LOG_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,password TEXT,time DATETIME DEFAULT CURRENT_TIMESTAMP)";

    //Rankings
    private static final String RANKINGS_TABLE_NAME = "ranking";
    private static final String CREATE_RANKINGS_TABLE = "CREATE TABLE " + RANKINGS_TABLE_NAME + " (username TEXT,points INTEGER,time DATETIME DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(username) REFERENCES "+USERS_TABLE_NAME+"(name))";



    public MyDataAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USER_LOG_TABLE);
        db.execSQL(CREATE_RANKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+USER_LOG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+RANKINGS_TABLE_NAME);
        onCreate(db);
    }

    public Boolean checkIsUserCorrect(User user){
        Boolean userCorrect = false;
        User userExists  = null;
        SQLiteDatabase db=this.getWritableDatabase();
        String[] arguments = new String[2];
        arguments[0] = user.name;
        arguments[1] = user.password;
        Cursor cursor = db.rawQuery("SELECT * FROM "+USERS_TABLE_NAME+" WHERE name == ? and password == ? ",arguments);
        if(cursor!=null){
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                userCorrect = true;
            }
            cursor.close();
        }
        db.close();
        return userCorrect;
    }

    public List<RankingUser> getRankings(){
        Boolean userCorrect = false;
        User userExists  = null;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RANKINGS_TABLE_NAME + " ORDER BY points DESC ", null);
        List<RankingUser> rankings = new ArrayList();
        if(cursor!=null){
            if(cursor.moveToFirst()) {
                RankingUser newRank = new RankingUser();
                newRank.name = cursor.getString(cursor.getColumnIndex("username"));
                newRank.points = cursor.getInt(cursor.getColumnIndex("points"));
                newRank.time = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("time")));
                rankings.add(newRank);
                while (cursor.moveToNext()) {
                    newRank = new RankingUser();
                    newRank.name = cursor.getString(cursor.getColumnIndex("username"));
                    newRank.points = cursor.getInt(cursor.getColumnIndex("points"));
                    newRank.time = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("time")));
                    rankings.add(newRank);
                }
            }
        }
        db.close();
        return rankings;
    }

    public void insertNewRanking(RankingUser newRanking){
        if(checkUserExists(newRanking.name)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("username", newRanking.name);
            cv.put("points", newRanking.points);
            db.insert(RANKINGS_TABLE_NAME, null, cv);
            db.close();
        }
    }

    public void deleteRankings(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RANKINGS_TABLE_NAME,null,null);
    }

    public Boolean checkUserExists(User user){
        return checkUserExists(user.name);
    }
    public Boolean checkUserExists(String username){
        Boolean userCorrect = false;
        User userExists  = null;
        SQLiteDatabase db=this.getWritableDatabase();
        String[] arguments = new String[1];
        arguments[0] = username;
        Cursor cursor = db.rawQuery("SELECT * FROM "+USERS_TABLE_NAME+" WHERE name == ? ",arguments);
        if(cursor!=null){
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                userCorrect = true;
            }
            cursor.close();
        }
        db.close();
        return userCorrect;
    }
    public void updateUser(User modifiedUser){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", modifiedUser.avatar);
        values.put("password", modifiedUser.password);
        values.put("color", modifiedUser.color);
        // updating row
        String[] arguments = {modifiedUser.name};
        db.update(USERS_TABLE_NAME, values, " name = ? ", arguments );

        System.out.println(modifiedUser.password+" "+modifiedUser.color+" "+modifiedUser.name+" "+modifiedUser.avatar);
        /*SQLiteDatabase db=this.getWritableDatabase();


        Cursor cursor;
        if(modifiedUser.avatar==null) {
            String[] arguments = {modifiedUser.password,modifiedUser.color,modifiedUser.name};
            cursor = db.rawQuery("UPDATE " + USERS_TABLE_NAME + " SET password = ?,color = ? WHERE name = ? ", arguments);
        }else{
            String[] arguments = {modifiedUser.password,modifiedUser.color,modifiedUser.avatar.toString(),modifiedUser.name};
            cursor = db.rawQuery("UPDATE " + USERS_TABLE_NAME + " SET password = ?,color = ?,avatar = ? WHERE name = ? ", arguments);
        }
        cursor.moveToFirst();
        cursor.close();*/
        db.close();
    }


    public Boolean insertNewUser(User user){
        //Si no existe el usuario lo creamos
        if(!checkUserExists(user)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("name", user.name);
            cv.put("password", user.password);
            cv.put("color", user.color);
            db.insert(USERS_TABLE_NAME, null, cv);
            db.close();
            return true;
        }
        return false;
    }

    public User getLastUserLogged(){
        User user  = null;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+USER_LOG_TABLE_NAME+" WHERE id == (SELECT MAX(id) FROM "+USER_LOG_TABLE_NAME+")",null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                user = new User();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                user.name = name;
                user.password = password;
            }
            cursor.close();
        }
        db.close();
        return user;
    }

    public User getUser(String name,String password){
        User user  = null;
        SQLiteDatabase db=this.getWritableDatabase();
        String[] arguments = {name,password};
        Cursor cursor = db.rawQuery("SELECT * FROM "+USERS_TABLE_NAME+" WHERE name == ? and password == ? ",arguments);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                user = new User();
                user.name = name;
                user.password = password;
                user.color = cursor.getString(cursor.getColumnIndex("color"));
                user.avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
            }
            cursor.close();
        }
        db.close();
        return user;
    }

    public User getLastUserLoggedComplete(){
        User userLoggedIncomplete = getLastUserLogged();
        if(userLoggedIncomplete!=null) {
            return getUser(userLoggedIncomplete.name, userLoggedIncomplete.password);
        }else{
            return null;
        }
    }

    public void setLastUserLogged(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("name",user.name);
        cv.put("password",user.password);
        db.insert(USER_LOG_TABLE_NAME, null, cv);
        db.close();
    }

    public int getBestPoints(User user){
        int bestPoints = 0;
        Boolean userCorrect = false;
        User userExists  = null;
        SQLiteDatabase db=this.getWritableDatabase();
        String[] parameters = {user.name};
        Cursor cursor = db.rawQuery("SELECT * FROM " + RANKINGS_TABLE_NAME + " WHERE username = ? ORDER BY points DESC ", parameters);
        List<RankingUser> rankings = new ArrayList();
        if(cursor!=null){
            if(cursor.moveToFirst()) {
               bestPoints = cursor.getInt(cursor.getColumnIndex("points"));
            }
        }
        return bestPoints;
    }

    public void logout(){
        User loggedOutUser = new User();
        loggedOutUser = getLastUserLogged();
        loggedOutUser.password = "";
        setLastUserLogged(loggedOutUser);
    }
}
