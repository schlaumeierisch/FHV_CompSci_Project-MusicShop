package at.fhv.musicshopfx.domain;


import javafx.scene.control.CheckBox;

public class TopicLine {

    private final String topicName;
    private final CheckBox checkbox;

    public TopicLine(String topicName) {
        this.topicName = topicName;
        this.checkbox = new CheckBox();
        this.checkbox.setFocusTraversable(false);
    }

    public String getTopicName() {
        return topicName;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }
}
