import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUIDisplay {

    public static JPanel displayPosts(ArrayList<String> posts, ArrayList<JButton> postsButton,
                                      ArrayList<JLabel> postsLabel, int i, ActionListener actionListener) {
        postsButton.add(new JButton(String.format("Post %d", Integer.parseInt(posts.get(i).substring(
                posts.get(i).indexOf(" ") + 1, posts.get(i).indexOf(":"))))));
        postsButton.get(i).addActionListener(actionListener);
        postsLabel.add(new JLabel(posts.get(i)));
        JPanel panel = new JPanel();
        panel.add(postsButton.get(i));
        panel.add(postsLabel.get(i));
        return panel;
    }
}
