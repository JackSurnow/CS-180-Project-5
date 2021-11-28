import java.io.*;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class PostReader { //Reads the master post class and individual post files

    public ArrayList<String> readPosts() { // Format posts from post.txt to arrayList posts
        File f = new File(Main.getPostsFile());
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

    public ArrayList<String> readPost(int postNumber) { // Reads the individual post
        String fileName = "Post" + postNumber + ".txt";
        ArrayList<String> post = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = bufferedReader.readLine();

            while (line != null) {
                post.add(line);
                line = bufferedReader.readLine();
            }

            return post;
        } catch (Exception e) {
            var failed = new ArrayList<String>();
            failed.add("There was an error reading this post, ensure the post file is correct!");
            e.printStackTrace();
            return failed;
        }
    }
}