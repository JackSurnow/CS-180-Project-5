import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class PostManager {
    private ForumServerThread forumServerThread;

    public PostManager(ForumServerThread forumServerThread) {
        this.forumServerThread = forumServerThread;
    }

    public void createPost(ArrayList<String> posts, ArrayList<String> postImport, String username) {
        String title = postImport.get(0);
        String description = postImport.get(1);

        if (title != null && !title.isEmpty() && description != null && !description.isEmpty()) {
            int newNum;
            String newPost;

            // checks if there are no posts yet
            if (posts == null || posts.isEmpty()) {
                posts = new ArrayList<>();
                newNum = 1;
            } else {

                // the new number is the last one plus 1
                newNum = Integer.parseInt(posts.get(posts.size() - 1).substring(posts.get(posts.size() - 1)
                        .indexOf(" ") + 1, posts.get(posts.size() - 1).indexOf(":"))) + 1;
            }
            newPost = String.format("Post %d: %s by %s at %s", newNum, title, username, Time.time());

            File f = new File(String.format("Post%d.txt", newNum));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write(newPost + "\n\n" + description + "\n\n" + "There are no replies.");
                ArrayList<String> post = new ArrayList<>();
                post.add(newPost);
                post.add("");
                post.add(description);
                post.add("");
                post.add("There are no replies.");
                posts.add(newPost);
                ForumServer.setPosts(posts);
                ExtraUtil.writePosts(posts);
                forumServerThread.sendPosts();
                ExtraUtil.writePost(post, String.format("Post%d.txt", newNum));
                forumServerThread.sendResponse(post, "GUI;" + username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void editPost(ArrayList<String> posts, ArrayList<String> post, String editMenu,
                         String change, String username) {
        int editPostNum = Integer.parseInt(post.get(0).substring(post.get(0)
                .indexOf(" ") + 1, post.get(0).indexOf(":")));
        int indexPostNum = 0;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).matches(post.get(0))) {
                indexPostNum = i;
            }

        }
        String fileName = String.format("Post%d.txt", Integer.parseInt(post.get(0).substring(post.get(0)
                .indexOf(" ") + 1, post.get(0).indexOf(":"))));

        switch (editMenu) {
            case "Change Title" -> {
                if (change != null && !change.isEmpty()) {
                    String newPost = String.format("Post %d: %s by %s at %s", editPostNum, change,
                            username, Time.time());
                    post.set(0, newPost);
                    posts.set(indexPostNum, newPost);
                    ForumServer.setPosts(posts);
                    ExtraUtil.writePosts(posts);
                    forumServerThread.sendPosts();
                    ExtraUtil.writePost(post, fileName);
                    forumServerThread.sendResponse(post, "GUI;" + username);
                }
            }
            case "Change Description" -> {

                if (change != null && !change.isEmpty()) {
                    post.set(2, change);
                    ExtraUtil.writePost(post, fileName);
                    forumServerThread.sendResponse(post, "GUI;" + username);
                }
            }
        }
    }

    public void deletePost(ArrayList<String> posts, ArrayList<String> post, String modifier) {
        int deletePost = Integer.parseInt(modifier.substring(0, modifier.indexOf(";")));
        String username= modifier.substring(modifier.indexOf(";") + 1);
        if (deletePost == JOptionPane.YES_OPTION) {
            for (int i = 0; i < posts.size(); i++) {
                int fileNum = Integer.parseInt(post.get(0).substring(post.get(0)
                                .indexOf(" ") + 1, post.get(0).indexOf(":")));
                if (posts.get(i).substring(0, posts.get(i).indexOf(":") + 1)
                        .contains(String.format("Post %d:", fileNum))) {
                    File f = new File(String.format("Post%d.txt", fileNum));
                    if (f.delete()) {
                        posts.remove(i);
                        ForumServer.setPosts(posts);
                        ExtraUtil.writePosts(posts);
                        forumServerThread.sendPosts();
                        forumServerThread.sendResponse(new ArrayList<>(), "GUI;" + username);
                        break;
                    }
                }
            }
        }
    }
}
