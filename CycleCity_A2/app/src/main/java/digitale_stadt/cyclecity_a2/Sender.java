package digitale_stadt.cyclecity_a2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexutza_a on 29.04.2016.
 */
public class Sender {
     static Position position;

    public static String POST (Position pos){

        position = pos;

        String json = "";

        //Build JSON object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Timestamp", position.getTime());
            jsonObject.accumulate("Latitude", position.getLatitude());
            jsonObject.accumulate("Longitude", position.getLongitude());
            jsonObject.accumulate("Altitude", position.getAltitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Convert jsonObject to String
        json = jsonObject.toString();

        return json;
    }
}
