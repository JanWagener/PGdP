package pgdp.searchengine.gui.view;

import javax.swing.*;

public class ResultPane extends JPanel {
    private final JLabel linkLabel;
    private final JLabel titleLabel;
    private final JLabel contentLabel;

    public ResultPane(String link, String title, String content) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        linkLabel = new JLabel();
        titleLabel = new JLabel();
        contentLabel = new JLabel();

        setLink(link);
        setTitle(title);
        setContent(content);

        add(linkLabel);
        add(titleLabel);
        add(contentLabel);

        setBorder(BorderFactory.createEtchedBorder());
    }

    public void setLink(String link) {
        linkLabel.setText(link);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setContent(String content) {
        contentLabel.setText(content.substring(1, Math.min(content.length(), 50)));
    }
}
