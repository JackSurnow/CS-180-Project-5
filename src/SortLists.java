import java.io.*;
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

public class SortLists {
    private Main main;

    public SortLists(Main main) {
        this.main = main;
    }

    public ArrayList<String> sortReplies(ArrayList<String> posts, String studentName) {
        ArrayList<String> sort = new ArrayList<>();

        for (int i = 0; i < posts.size(); i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(String.format("Post%d.txt",
                    Integer.parseInt(posts.get(i).substring(posts.get(i).indexOf(" ") + 1, posts.get(i)
                            .indexOf(":"))))))) {

                String line = br.readLine();
                while (line != null) {
                    if (line.contains(":") && line.contains("Reply ") && line.substring(0, line.indexOf("R"))
                            .contains("(") && line.contains(" said \"") && line.substring(line.indexOf(":") + 2,
                            line.indexOf(" said \"")).matches(studentName)) {
                        sort.add(String.format("%d %s", sort.size() + 1, line.substring(line.indexOf("("))));
                    } else if (line.contains(":") && line.contains("Reply ") && line.contains(" said \"") && line
                            .substring(line.indexOf(":") + 2, line.indexOf(" said \"")).matches(studentName)) {
                        sort.add(String.format("%d %s", sort.size() + 1, line.substring(line.indexOf("R"))));
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        main.setSort(sort);
        return sort;
    }

    public ArrayList<String> sortGrades(ArrayList<String> posts) {
        ArrayList<String> sort = new ArrayList<>();

        for (int i = 0; i < posts.size(); i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(String.format("Post%d.txt",
                    Integer.parseInt(posts.get(i).substring(posts.get(i).indexOf(" ") + 1, posts.get(i)
                            .indexOf(":"))))))) {

                String line = br.readLine();
                while (line != null) {

                    if (line.contains(":") && line.contains(" said \"") && line.contains("Reply ") && line
                            .substring(0, line.indexOf("R")).contains("(") && line.contains(",") && line.substring(line
                            .lastIndexOf(",")).contains("Grade: ") && line.substring(line.indexOf(":") + 2, line
                            .indexOf(" said \"")).matches(main.getUsername())) {
                        sort.add(line.substring(line.indexOf("(")));
                    } else if (line.contains(":") && line.contains(" said \"") && line.contains("Reply ") && line
                            .contains(",") && line.substring(line.lastIndexOf(",")).contains("Grade: ") && line
                            .substring(line.indexOf(":") + 2, line.indexOf(" said \"")).matches(main.getUsername())) {
                        sort.add(line.substring(line.indexOf("R")));
                    }
                    line = br.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        main.setSort(sort);
        return sort;
    }

    public ArrayList<String> sortVotesMost(ArrayList<String> posts) {
        ArrayList<String> sort = new ArrayList<>();

        for (String item : posts) {
            sort.add(item);
            ArrayList<String> post = new ArrayList<>();
            ArrayList<String> transfer = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(String.format("Post%d.txt",
                    Integer.parseInt(item.substring(item.indexOf(" ") + 1, item
                            .indexOf(":"))))))) {

                String line = br.readLine();
                while (line != null) {
                    post.add(line);
                    line = br.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String value : post) {
                boolean entered = false;
                if (value.contains(":") && value.contains(" said \"") &&
                        value.contains("Reply ") && value.substring(0, value
                        .indexOf("R")).contains("(")) {
                    if (transfer.size() > 0) {
                        for (int k = 0; k < transfer.size(); k++) {
                            int numVotes = Integer.parseInt(value.substring(value
                                    .indexOf("(") + 1, value.indexOf(")")));
                            if (!entered && numVotes > Integer.parseInt(transfer.get(k)
                                    .substring(transfer.get(k).indexOf("(") + 1,
                                            transfer.get(k).indexOf(")")))) {
                                transfer.add(k, value.substring(value.indexOf("(")));
                                entered = true;
                            }
                        }

                        if (!entered) {
                            transfer.add(value.substring(value.indexOf("(")));
                        }

                    } else {
                        transfer.add(value.substring(value.indexOf("(")));
                    }
                }
            }

            for (String s : transfer) {
                sort.add(s);
            }
        }
        main.setSort(sort);
        return sort;
    }

    public ArrayList<String> sortVotesLeast(ArrayList<String> posts) {
        ArrayList<String> sort = new ArrayList<>();

        for (String value : posts) {
            sort.add(value);
            ArrayList<String> post = new ArrayList<>();
            ArrayList<String> transfer = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(String.format("Post%d.txt",
                    Integer.parseInt(value.substring(value.indexOf(" ") + 1, value
                            .indexOf(":"))))))) {

                String line = br.readLine();
                while (line != null) {
                    post.add(line);
                    line = br.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String s : post) {
                boolean entered = false;
                if (s.contains(":") && s.contains(" said \"") &&
                        s.contains("Reply ") && s.substring(0, s
                        .indexOf("R")).contains("(")) {
                    if (transfer.size() > 0) {
                        for (int k = 0; k < transfer.size(); k++) {
                            int numVotes = Integer.parseInt(s.substring(s
                                    .indexOf("(") + 1, s.indexOf(")")));
                            if (!entered && numVotes > Integer.parseInt(transfer.get(k)
                                    .substring(transfer.get(k).indexOf("(") + 1,
                                            transfer.get(k).indexOf(")")))) {
                                transfer.add(k, s.substring(s.indexOf("(")));
                                entered = true;
                            }
                        }

                        if (!entered) {
                            transfer.add(s.substring(s.indexOf("(")));
                        }

                    } else {
                        transfer.add(s.substring(s.indexOf("(")));
                    }
                }
            }

            for (int j = transfer.size() - 1; j >= 0; j--) {
                sort.add(transfer.get(j));
            }
        }
        main.setSort(sort);
        return sort;
    }
}
