package ru.pda.cloudservice.entitys;

public class UserItemList {
    private String fileName;
    private int fileSize;

    public UserItemList(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String filename) {
        this.fileName = filename;
    }

    public int getSize() {
        return fileSize;
    }

    public void setSize(int size) {
        this.fileSize = size;
    }
}
