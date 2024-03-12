package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.AdminController;

import javax.swing.*;
import java.awt.*;

public class DummyDocumentPane extends AbstractDocumentPane {

    public DummyDocumentPane(int id, String address, AdminController adminController) {
        super(id, address);
        setBackground(Color.RED);

        JButton crawlButton = new JButton("Crawl");
        crawlButton.addActionListener(actionEvent -> adminController.crawlButtonPressedForAddress(address));
        add(crawlButton);
    }

}
