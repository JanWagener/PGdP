package pgdp.searchengine.gui.controller;

import pgdp.searchengine.gui.view.CrawlDialog;
import pgdp.searchengine.gui.view.SearchEngineView;

import java.awt.event.WindowEvent;

public class SearchEngineController {
    private SearchController searchController;
    private ResultController resultController;
    private AdminController adminController;

    private SearchEngineView searchEngineView;

    public void setSearchEngineView(SearchEngineView searchEngineView) {
        this.searchEngineView = searchEngineView;
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    public void setResultController(ResultController resultController) {
        this.resultController = resultController;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void processQuery(String query) {
        resultController.loadResultsFor(query);
        changeToResultView();
    }

    public void changeToAdminView() {
        searchEngineView.displayAdminView();
    }

    public void changeToResultView() {
        searchEngineView.displayResultView();
    }

    public void changeToSearchView() {
        searchEngineView.displaySearchView();
    }

    public void crawlButtonPressed() {
        CrawlDialog crawlDialog = new CrawlDialog(adminController);
        crawlDialog.setSize(400, 250);
        crawlDialog.setVisible(true);
    }

    public void exitButtonPressed() {
        searchEngineView.dispatchEvent(new WindowEvent(searchEngineView, WindowEvent.WINDOW_CLOSING));
    }
}
