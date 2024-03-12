package pgdp.searchengine.gui.controller;

import pgdp.searchengine.gui.model.ResultModel;
import pgdp.searchengine.gui.view.ResultPane;
import pgdp.searchengine.gui.view.ResultView;
import pgdp.searchengine.pagerepository.LinkedDocument;

import java.util.List;

public class ResultController {
    private ResultView resultView;
    private ResultModel resultModel;

    public void setResultModel(ResultModel resultModel) {
        this.resultModel = resultModel;
    }

    public void setResultView(ResultView resultView) {
        this.resultView = resultView;
    }

    public void loadResultsFor(String query) {
        resultView.clear();
        resultModel.computeResult(query);
        loadNextBatch();
    }

    public void loadNextBatch() {
        List<LinkedDocument> nextResults = resultModel.getNextBatch();

        for(LinkedDocument document : nextResults) {
            ResultPane resultPane = new ResultPane(document.getAddress(), document.getTitle(), document.getContent());
            resultView.addResultPane(resultPane);
        }
    }
}
