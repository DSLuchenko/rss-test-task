package com.dsluchenko.app.core.impl;

import com.dsluchenko.app.core.TreeNode;
import com.dsluchenko.app.util.FileLogger;

import java.util.logging.Logger;


public abstract class Node<T> implements TreeNode<T> {
    protected static final Logger logger = FileLogger.getFileLogger(Node.class.getName());

    private int id;
    private RootNode parrent;
    private String name;
    private T value;


    public Node(int id, RootNode parrent, String name, T value) {
        this.id = id;
        this.parrent = parrent;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public RootNode getParrent() {
        return parrent;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%d, %d, %s, ", this.id, this.parrent == null ? -1 : this.parrent.getId(), this.getName());
    }

}
