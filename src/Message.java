import java.io.Serial;
import java.util.ArrayList;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -5399605122490343339L;

    private final String listName;
    private final ArrayList<String> contents;
    private final ArrayList<ArrayList<String>> sortContents;
    private final int num;
    private final String modifier;

    public Message(String listName, ArrayList<String> contents) {
        this.listName = listName;
        this.contents = contents;
        this.sortContents = new ArrayList<>();
        this.num = 0;
        this.modifier = null;
    }

    public Message(String listName, ArrayList<String> contents, String modifier) {
        this.listName = listName;
        this.contents = contents;
        this.sortContents = new ArrayList<>();
        this.num = 0;
        this.modifier = modifier;
    }

    public Message(String listName, int num) {
        this.listName = listName;
        this.contents = new ArrayList<>();
        this.sortContents = new ArrayList<>();
        this.num = num;
        this.modifier = null;
    }

    public Message(String listName, int num, String modifier) {
        this.listName = listName;
        this.contents = new ArrayList<>();
        this.sortContents = new ArrayList<>();
        this.num = num;
        this.modifier = modifier;
    }

    public Message(String listName, String modifier) {
        this.listName = listName;
        this.contents = new ArrayList<>();
        this.sortContents = new ArrayList<>();
        this.num = 0;
        this.modifier = modifier;
    }

    public Message(String listName, ArrayList<String> contents,
                   ArrayList<ArrayList<String>> sortContents, String modifier) {
        this.listName = listName;
        this.contents = contents;
        this.sortContents = sortContents;
        this.num = 0;
        this.modifier = modifier;
    }

    public String getListName() {
        return listName;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public int getNum() {
        return num;
    }

    public String getModifier() {
        return modifier;
    }

    public ArrayList<ArrayList<String>> getSortContents() {
        return sortContents;
    }
}
