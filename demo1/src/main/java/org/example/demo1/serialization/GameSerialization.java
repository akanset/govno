package org.example.demo1.serialization;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSerialization implements Serializable {
    private ArrayList<Serializable> children;

    public GameSerialization(ArrayList<Serializable> children) {
        this.children = children;
    }

    public ArrayList<Serializable> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Serializable> children) {
        this.children = children;
    }
}
