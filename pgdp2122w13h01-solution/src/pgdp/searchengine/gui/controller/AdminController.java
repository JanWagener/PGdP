package pgdp.searchengine.gui.controller;

import pgdp.searchengine.gui.model.AdminModel;
import pgdp.searchengine.gui.view.AdminView;
import pgdp.searchengine.gui.view.DocumentPane;
import pgdp.searchengine.gui.view.DummyDocumentPane;
import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.Document;
import pgdp.searchengine.pagerepository.DummyLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocument;

import java.util.List;

public class AdminController {
    private AdminView adminView;
    private AdminModel adminModel;

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    public void setAdminModel(AdminModel adminModel) {
        this.adminModel = adminModel;
    }

    public void loadDocuments() {
        adminModel.loadListOfAllDocuments();
        loadNextBatch();
    }

    public void loadNextBatch() {
        loadIntoView(adminModel.getNextBatch());
    }

    public void loadIntoView(List<AbstractLinkedDocument> documents) {
        for(AbstractLinkedDocument document : documents) {
            if(document instanceof LinkedDocument linkedDocument) {
                DocumentPane documentPane = new DocumentPane(
                        linkedDocument.getDocumentId(),
                        linkedDocument.getAddress(),
                        linkedDocument.getTitle(),
                        linkedDocument.getContent(),
                        linkedDocument.getOutgoingLinks().toListSortedById().stream().mapToInt(Document::getDocumentId).toArray()
                );
                adminView.addDocumentPane(documentPane);
            } else if(document instanceof DummyLinkedDocument dummy) {
                DummyDocumentPane dummyPane = new DummyDocumentPane(
                        dummy.getDocumentId(),
                        dummy.getAddress(),
                        this
                );
                adminView.addDocumentPane(dummyPane);
            }
        }
    }

    public void crawlButtonPressedForAddress(String address) {
        adminModel.crawlPageWithAddress(address);
        adminView.clear();
        loadIntoView(adminModel.getDocumentsSoFar());
    }

    public void crawlFromAddress(int amount, String address) {
        adminModel.crawlPageWithAddress(amount, address);
        adminView.clear();
        loadDocuments();
    }
}
