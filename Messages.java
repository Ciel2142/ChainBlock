package blockchain;

import java.util.List;
import java.util.Scanner;

public class Messages implements Runnable {

    private static Messages MESSAGES;

    private final List<Message> MESSAGE_LIST;
    private Scanner sc;
    private boolean stop;

    Messages(List<Message> messageList) {
        this.MESSAGE_LIST = messageList;
        this.sc = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (!stop) {
            this.MESSAGE_LIST.add(new Message(sc.next(), sc.nextLine()));
        }
    }

    public void stop() {
        this.stop = true;
    }
}


class Message {
    private final String NAME;
    private final String CONTENT;

    Message(String name, String content) {
        this.NAME = name;
        this.CONTENT = content;
    }

    public String toString() {
        return String.format("%s: %s", this.NAME, this.CONTENT);
    }
}
