package digitale_stadt.cyclecity_a2;

import android.location.Location;

import java.util.Date;

/**
 * Created by alexutza_a on 29.04.2016.
 */
public class Position {
    private long id;
    private int trackId;
    private String deviceId;
    private Date time;
    private double latitude;
    private double longitude;
    private double altitude;
    private int sent;

    //Standardkonstruktor

    public Position() {
    }

    //Konstruktor

    public Position(int trackId, String deviceId, Location location){
        this.trackId = trackId;
        this.deviceId = deviceId;
        time = new Date(location.getTime());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        sent = 0;
    }

    // Getter und Setter
    public int getSent() {return sent;}
    public void setSent(int sent) {this.sent = sent;}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public int getTrackId() {return trackId;}
    public void setTrackId(int trackId) {this.trackId = trackId;}

    public String getDeviceId() {return deviceId;}
    public void setDeviceId(String deviceId) {this.deviceId = deviceId;}

    public Date getTime() {return time;}
    public void setTime(Date time) {this.time = time;}

    public double getLatitude() {return latitude;}
    public void setLatitude(double latitude) {this.latitude = latitude;}

    public double getLongitude() {return longitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}

    public double getAltitude() {return altitude;}
    public void setAltitude(double altitude) {this.altitude = altitude;}
}
