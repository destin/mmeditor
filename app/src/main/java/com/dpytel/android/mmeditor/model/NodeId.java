package com.dpytel.android.mmeditor.model;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import org.w3c.dom.Element;

/**
 * Represents ID attribute of a node
 */
public class NodeId {

    private static final String ID_ATTR_NAME = "ID";
    private final String id;

    @NonNull
    public static NodeId nodeIdForElement(@NonNull Element element) {
        if (!element.hasAttribute(ID_ATTR_NAME)) {
            throw new MissingNodeIdException();
        }
        String id = element.getAttribute(ID_ATTR_NAME);
        return new NodeId(id);
    }

    public NodeId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public long toLong() {
        return Long.parseLong(id.substring(3));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId = (NodeId) o;
        return Objects.equal(id, nodeId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id;
    }

    public static class MissingNodeIdException extends MindmapFormatException {
        public MissingNodeIdException() {
            super("One of nodes has no id");
        }
    }
}
