package digitale_stadt.cyclecity_a2;

/**
 * Created by alexutza_a on 29.04.2016.
 */
public class Sender {
    static Position position;

    public static String POST (Position pos){

        position = pos;

        return "" + position;
    }
}
