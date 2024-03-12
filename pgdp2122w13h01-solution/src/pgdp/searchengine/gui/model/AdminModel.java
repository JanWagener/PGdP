package pgdp.searchengine.gui.model;

import pgdp.searchengine.networking.PageCrawling;
import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;

import java.util.LinkedList;
import java.util.List;

public class AdminModel {
    private final LinkedDocumentCollection documentCollection;

    private List<AbstractLinkedDocument> documentsSortedById;
    private int numberOfLoadedDocuments;

    public AdminModel(LinkedDocumentCollection documentCollection) {
        this.documentCollection = documentCollection;
    }

    public List<AbstractLinkedDocument> getNextBatch() {
        if(documentsSortedById == null) {
            return new LinkedList<>();
        }

        int newNumber = Math.min(numberOfLoadedDocuments + 10, documentsSortedById.size());

        var output = documentsSortedById.subList(numberOfLoadedDocuments, newNumber);
        numberOfLoadedDocuments = newNumber;
        return output;
    }

    public List<AbstractLinkedDocument> getDocumentsSoFar() {
        return documentsSortedById.subList(0, Math.min(numberOfLoadedDocuments, documentsSortedById.size()));
    }

    public void loadListOfAllDocuments() {
        documentsSortedById = documentCollection.toListSortedById();
    }

    public void crawlPageWithAddress(String address) {
        PageCrawling.crawlPage(documentCollection, address);
        loadListOfAllDocuments();
    }

    public void crawlPageWithAddress(int amount, String address) {
        PageCrawling.crawlPages(documentCollection, amount, address);
        loadListOfAllDocuments();
    }
}
