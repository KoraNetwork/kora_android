package com.kora.android.data.web3j.storage;

public class FileMetaData {

    private String mDisplayName;
    private long mSize;
    private String mMimeType;
    private String mPath;

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mimeType) {
        this.mMimeType = mimeType;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    @Override
    public String toString() {
        return "FileMetaData{" + "\n" +
                "mDisplayName=" + mDisplayName + "\n" +
                "mSize=" + mSize + "\n" +
                "mMimeType=" + mMimeType + "\n" +
                "mPath=" + mPath + "\n" +
                "}";
    }
}