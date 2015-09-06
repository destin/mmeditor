package com.dpytel.android.mmeditor.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by dawid on 03.09.15.
 */
public class Mindmap {

    private final Document document;

    public Mindmap(Document document) {
        this.document = document;
    }

    public MindmapNode getRootNode() {
        Element map = document.getDocumentElement();
        NodeList childNodes = map.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item instanceof Element) {
                return new XmlMindmapNode((Element) item);
            }
        }
        throw new IllegalArgumentException("Cannot find root element");
    }

}
