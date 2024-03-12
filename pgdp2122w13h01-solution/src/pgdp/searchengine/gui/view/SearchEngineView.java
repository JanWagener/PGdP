package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.SearchController;
import pgdp.searchengine.gui.controller.SearchEngineController;

import javax.swing.*;
import java.awt.*;

public class SearchEngineView extends JFrame {
    private SearchEngineController searchEngineController;

    private TopBar topBar;
    private JPanel body;

    private AdminView adminView;
    private ResultView resultView;
    private SearchView searchView;

    private String ADMIN_VIEW = "Card with Admin View";
    private String RESULT_VIEW = "Card with Result View";
    private String SEARCH_VIEW = "Card with Search View";

    public SearchEngineView() {
        super("PinguPinguLos");
    }

    public void setSearchEngineController(SearchEngineController searchEngineController) {
        this.searchEngineController = searchEngineController;
    }

    public void init(SearchEngineController searchEngineController, AdminView adminView, ResultView resultView, SearchView searchView) {
        this.searchEngineController = searchEngineController;
        this.adminView = adminView;
        this.resultView = resultView;
        this.searchView = searchView;

        topBar = new TopBar(searchEngineController);
        add(topBar, "North");

        body = new JPanel();
        body.setLayout(new CardLayout());
        add(body, "Center");

        body.add(searchView, SEARCH_VIEW);
        body.add(resultView, RESULT_VIEW);
        body.add(adminView, ADMIN_VIEW);

        displaySearchView();
    }

    public void displayAdminView() {
        displayView(ADMIN_VIEW);
        topBar.hideToAdminViewButton();
        topBar.setTitle("Admin View");
    }

    public void displayResultView() {
        displayView(RESULT_VIEW);
        topBar.hideCrawlButton();
        topBar.setTitle("Search Results");
    }

    public void displaySearchView() {
        displayView(SEARCH_VIEW);
        topBar.hideCrawlButton();
        topBar.hideToSearchViewButton();
        topBar.setTitle("Search");
    }

    private void displayView(String view) {
        CardLayout cardLayout = (CardLayout) body.getLayout();
        cardLayout.show(body, view);
        topBar.setAllButtonsVisible();
    }
}
