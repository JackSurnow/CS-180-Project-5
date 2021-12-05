import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static String time() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }
}
