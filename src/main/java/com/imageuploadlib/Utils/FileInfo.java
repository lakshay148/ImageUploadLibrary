package com.imageuploadlib.Utils;

import java.io.Serializable;

/**
 * Created by Lakshay on 27-02-2015.
 */
public class FileInfo implements Serializable{

    public enum FILE_TYPE {IMAGE,FOLDER};

    String filePath;
    FILE_TYPE type;
    String fileName;
    String displayName;
    Boolean selected;
    Boolean fromServer;
    int fileCount;

    public Boolean getFromServer() {
        return fromServer;
    }

    public void setFromServer(Boolean fromServer) {
        this.fromServer = fromServer;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = super.equals(o);
        if (equals) {
            return true;
        }
        if (o instanceof FileInfo) {
            if (this.filePath.equals(((FileInfo) o).getFilePath())) {
                return true;
            }
        }
        return equals;
    }

    public FileInfo() {
        this.selected = false;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FILE_TYPE getType() {
        return type;
    }

    public void setType(FILE_TYPE type) {
        this.type = type;
    }
}
