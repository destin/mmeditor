package com.dpytel.android.mmeditor.model;

import android.support.annotation.Nullable;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mindmap node based on XML DOM element
 */
public class XmlMindmapNode implements MindmapNode {

    private static final String TEXT_ATTRIBUTE = "TEXT";
    private static final String NODE_TAG_NAME = "node";
    private final Element element;

    public XmlMindmapNode(Element element) {
        if (!isNode(element)) {
            throw new NotMindmapNodeException(element);
        }
        this.element = element;
    }

    @Override
    public String getText() {
        Node namedItem = element.getAttributes().getNamedItem(TEXT_ATTRIBUTE);
        if (namedItem == null) {
            return null;
        }
        return namedItem.getTextContent();
    }

    @Override
    public List<MindmapNode> getChildren() {
        NodeList childNodes = element.getChildNodes();
        List<MindmapNode> children = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item instanceof Element) {
                Element element = (Element) item;
                if (isNode(element)) {
                    children.add(new XmlMindmapNode(element));
                }
            }
        }
        return Collections.unmodifiableList(children);
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.nodeIdForElement(element);
    }

    @Nullable
    @Override
    public MindmapNode getParentNode() {
        Node parentNode = element.getParentNode();
        if (isRoot()) {
            throw new IllegalStateException("Trying to get parent of root node");
        }
        return new XmlMindmapNode((Element) parentNode);
    }

    @Override
    public boolean isRoot() {
        return !isNode(element.getParentNode());
    }

    private static boolean isNode(Node element) {
        return element != null
                && element instanceof Element
                && NODE_TAG_NAME.equals(((Element) element).getTagName());
    }

    public static class NotMindmapNodeException extends MindmapFormatException {

        public NotMindmapNodeException(Element element) {
            super("Element <" + element.getTagName() + "> is not a mindmap node");
        }
    }
}
