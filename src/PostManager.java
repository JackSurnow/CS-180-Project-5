import javax.swing.*;
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

public class PostManager {
    private Main main;

    public PostManager(Main main) {
        this.main = main;
    }

    public boolean createPost(ArrayList<String> posts, ArrayList<String> postImport, boolean imported) {
        String title = null;
        String description = null;

        if (imported) {
            title = postImport.get(0);
            description = postImport.get(1);
        } else {
            title = JOptionPane.showInputDialog(null, "What is the new title?",
                    "Post Forum", JOptionPane.QUESTION_MESSAGE);
            description = JOptionPane.showInputDialog(null, "What is the new description?",
                    "Post Forum", JOptionPane.QUESTION_MESSAGE);
        }

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
            newPost = String.format("Post %d: %s by %s at %s", newNum, title, main.getUsername(), ExtraUtil.time());

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
                PostWriter.writePosts(posts);
                Main.setPosts(posts);
                main.setPost(post);
                return true;
            } catch (IOException e) {
                main.setPost(null);
                return false;

            }
        }
        main.setPost(null);
        return false;
    }

    public boolean editPost(ArrayList<String> posts, ArrayList<String> post) {
        if (!post.isEmpty()) {
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

            String[] options = {"Change Title", "Change Description"};
            String editMenu = (String) JOptionPane.showInputDialog(null, "What do you want to edit?",
                    "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);

            // ArrayList<String> post = PostReader.readPost(editPostNum);

            switch (editMenu) {
                case "Change Title" -> {
                    String title = JOptionPane.showInputDialog(null, "What is the new title?",
                            "Post Forum", JOptionPane.QUESTION_MESSAGE);
                    if (title != null && !title.isEmpty()) {
                        String newPost = String.format("Post %d: %s by %s at %s", editPostNum, title,
                                main.getUsername(), ExtraUtil.time());
                        post.set(0, newPost);
                        posts.set(indexPostNum, newPost);
                        Main.setPosts(posts);
                        PostWriter.writePosts(posts);
                        PostWriter.writePost(post, fileName);
                        main.setPost(post);
                        return true;
                    }
                }
                case "Change Description" -> {
                    String description = JOptionPane.showInputDialog(null, "What is the new description?",
                            "Post Forum", JOptionPane.QUESTION_MESSAGE);
                    if (description != null && !description.isEmpty()) {
                        post.set(2, description);
                        PostWriter.writePost(post, fileName);
                        main.setPost(post);
                        return true;
                    }
                }
                default -> {
                    main.setPost(null);
                    return false;
                }
            }
        }
        main.setPost(null);
        return false;
    }

    public boolean deletePost(ArrayList<String> posts, ArrayList<String> post) {
        int deletePostNum = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Post?",
                    "Post Forum", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (deletePostNum == JOptionPane.YES_OPTION) {
            for (int i = 0; i < posts.size(); i++) {
                int fileNum = Integer.parseInt(post.get(0).substring(post.get(0)
                                .indexOf(" ") + 1, post.get(0).indexOf(":")));
                if (posts.get(i).substring(0, posts.get(i).indexOf(":") + 1)
                        .contains(String.format("Post %d:", fileNum))) {
                    File f = new File(String.format("Post%d.txt", fileNum));
                    if (f.delete()) {
                        posts.remove(i);
                        PostWriter.writePosts(posts);
                        Main.setPosts(posts);
                        main.setPost(new ArrayList<>());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
