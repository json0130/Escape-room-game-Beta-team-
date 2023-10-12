package nz.ac.auckland.se206;

// ChatModel.java

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.se206.gpt.ChatMessage;

public class chatHistory {
    private List<ChatMessage> chatHistory = new ArrayList<>();

    public List<ChatMessage> getChatHistory() {
        return chatHistory;
    }

    public void addMessage(ChatMessage message) {
        chatHistory.add(message);
    }
}