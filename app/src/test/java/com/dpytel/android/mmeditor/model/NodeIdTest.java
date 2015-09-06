package com.dpytel.android.mmeditor.model;

import android.support.annotation.NonNull;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NodeIdTest {

    @Test
    public void testToLong() throws Exception {
        assertToLongIs("ID_1511553563", 1511553563);
        assertToLongIs("ID_108504624", 108504624);
        assertToLongIs("ID_0", 0);
    }

    @Test
    public void parseCorrectNodeId() throws Exception {
        String id = "ID_1511553563";
        NodeId nodeId = nodeId(id);
        assertThat(nodeId.getId(), is(id));
    }

    @Test(expected = NodeId.MissingNodeIdException.class)
    public void nodeWithoutIdThrowsException() throws Exception {
        NodeId.nodeIdForElement(new NodeElement());
    }

    @Test
    public void testEquals() throws Exception {
        NodeId nodeId = new NodeId("ID_1");
        assertEquals(nodeId, nodeId);
        assertEquals(nodeId, new NodeId("ID_1"));
        assertNotEquals(nodeId, new NodeId("ID_33"));
    }

    private void assertToLongIs(String id, long expectedIdAsLong) {
        assertThat(nodeId(id).toLong(), is(expectedIdAsLong));
    }

    @NonNull
    private NodeId nodeId(String id) {
        Element element = new NodeElement(id);
        return NodeId.nodeIdForElement(element);
    }
}