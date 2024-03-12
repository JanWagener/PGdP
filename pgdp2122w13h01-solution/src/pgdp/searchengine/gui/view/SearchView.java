package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.SearchController;

import javax.swing.*;
import java.awt.*;

public class SearchView extends JPanel {
    private SearchController searchController;

    public SearchView() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 20, 5);

        // Text "PinguPinguLos"
        JLabel pplLabel = new JLabel("PinguPinguLos");
        pplLabel.setFont(new Font(Font.SERIF, Font.BOLD, 32));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(pplLabel, constraints);

        constraints.gridwidth = 1;

        // Suchfeld
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 50));
        searchField.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
        constraints.gridy = 1;
        add(searchField, constraints);

        // "Search"-Button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(actionEvent -> searchController.executeSearch(searchField.getText()));
        searchButton.setPreferredSize(new Dimension(80, 50));
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(searchButton, constraints);
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

}
