package com.dsluchenko.app.core.impl;

public class LeafNode extends Node<String> {


    private LeafNode(int id, RootNode parrent, String name, String value) {
        super(id, parrent, name, value);

    }

    public static LeafNode createLeaf(int id, RootNode parrent, String name, String value) {
        LeafNode leaf = new LeafNode(id, parrent, name, value);

        logger.finest("Создан узел дерева - лист :" + leaf);

        return leaf;
    }

    ;

    @Override
    public String toString() {
        return super.toString()
                + "\""
                + this.getValue()
                + "\""
                + "\n";
    }
}
