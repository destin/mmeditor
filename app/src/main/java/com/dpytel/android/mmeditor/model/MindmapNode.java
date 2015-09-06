package com.dpytel.android.mmeditor.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface MindmapNode {

    @Nullable
    String getText();

    @NonNull List<MindmapNode> getChildren();

    @NonNull NodeId getNodeId();

    /**
     * Get parent node of given mindmap node. Throws {@link IllegalStateException} when called on
     * root node. Therefore always first invoke {@link #isRoot()}
     *
     * @return parent node
     */
    @NonNull MindmapNode getParentNode();

    boolean isRoot();
}
