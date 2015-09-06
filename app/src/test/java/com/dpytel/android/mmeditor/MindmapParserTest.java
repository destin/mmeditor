package com.dpytel.android.mmeditor;

import com.dpytel.android.mmeditor.model.Mindmap;
import com.dpytel.android.mmeditor.model.MindmapFormatException;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MindmapParserTest {

    private MindmapParser parser = new MindmapParser();

    @Test
    public void parseCorrectMMFile() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mmeditorhelp.mm");
        Mindmap mindmap = parser.parseMindmap(inputStream);

        assertThat(mindmap, notNullValue());
        assertThat(mindmap.getRootNode(), notNullValue());
    }

    @Test(expected = MindmapFormatException.class)
    public void incorrectFileThrowsMindmapFormatException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Not a XML".getBytes("UTF-8"));
        parser.parseMindmap(inputStream);
    }
}