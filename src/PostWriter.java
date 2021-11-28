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

public class PostWriter {
    public static void writePost(ArrayList<String> post, String fileName) {
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

    public static void writePosts(ArrayList<String> posts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Main.getPostsFile()))) {
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