package com.cht.exchange.queue;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Workspace {
    private ArrayList<String> commandList = new ArrayList<String>();

    // 將要執行的 exchange command catch 起來
    public void catchCommand(String command) {
        commandList.add(command);
    }

    public void cleanCommands() {
        commandList.clear();
    }

    public ArrayList<String> getCommands() {
        return commandList;
    }
}
