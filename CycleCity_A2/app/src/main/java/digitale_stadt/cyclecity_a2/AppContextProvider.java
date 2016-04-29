package digitale_stadt.cyclecity_a2;

import android.app.Application;
import android.content.Context;

/**
 * Created by alexutza_a on 29.04.2016.
 */
public class AppContextProvider extends Application {

    public static Context appContext;

    @Override
    public void onCreate(){
        super.onCreate();
        appContext= getApplicationContext();
    }


    public static Context getAppContext(){
        return appContext;
    }
}
