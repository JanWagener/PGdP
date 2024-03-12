package pgdp.searchengine.gui.model;

import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;

import java.util.LinkedList;
import java.util.List;

public class ResultModel {
    private final LinkedDocumentCollection documentCollection;

    private List<LinkedDocument> resultsSortedByRelevance;
    private int numberOfLoadedDocuments;

    public ResultModel(LinkedDocumentCollection documentCollection) {
        this.documentCollection = documentCollection;
        clear();
    }

    public List<LinkedDocument> getNextBatch() {
        if(resultsSortedByRelevance == null) {
            return new LinkedList<>();
        }

        int number = Math.min(numberOfLoadedDocuments + 10, resultsSortedByRelevance.size());

        var output = resultsSortedByRelevance.subList(numberOfLoadedDocuments, number);
        numberOfLoadedDocuments = number;
        return output;
    }

    public void computeResult(String query) {
        clear();
        for(AbstractLinkedDocument document : documentCollection.query(query, 0.85, 0.6, 50)) {
            if(document instanceof LinkedDocument linkedDocument) {
                resultsSortedByRelevance.add(linkedDocument);
            }
        }
    }

    public void clear() {
        resultsSortedByRelevance = new LinkedList<>();
        numberOfLoadedDocuments = 0;
    }
}
