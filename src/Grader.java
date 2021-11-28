import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class Grader {
    private Main main;

    public Grader(Main main) {
        this.main = main;
    }

    public boolean gradePost(ArrayList<String> post, String id, String inputGrade, int postFile) {
        try {
            int grade = Integer.parseInt(inputGrade);
            if (grade >= 0 && grade <= 100) { // check for invalid grade #s

                int index = -1;
                for (int i = 0; i < post.size(); i++) {
                    if (post.get(i).contains("Reply " + id)) {
                        index = i;
                        break;
                    }
                }

                if (!post.get(index).contains(", Grade:")) {
                    post.set(index, post.get(index) + ", Grade: " + grade); // appends grade
                    main.setPost(post);
                    PostWriter.writePost(post, "Post" + postFile + ".txt"); // updates posts
                    return true;
                }
            }
        } catch (Exception e) { // errors in input
            e.printStackTrace();
        }
        return false;
    }
}