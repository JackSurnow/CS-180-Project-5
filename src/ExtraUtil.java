import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Purdue University -- CS18000
 * Project 5
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class ExtraUtil {
    public synchronized static ArrayList<String> readAccounts() {
        File f = new File(ForumServer.getAccountsFile());
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

    public synchronized static void writeAccounts(ArrayList<String> accounts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ForumServer.getAccountsFile()))) {
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

    public synchronized static ArrayList<String> readVotes() {
        File f = new File(ForumServer.getVotesFile());
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

    public synchronized static void writeVotes(ArrayList<String> votes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ForumServer.getVotesFile()))) {
            for (int i = 0; i < votes.size(); i++) {
                bw.write(votes.get(i));
                if (i < votes.size() - 1) {
                    bw.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static ArrayList<String> readPosts() { // Format posts from post.txt to arrayList posts
        File f = new File(ForumServer.getPostsFile());
        if (!f.exists() || !f.isFile()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
            String line = bufferedReader.readLine();
            ArrayList<String> posts = new ArrayList<>();

            while (line != null) {
                posts.add(line);
                line = bufferedReader.readLine();
            }

            return posts;
        } catch (Exception e) {
            var failed = new ArrayList<String>();
            failed.add("There was an error reading the posts, ensure the posts file is correct!");
            e.printStackTrace();
            return failed;
        }
    }

    public synchronized static void writePost(ArrayList<String> post, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < post.size(); i++) {
                bw.write(post.get(i));
                if (i < post.size() - 1) {
                    bw.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void writePosts(ArrayList<String> posts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ForumServer.getPostsFile()))) {
            for (int i = 0; i < posts.size(); i++) {
                bw.write(posts.get(i));
                if (i < posts.size() - 1) {
                    bw.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
