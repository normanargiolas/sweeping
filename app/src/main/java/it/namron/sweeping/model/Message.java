package it.namron.sweeping.model;

/**
 * Created by norman on 15/05/17.
 */

public class Message {

    private boolean isSelected;
    private String folderName;

    public Message() {
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
