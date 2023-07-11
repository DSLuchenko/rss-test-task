package com.dsluchenko.app.core;

import com.dsluchenko.app.core.impl.RootNode;

public interface TreeNode<T> extends Tree {
    int getId();

    RootNode getParrent();

    String getName();

    T getValue();
}
