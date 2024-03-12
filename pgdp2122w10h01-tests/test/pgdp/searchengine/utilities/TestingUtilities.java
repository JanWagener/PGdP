package pgdp.searchengine.utilities;

import pgdp.searchengine.pagerepository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestingUtilities {

    public static String stripToBareMinimum(String s) {
        return s.replaceAll("\\n|\\t|\\r|\\r\\n", " ").replaceAll(" +", " ").trim().toLowerCase();
    }

    // ========================================================================================= //
    // -------------------------- LinkedDocumentCollection-Processing -------------------------- //
    // ========================================================================================= //

    public static String linkedDocumentCollectionAsString(LinkedDocumentCollection collection) {
        StringBuilder collectionString = new StringBuilder();
        for(int i = 0; i < collection.getNumberOfBuckets(); i++) {
            Bucket bucket = collection.getBuckets()[i];
            if(bucket.size() == 0) {
                collectionString.append("Bucket ").append(i).append(": Empty\n");
                continue;
            }
            collectionString.append("Bucket ").append(i).append(":\n");
            DocumentListElement currentElement = bucket.getHead();
            while(currentElement != null) {
                if(currentElement.getDocument() instanceof DummyLinkedDocument dummy) {
                    collectionString
                            .append("\t")
                            .append(dummy.getDocumentId())
                            .append(": Dummy (")
                            .append(dummy.getAddress())
                            .append(")\n");
                }
                if(currentElement.getDocument() instanceof LinkedDocument linked) {
                    collectionString
                            .append("\t")
                            .append(linked.getDocumentId())
                            .append(": Real (")
                            .append(linked.getAddress())
                            .append("), Links to: ")
                            .append(linkedDocumentCollectionIDsAsString(linked.getOutgoingLinks()))
                            .append("\n");
                }
                currentElement = currentElement.getNext();
            }
        }

        return collectionString.toString();
    }

    public static String linkedDocumentCollectionIDsAsString(LinkedDocumentCollection collection) {
        StringBuilder sb = new StringBuilder();
        for(Document document : collection) {
            sb.append(document.getDocumentId()).append(" ");
        }
        return sb.toString();
    }

    public static HashMap<String, Boolean> linkedDocumentCollectionToNodeMap(LinkedDocumentCollection collection) {
        HashMap<String, Boolean> nodes = new HashMap<>();
        for(Document document : collection) {
            nodes.put(((AbstractLinkedDocument) document).getAddress(), document instanceof LinkedDocument);
        }
        return nodes;
    }

    public static LinkedDocumentCollection copyLinkedDocumentCollection(LinkedDocumentCollection collection) {
        LinkedDocumentCollection copy = new LinkedDocumentCollection(collection.getNumberOfBuckets());
        for(Document document : collection) {
            copy.add(document);
        }
        return copy;
    }

    public static int countOccurrencesOfAddress(LinkedDocumentCollection collection, String address) {
        int count = 0;
        for(Document document : collection) {
            if(document instanceof AbstractLinkedDocument linked && linked.getAddress().equals(address)) {
                count++;
            }
        }
        return count;
    }

    public static Set<Tuple<String, String>> linkedDocumentCollectionToLinkSet(LinkedDocumentCollection collection) {
        Set<Tuple<String, String>> links = new HashSet<>();
        for(Document document : collection) {
            if(document instanceof LinkedDocument linked) {
                for(Document endOfLink : linked.getOutgoingLinks()) {
                    links.add(Tuple.of(
                            linked.getAddress(),
                            ((AbstractLinkedDocument) endOfLink).getAddress()
                    ));
                }
            }
        }
        return links;
    }

    public static void assertAddingWorkedCorrectly(LinkedDocumentCollection originalCollection, LinkedDocumentCollection resultCollection, String documentToAdd, String[] linksToAdd, String methodName) {
        // Value == true iff node instanceof LinkedDocument
        HashMap<String, Boolean> oldNodes = linkedDocumentCollectionToNodeMap(originalCollection);
        HashMap<String, Boolean> newNodes = linkedDocumentCollectionToNodeMap(resultCollection);

        String standardErrorText = "Deine Implementierung der Methode " + methodName + " funktioniert nicht für jeden Input. Der Aufruf der Methode mit einem Dokument mit Adresse \"" +
                documentToAdd + "\" und Links zu den Adressen " + Arrays.toString(linksToAdd) + " zu der Collection " + linkedDocumentCollectionAsString(originalCollection) +
                " führt zu folgendem Fehler:\n";

        Set<Tuple<String, String>> oldLinks = linkedDocumentCollectionToLinkSet(originalCollection);
        Set<Tuple<String, String>> newLinks = linkedDocumentCollectionToLinkSet(resultCollection);

        assertAll(
                // ------------ CHECK NODES ------------ //
                () -> oldNodesSubsetNewNodes(oldNodes, newNodes, documentToAdd, standardErrorText),
                () -> documentToAddInNewNodes(newNodes, documentToAdd, standardErrorText),
                () -> linkedAddressesSubsetNewNodes(oldNodes, newNodes, documentToAdd, linksToAdd, standardErrorText),
                () -> newNodesSubsetOldNodesUnionDocumentToAddUnionLinkedAddresses(oldNodes, newNodes, documentToAdd, linksToAdd, standardErrorText),
                () -> noDuplicateNodesInNewNodes(newNodes, resultCollection, standardErrorText),

                // ------------ CHECK LINKS ------------ //
                () -> oldLinksSubsetNewLinks(oldLinks, newLinks, standardErrorText),
                () -> linksFromDocumentToAddToLinkedAddressesSubsetNewLinks(documentToAdd, linksToAdd, newLinks, standardErrorText),
                () -> newLinksSubsetOldLinksUnionLinksFromDocumentToAddToLinkedAddresses(documentToAdd, linksToAdd, oldLinks, newLinks, standardErrorText)
        );
    }

    // =================================================================================================================== //
    // --------------------------------- Helper Methods for assertAddingWorksCorrectly() --------------------------------- //
    // =================================================================================================================== //

    // Every node in old collection is also in new and has same "crawledness" (except node of added address)
    private static void oldNodesSubsetNewNodes(HashMap<String, Boolean> oldNodes, HashMap<String, Boolean> newNodes, String documentToAdd, String standardErrorText) {
        for(String address : oldNodes.keySet()) {
            if(address.equals(documentToAdd)) {
                continue;
            }
            if(newNodes.get(address) == null) {
                fail(standardErrorText + "Das bereits vorhandene Dokument mit Adresse \"" + address + "\" wird durch die Operation gelöscht.");
            }
            if(oldNodes.get(address) && !newNodes.get(address)) {
                fail(standardErrorText + "Das bereits vorhandene Dokument mit Adresse \"" + address + "\" wird von einem 'echten' Dokument zu einem Dummy degradiert.");
            }
            if(!oldNodes.get(address) && newNodes.get(address)) {
                fail(standardErrorText + "Das bereits vorhandene Dummy-Dokument mit Adresse \"" + address + "\" wird zu einem 'LinkedDocument', obwohl es ein Dummy hätte bleiben sollen.");
            }
        }
    }

    // Added address now present as 'LinkedDocument'
    private static void documentToAddInNewNodes(HashMap<String, Boolean> newNodes, String documentToAdd, String standardErrorText) {
        if(newNodes.get(documentToAdd) == null) {
            fail(standardErrorText + "Das in die Collection einzufügende Dokument \"" + documentToAdd + "\" ist nach Ausführung der Methode nicht vorhanden.");
        }
        else if(!newNodes.get(documentToAdd)) {
            fail(standardErrorText + "Das in die Collection einzufügende Dokument \"" + documentToAdd + "\" ist nach Ausführung der Methode nur als Dummy-Dokument vorhanden.");
        }
    }

    // Every "linkToAdd", that was not present in original collection is now present as dummy
    private static void linkedAddressesSubsetNewNodes(HashMap<String, Boolean> oldNodes, HashMap<String, Boolean> newNodes, String documentToAdd, String[] linksToAdd, String standardErrorText) {
        for(String linkToAdd : linksToAdd) {
            if(!oldNodes.containsKey(linkToAdd) && !linkToAdd.equals(documentToAdd)) {
                if(newNodes.get(linkToAdd) == null) {
                    fail(standardErrorText + "Für den vom in die Collection einzufügenden Dokument ausgehenden Link \"" + linkToAdd +
                            "\" ist nach Ausführung der Methode kein Dokument in der Collection vorhanden.");
                }
                if(newNodes.get(linkToAdd)) {
                    fail(standardErrorText + "Für dem vom in die Collection einzufügenden Dokument ausgehenden Link \"" + linkToAdd +
                            "\", der zuvor noch nicht in ihr vorhanden war, ist jetzt bereits ein 'LinkedDocument' vorhanden, obwohl dafür nur ein Dummy vorhanden sein sollte.");
                }
            }
        }
    }

    // Everything, that is present after the operation was already present before it, is the added document or is an added link
    private static void newNodesSubsetOldNodesUnionDocumentToAddUnionLinkedAddresses(HashMap<String, Boolean> oldNodes, HashMap<String, Boolean> newNodes, String documentToAdd, String[] linksToAdd, String standardErrorText) {
        Set<String> linksToAddAsCollection = Set.of(linksToAdd);
        for(String address : newNodes.keySet()) {
            if(oldNodes.get(address) == null && !address.equals(documentToAdd) && !linksToAddAsCollection.contains(address)) {
                fail(standardErrorText + "Für die Adresse \"" + address + "\" gab es vor Aufruf der Methode weder ein Dokument in der Collection (weder LinkedDocument noch Dummy), " +
                        "noch ist es die hinzugefügte Adresse, noch ist es einer der hinzugefügten Links. Dennoch ist nach Aufruf der Methode ein Dokument mit dieser Adresse in der " +
                        "Collection vorhanden. Das sollte nicht der Fall sein.");
            }
        }
    }

    // No duplicates in output collection
    private static void noDuplicateNodesInNewNodes(HashMap<String, Boolean> newNodes, LinkedDocumentCollection resultCollection, String standardErrorText) {
        for(String address : newNodes.keySet()) {
            if(countOccurrencesOfAddress(resultCollection, address) > 1) {
                fail(standardErrorText + "Für die Adresse \"" + address + "\" gibt es nach Aufruf der Methode mehrere Dokumente in der Collection.");
            }
        }
    }

    // Old links still present (between respective addresses, that is)
    private static void oldLinksSubsetNewLinks(Set<Tuple<String, String>> oldLinks, Set<Tuple<String, String>> newLinks, String standardErrorText) {
        for(Tuple<String, String> link : oldLinks) {
            if(!newLinks.contains(link)) {
                fail(standardErrorText + "Der Link " + link + " ist vor Aufrufen der Methode vorhanden, wird aber dann gelöscht. Es sollten keine Links gelöscht werden.");
            }
        }
    }

    // New links from added document to any in 'linksToAdd'
    private static void linksFromDocumentToAddToLinkedAddressesSubsetNewLinks(String documentToAdd, String[] linksToAdd, Set<Tuple<String, String>> newLinks, String standardErrorText) {
        for(String linkToAdd : linksToAdd) {
            if(!newLinks.contains(Tuple.of(documentToAdd, linkToAdd))) {
                fail(standardErrorText + "Es sollte vom dem einzufügenden Dokument zu jeder von ihm aus verlinkten Adresse auch ein Link gehen. " +
                        "Zu der Adresse \"" + linkToAdd + "\" wurde aber beim Aufrufen der Methode keiner eingefügt.");
            }
        }
    }

    // No more links than
    private static void newLinksSubsetOldLinksUnionLinksFromDocumentToAddToLinkedAddresses(String documentToAdd, String[] linksToAdd, Set<Tuple<String, String>> oldLinks, Set<Tuple<String, String>> newLinks, String standardErrorText) {
        Set<String> linksToAddAsCollection = Set.of(linksToAdd);
        for(Tuple<String, String> link : newLinks) {
            if(!oldLinks.contains(link) && !(link.getFirst().equals(documentToAdd) && linksToAddAsCollection.contains(link.getSecond()))) {
                fail(standardErrorText + "Der Link " + link + " war weder in der alten Collection vorhanden, noch führt er von dem einzufügenden Dokument zu einem der von diesem " +
                        "aus verlinkten Adressen. Dennoch ist der nach dem Aufruf der Methode in der Collection vorhanden. Das sollte er nicht sein.");
            }
        }
    }

}
