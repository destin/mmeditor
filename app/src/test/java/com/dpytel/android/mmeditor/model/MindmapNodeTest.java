package com.dpytel.android.mmeditor.model;

import android.support.annotation.NonNull;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by dawid on 03.09.15.
 */
public class MindmapNodeTest {

    public static final String NODE_WITH_EMPTY_TEXT = "<node ID=\"ID_179540908\" TEXT=\"\"/>";

    public static final String NODE_WITHOUT_CHILDREN = "<node ID=\"ID_179540908\" TEXT=\"\"/>";

    public static final String NODE_WITH_CHILD = "<node ID=\"ID_1\" TEXT=\"Parent\">" +
            "<node ID=\"ID_11\" TEXT=\"Child\"/>" +
            "</node>";
    private static final String NODE_WITH_CHILD_AND_FONT = "<node ID=\"ID_1\" TEXT=\"Parent\">" +
            "<font BOLD=\"true\" NAME=\"SansSerif\" SIZE=\"19\"/>" +
            "<node ID=\"ID_11\" TEXT=\"Child\"/>" +
            "</node>";;

    @Test
    public void getText() throws Exception {
        Element rootNode = parseRootNodeFromFile("mmeditorhelp.mm");
        MindmapNode helpMindmap = new XmlMindmapNode(rootNode);
        assertThat(helpMindmap.getText(), is("MMeditor help"));
    }

    @Test
    public void getTextReturnsNullIfNoText() throws Exception {
        MindmapNode mindmapNode = mindmapNodeFromString(NODE_WITH_EMPTY_TEXT);
        assertThat(mindmapNode.getText(), is(""));
    }

    @Test
    public void getNoChildren() throws Exception {
        MindmapNode mindmapNode = mindmapNodeFromString(NODE_WITHOUT_CHILDREN);
        List<MindmapNode> children = mindmapNode.getChildren();
        assertThat(children, is(Matchers.empty()));
    }

    @Test
    public void getOneChild() throws Exception {
        MindmapNode mindmapNode = mindmapNodeFromString(NODE_WITH_CHILD);
        List<MindmapNode> children = mindmapNode.getChildren();
        assertThat(children, hasSize(1));
        assertThat(children.get(0).getText(), is("Child"));
    }

    @Test
    public void getChildrenReturnsOnlyNodes() throws Exception {
        MindmapNode mindmapNode = mindmapNodeFromString(NODE_WITHOUT_CHILDREN);
        assertThat(mindmapNode.getNodeId().getId(), is("ID_179540908"));
    }

    @Test
    public void getNodeId() throws Exception {
        Element rootNode = parseRootNodeFromFile("mmeditorhelp.mm");
        MindmapNode helpMindmap = new XmlMindmapNode(rootNode);
        assertThat(helpMindmap.getText(), is("MMeditor help"));
    }

    @Test
    public void getParentNode() throws Exception {
        MindmapNode node = mindmapNodeFromString(NODE_WITH_CHILD);
        MindmapNode child = firstChild(node);
        MindmapNode parentNode = child.getParentNode();
        assertThat(parentNode.getNodeId(), is(node.getNodeId()));
    }

    @Test(expected = IllegalStateException.class)
    public void rootNodeHasNoParent() throws Exception {
        MindmapNode node = mindmapNodeFromString(NODE_WITH_CHILD);
        assertTrue(node.isRoot());
        node.getParentNode();
    }

    @Test
    public void childNodeIsNotRoot() throws Exception {
        MindmapNode node = mindmapNodeFromString(NODE_WITH_CHILD);
        assertFalse(firstChild(node).isRoot());
    }

    @NonNull
    private MindmapNode mindmapNodeFromString(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document document = docBuilder.parse(inputStream);
        Element documentElement = document.getDocumentElement();
        return new XmlMindmapNode(documentElement);
    }

    private Element parseRootNodeFromFile(String resName) throws ParserConfigurationException, SAXException, IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resName);
        assertNotNull(inputStream);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(inputStream);
        Element map = document.getDocumentElement();
        NodeList childNodes = map.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item instanceof Element) {
                return (Element) item;
            }
        }
        throw new IllegalArgumentException("Cannot find root element");
    }

    private MindmapNode firstChild(MindmapNode node) {
        return node.getChildren().get(0);
    }
}