package codeu.chat.util.mysql;

/**
 * Created by mltaskova on 5/26/17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MySQLConnection dao = new MySQLConnection();

        dao.writeConversations("title", "smth", "pass");

        String[] arr = dao.readConversations();
        for (int i = 0; i<arr.length;i++)
        {
            System.out.println(arr[i]);
        }
    }
}
