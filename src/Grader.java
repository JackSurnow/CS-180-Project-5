import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class Grader {
    private ForumClient forumClient;

    public Grader(ForumClient forumClient) {
        this.forumClient = forumClient;
    }

    public void gradeLogic(Message message) {
        try {
            ArrayList<String> sort = forumClient.getSort();
            int index = Integer.parseInt(JOptionPane.showInputDialog(null, "Select a reply index",
                    "Post Forum", JOptionPane.QUESTION_MESSAGE).replaceAll("[^0-9]",""));
            if (index > 0 && index <= sort.size()) {
                String replyMessage = sort.get(index - 1).substring(sort.get(index - 1).indexOf("R"));
                String replyNum = replyMessage.substring(replyMessage.indexOf("y") + 2, replyMessage.indexOf(":"));

                boolean done = false;
                for (int i = 0; i < message.getContents().size(); i++) {
                    int postFile = Integer.parseInt(message.getContents().get(i).substring(message.getContents()
                            .get(i).indexOf(" ") + 1, message.getContents().get(i).indexOf(":")));
                    for (int j = 0; j < message.getSortContents().get(i).size(); j++) {
                        if (message.getSortContents().get(i).get(j).contains(replyMessage)) {
                            forumClient.setPost(message.getSortContents().get(i), false);
                            String grade = JOptionPane.showInputDialog(null, "Enter " +
                                    "a grade.", "Post Forum", JOptionPane.QUESTION_MESSAGE);
                            if (grade != null && !grade.isEmpty()) {
                                if (gradePost(forumClient.getPost(), replyNum, grade, postFile)) {
                                    forumClient.GUI();
                                }
                            } else if (grade != null) {
                                JOptionPane.showMessageDialog(null, "Enter a grade.",
                                        "Post Forum", JOptionPane.ERROR_MESSAGE);
                            }
                            done = true;
                            break;
                        }
                    }

                    if (done) {
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enter a valid index.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                if (!post.get(index).contains(",") || !(post.get(index).contains(",") && post.get(index)
                        .substring(post.get(index).lastIndexOf(",")).contains(", Grade:"))) {
                    post.set(index, post.get(index) + ", Grade: " + grade); // appends grade
                    forumClient.setPost(post, true);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "That reply has already been graded.",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enter a grade from 0 to 100.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) { // errors in input
            e.printStackTrace();
        }
        return false;
    }
}
