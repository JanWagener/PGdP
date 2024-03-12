package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.ResultController;

import javax.swing.*;
import java.awt.*;

public class ResultView extends JScrollPane {
    private JPanel content;
    private JPanel documents;
    private JButton loadMore;
    private ResultController resultController;

    private GridBagConstraints constraints;
    private int numberOfDisplayedResults = 0;

    public ResultView() {
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
        loadMore.addActionListener(actionEvent -> resultController.loadNextBatch());
        content.add(loadMore, "South");
    }

    public void setResultController(ResultController resultController) {
        this.resultController = resultController;
    }

    public void addResultPane(ResultPane resultPane) {
        constraints.gridy = numberOfDisplayedResults++;
        documents.add(resultPane, constraints);
        updateUI();
    }

    public void clear() {
        numberOfDisplayedResults = 0;
        documents.removeAll();
    }

}
