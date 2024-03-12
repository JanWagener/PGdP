package pgdp.searchengine.gui.view;

import pgdp.searchengine.pagerepository.LinkedDocument;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DocumentPane extends AbstractDocumentPane {
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel linksToLabel;

    public DocumentPane(int id, String address, String title, String content, int[] linksTo) {
        super(id, address);

        titleLabel = new JLabel();
        contentLabel = new JLabel();
        linksToLabel = new JLabel();

        setTitle(title);
        setContent(content);
        setLinksTo(linksTo);

        add(titleLabel);
        add(contentLabel);
        add(linksToLabel);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setContent(String content) {
        contentLabel.setText(content.substring(0, Math.min(content.length(), 50)));
    }

    public void setLinksTo(int[] linksTo) {
        linksToLabel.setText("Links To: " + Arrays.stream(linksTo).mapToObj(n -> "" + n).collect(Collectors.joining(", ")));
    }
}
