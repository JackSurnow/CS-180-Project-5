import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class ExtraUtil {

    // return the timestamp
    public static String time() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static ArrayList<String> readAccounts() {
        File f = new File(Main.getAccountsFile());
        if (!f.exists() || !f.isFile()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
            ArrayList<String> account = new ArrayList<>();
            String line = bf.readLine();

            while (line != null) {
                account.add(line);
                line = bf.readLine();
            }
            return account;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void writeAccounts(ArrayList<String> accounts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Main.getAccountsFile()))) {
            for (int i = 0; i < accounts.size(); i++) {
                bw.write(accounts.get(i));
                if (i < accounts.size() - 1) {
                    bw.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readVotes() {
        File f = new File(Main.getVotesFile());
        if (!f.exists() || !f.isFile()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
            ArrayList<String> votes = new ArrayList<>();
            String line = bf.readLine();

            while (line != null) {
                votes.add(line);
                line = bf.readLine();
            }
            return votes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
