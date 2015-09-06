package com.dpytel.android.mmeditor;

import android.support.annotation.NonNull;

import com.dpytel.android.mmeditor.model.Mindmap;
import com.dpytel.android.mmeditor.model.MindmapFormatException;
import com.google.common.io.Closeables;

import org.w3c.dom.Document;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by dawid on 05.09.15.
 */
public class MindmapParser {

    private final DocumentBuilderFactory documentBuilderFactory;

    public MindmapParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    @NonNull
    public Mindmap parseMindmap(InputStream inputStream) {
        Document document;
        try {
            DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
            document = docBuilder.parse(inputStream);
        } catch (Exception e) {
            throw new MindmapFormatException("Cannot parse mindmap: " + e.getMessage(), e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
        return new Mindmap(document);
    }

}
