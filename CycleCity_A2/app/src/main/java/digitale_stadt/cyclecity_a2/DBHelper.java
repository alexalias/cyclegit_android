package digitale_stadt.cyclecity_a2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

/**
 * Created by alexutza_a on 29.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "CycleCity.db";

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    // TODO????
    public interface DatabaseHandler<T> {
        void onComplete(boolean success, T result);
    }


    private static abstract class DatabaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

        private DatabaseHandler<T> handler;
        private RuntimeException error;

        public DatabaseAsyncTask(DatabaseHandler<T> handler) {
            this.handler = handler;
        }

        @Override
        protected T doInBackground(Void... params) {
            try {
                return executeMethod();
            } catch (RuntimeException error) {
                this.error = error;
                return null;
            }
        }

        protected abstract T executeMethod();

        @Override
        protected void onPostExecute(T result) {
            handler.onComplete(error == null, result);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE positions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "trackId INTEGER," +
                "deviceId TEXT," +
                "timestamp INTEGER," +
                "latitude REAL," +
                "longitude REAL," +
                "altitude REAL," +
                "sent INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS positions;");
        onCreate(db);
        Log.d("DBHelper", "DB Upgrade, old version: " + oldVersion +
                ", new version: " + newVersion);
    }

    public void insertPosition(Position position) {

        ContentValues values = new ContentValues();
        values.put("trackId", position.getTrackId());
        values.put("deviceId", position.getDeviceId());
        values.put("time", position.getTime().getTime());
        values.put("latitude", position.getLatitude());
        values.put("longitude", position.getLongitude());
        values.put("altitude", position.getAltitude());
        values.put("sent", position.getSent());

        db.insertOrThrow("positions", null, values);
    }

    public void insertPositionAsync(final Position position, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                insertPosition(position);
                return null;
            }
        }.execute();
    }

    public Position selectPosition() {
        Position position = new Position();
        Cursor cursor = db.rawQuery("SELECT * FROM positions ORDER BY id LIMIT 1", null);

        try {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                position.setId(cursor.getLong(cursor.getColumnIndex("id")));
                position.setTrackId(cursor.getInt(cursor.getColumnIndex("trackId")));
                position.setDeviceId(cursor.getString(cursor.getColumnIndex("deviceId")));
                position.setTime(new Date(cursor.getLong(cursor.getColumnIndex("time"))));
                position.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                position.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                position.setAltitude(cursor.getDouble(cursor.getColumnIndex("altitude")));

            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
        return position;
    }

    public void selectPositionAsync(DatabaseHandler<Position> handler) {
        new DatabaseAsyncTask<Position>(handler) {
            @Override
            protected Position executeMethod() {
                return selectPosition();
            }
        }.execute();
    }

    public void deletePosition(long id) {
        if (db.delete("positions", "id = ?", new String[] { String.valueOf(id) }) != 1) {
            throw new SQLException();
        }
    }

    public void deletePositionAsync(final long id, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                deletePosition(id);
                return null;
            }
        }.execute();
    }
}
