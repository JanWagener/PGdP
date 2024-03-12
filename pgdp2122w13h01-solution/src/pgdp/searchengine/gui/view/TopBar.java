package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.SearchEngineController;

import javax.swing.*;
import java.awt.*;

public class TopBar extends JPanel {
    private JLabel title;

    private JButton crawlButton;
    private JButton toAdminViewButton;
    private JButton toSearchViewButton;
    private JButton exitButton;

    public TopBar(SearchEngineController controller) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        title = new JLabel();
        title.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
        add(title);

        add(Box.createHorizontalGlue());

        crawlButton = new JButton("Crawl");
        crawlButton.addActionListener(actionEvent -> controller.crawlButtonPressed());
        add(crawlButton);

        toAdminViewButton = new JButton("Admin View");
        toAdminViewButton.addActionListener(actionEvent -> controller.changeToAdminView());
        add(toAdminViewButton);

        toSearchViewButton = new JButton("Back to Search");
        toSearchViewButton.addActionListener(actionEvent -> controller.changeToSearchView());
        add(toSearchViewButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(actionEvent -> controller.exitButtonPressed());
        add(exitButton);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void hideCrawlButton() {
        crawlButton.setVisible(false);
    }

    public void hideToAdminViewButton() {
        toAdminViewButton.setVisible(false);
    }

    public void hideToSearchViewButton() {
        toSearchViewButton.setVisible(false);
    }

    public void setAllButtonsVisible() {
        crawlButton.setVisible(true);
        toAdminViewButton.setVisible(true);
        toSearchViewButton.setVisible(true);
        exitButton.setVisible(true);
    }
}
