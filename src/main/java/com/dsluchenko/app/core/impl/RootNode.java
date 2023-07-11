package com.dsluchenko.app.core.impl;

import com.dsluchenko.app.util.FileLogger;

import java.util.ArrayList;
import java.util.List;

public class RootNode extends Node<List<Node>> {

    private RootNode(int id, RootNode parrent, String name, List<Node> value) {
        super(id, parrent, name, value);
    }

    public static RootNode createRoot(int id, String name) {
        RootNode root = new RootNode(id, null, name, new ArrayList<>());

        logger.finest("Создан корень дерева :" + root);

        return root;
    }

    public static RootNode createParrent(int id, RootNode parrent, String name) {
        RootNode parrentNode = new RootNode(id, parrent, name, new ArrayList<>());

        logger.finest("Создан узел дерева - родитель :" + parrentNode);

        return parrentNode;
    }


    @Override
    public String toString() {
        return super.toString() + getChildIds() + "\n";
    }

    private String getChildIds() {
        if (this.getValue().size() == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        for (Node node : (this.getValue())) {
            sb.append(node.getId() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");


        return sb.toString();

    }
}
