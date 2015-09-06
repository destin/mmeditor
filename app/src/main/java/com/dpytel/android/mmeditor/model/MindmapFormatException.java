package com.dpytel.android.mmeditor.model;

/**
 * Created by dawid on 04.09.15.
 */
public class MindmapFormatException extends RuntimeException {

    public MindmapFormatException(String detailMessage) {
        super(detailMessage);
    }

    public MindmapFormatException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
