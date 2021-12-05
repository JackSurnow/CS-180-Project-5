import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Purdue University -- CS18000
 * Project 5
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class Time {
    public static String time() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }
}
