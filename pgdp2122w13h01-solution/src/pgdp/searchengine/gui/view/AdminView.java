package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.AdminController;

import javax.swing.*;
import java.awt.*;

public class AdminView extends JScrollPane {
    private JPanel content;
    private JPanel documents;
    private JButton loadMore;
    private AdminController adminController;

    private GridBagConstraints constraints;
    private int numberOfDisplayedDocuments = 0;

    public AdminView() {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        content = new JPanel();
        content.setLayout(new BorderLayout());
        setViewportView(content);

        documents = new JPanel();
        documents.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        content.add(documents, "Center");

        loadMore = new JButton("Load More");
        loadMore.addActionListener(actionEvent -> adminController.loadNextBatch());
        content.add(loadMore, "South");
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    public void addDocumentPane(AbstractDocumentPane documentPane) {
        constraints.gridy = numberOfDisplayedDocuments++;
        documents.add(documentPane, constraints);
        updateUI();
    }

    public void clear() {
        numberOfDisplayedDocuments = 0;
        documents.removeAll();
    }
}
