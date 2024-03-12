package pgdp.searchengine.gui;

import pgdp.searchengine.gui.controller.AdminController;
import pgdp.searchengine.gui.controller.ResultController;
import pgdp.searchengine.gui.controller.SearchController;
import pgdp.searchengine.gui.controller.SearchEngineController;
import pgdp.searchengine.gui.model.AdminModel;
import pgdp.searchengine.gui.model.ResultModel;
import pgdp.searchengine.gui.view.AdminView;
import pgdp.searchengine.gui.view.ResultView;
import pgdp.searchengine.gui.view.SearchEngineView;
import pgdp.searchengine.gui.view.SearchView;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;

import javax.swing.*;

public class StartGUI {

    public static void main(String[] args) {
        startGUI();
    }

    public static void startGUI() {
        LinkedDocumentCollection documentCollection = new LinkedDocumentCollection(7);

        AdminModel adminModel = new AdminModel(documentCollection);
        ResultModel resultModel = new ResultModel(documentCollection);

        // Search Engine Objects
        SearchEngineView searchEngineView = new SearchEngineView();
        SearchEngineController searchEngineController = new SearchEngineController();

        searchEngineView.setSearchEngineController(searchEngineController);
        searchEngineController.setSearchEngineView(searchEngineView);

        // Admin Objects
        AdminView adminView = new AdminView();
        AdminController adminController = new AdminController();

        adminView.setAdminController(adminController);
        adminController.setAdminView(adminView);
        adminController.setAdminModel(adminModel);

        // Result Objects
        ResultView resultView = new ResultView();
        ResultController resultController = new ResultController();

        resultView.setResultController(resultController);
        resultController.setResultView(resultView);
        resultController.setResultModel(resultModel);

        // Search Objects
        SearchView searchView = new SearchView();
        SearchController searchController = new SearchController(searchEngineController);

        searchView.setSearchController(searchController);
        searchController.setSearchView(searchView);

        // Initialization
        searchEngineView.init(searchEngineController, adminView, resultView, searchView);
        searchEngineController.setAdminController(adminController);
        searchEngineController.setResultController(resultController);
        searchEngineController.setSearchController(searchController);

        searchEngineView.setSize(600, 600);
        searchEngineView.setVisible(true);
        searchEngineView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
