package pgdp.searchengine.gui.controller;

import pgdp.searchengine.gui.view.SearchEngineView;
import pgdp.searchengine.gui.view.SearchView;

public class SearchController {
    private SearchEngineController mainController;
    private SearchView searchView;

    public SearchController(SearchEngineController mainController) {
        this.mainController = mainController;
    }

    public void executeSearch(String query) {
        mainController.processQuery(query);
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }
}
