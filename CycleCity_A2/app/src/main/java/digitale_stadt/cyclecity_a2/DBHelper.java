package digitale_stadt.cyclecity_a2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anne Lorenz on 27.04.2016.
 *
 * Hier wird eine SQLite Datenbank erzeugt, die eine Tabelle (positions)
 * mit den einzelnen GPS-POsitionen enthält.
 *
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "CycleCity.db";

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

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

        // add dummy gps positions
        db.execSQL("INSERT INTO positions (trackId, deviceId, timestamp, latitude, longitude, altitude, sent)" +
                "VALUES (1, 100, 1451649018000, 53.551085, 9.993682, 0.0, 0), " +
                "(1, 100, 1451649019000, 53.551090, 9.993692, 2.0, 0)");

        Log.d("DBHelper", "1. Objekt Datenbank: " + selectPosition().toString());
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

    // Gibt die erste Position in der Tabelle zurück.
    // TODO: BRAUCHEN WIR SO WAS ÜBERHAUPT???

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

    // Gibt eine Liste aller Positionsobjekte zurück,
    // die noch nicht an den Server verschickt worden sind.

    public ArrayList<Position> selectAllPositionsNotSent() {
        ArrayList<Position> positions = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM positions WHERE sent = 0 ORDER BY id", null);

        try {
            if (cursor.getCount() > 0) {

                Position pos = new Position();

                cursor.moveToFirst();
                pos.setId(cursor.getLong(cursor.getColumnIndex("id")));
                pos.setTrackId(cursor.getInt(cursor.getColumnIndex("trackId")));
                pos.setDeviceId(cursor.getString(cursor.getColumnIndex("deviceId")));
                pos.setTime(new Date(cursor.getLong(cursor.getColumnIndex("time"))));
                pos.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pos.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pos.setAltitude(cursor.getDouble(cursor.getColumnIndex("altitude")));

                positions.add(pos);
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
        return positions;
    }

    public void selectAllPositionsNotSentAsync(DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                selectAllPositionsNotSent();
                return null;
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
