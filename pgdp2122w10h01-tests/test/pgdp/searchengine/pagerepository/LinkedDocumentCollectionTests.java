package pgdp.searchengine.pagerepository;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.assertj.core.internal.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.jupiter.api.DisplayName;
import pgdp.searchengine.W10H01;
import pgdp.searchengine.utilities.TestingUtilities;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.searchengine.utilities.TestingUtilities.assertAddingWorkedCorrectly;
import static pgdp.searchengine.utilities.Reflections.*;

@W10H01
public class LinkedDocumentCollectionTests {

    @DisplayName("- | Test whether new methods exist in LinkedDocumentCollection")
    @PublicTest
    void linkedDocumentCollectionStructuralTests() {
        assertAll(
                () -> assertTrue(addToResultCollection.exists(), "Die Methode 'addToResultCollection()' in 'LinkedDocumentCollection' existiert nicht bzw. hat nicht das benötigte Update erhalten."),
                () -> assertTrue(getNextUncrawledAddress.exists(), "Die in 'LinkedDocumentCollection' geforderte Methode 'String getNextUncrawledAddress()' existiert nicht.")
        );
    }

    // ================================================================================================ //
    // ---------------- Methods, that test one method in LinkedDocumentCollection each ---------------- //
    // ================================================================================================ //

    void testAddToResultCollection(LinkedDocumentCollection originalCollection, AbstractLinkedDocument documentToAdd, String[] linksToAdd) {
        LinkedDocumentCollection resultCollection = TestingUtilities.copyLinkedDocumentCollection(originalCollection);
        addToResultCollection.invokeOn(resultCollection, documentToAdd, linksToAdd);

        assertAddingWorkedCorrectly(originalCollection, resultCollection, documentToAdd.getAddress(), linksToAdd, "'addToResultCollection()'");
    }

    void testGetNextUncrawledAddress(LinkedDocumentCollection collection, Set<String> expectedAddresses) {
        String actualAddress = (String) getNextUncrawledAddress.invokeOn(collection);

        if(expectedAddresses.size() == 0) {
            assertNull(actualAddress,
                    "In der Collection\n" + TestingUtilities.linkedDocumentCollectionAsString(collection) +
                    "gibt es keine nicht heruntergeladenen Adressen. Daher sollte deine Implementierung der Methode " +
                            "'getNextUncrawledAddress()' hier auch 'null' zurückgeben. Stattdessen wurde aber \"" +
                    actualAddress + "\" zurückgegeben.");
        } else {
            assertTrue(expectedAddresses.contains(actualAddress),
                    "Die Collection\n" + TestingUtilities.linkedDocumentCollectionAsString(collection) + "enthält folgende noch nicht heruntergeladene Adressen:\n" +
                    expectedAddresses.toString() + "\nDeine Implementierung gibt aber " +
                            (actualAddress == null ? "'null'" : "\"" + actualAddress + "\"") + " zurück.");
        }
    }

    // =============================================================================================== //
    // ----------------------- Public Tests for LinkedDocumentCollection Class ----------------------- //
    // =============================================================================================== //

    @DisplayName("- | Public Tests for Method 'addToResultCollection()'")
    @PublicTest
    void testAddToResultCollectionPublic() {
        LinkedDocumentCollection ldc = getStandardCollection();
        LinkedDocument ld = (LinkedDocument) newLinkedDocument.newInstance(
                "Herr der Ringe",
                "Böser Mann macht bösen Ring. Muss zerstören!",
                "Im Auenland aß man gerade das siebente Frühstück, da kam Gandalf vorbei und ...",
                null,
                null,
                "j.r.r.tolkien.uk/lordoftherings",
                3
        );

        testAddToResultCollection(ldc, ld, new String[]{
                "franz.kafka.de/prozess",
                "schiller.de/räuber",
                "nietzsche.de/zarathustra",
                "platon.gr/politeia"
        });
    }

    @DisplayName("- | Public Tests for Method 'getNextUncrawledAddress()'")
    @PublicTest
    void testGetNextUncrawledAddressPublic() {
        LinkedDocumentCollection ldc = getStandardCollection();

        testGetNextUncrawledAddress(ldc, Set.of(
                "goethe.de/faust", "schiller.de/räuber", "j.r.r.tolkien.uk/lordoftherings"
        ));
    }

    // =============================================================================================== //
    // ----------------------- Hidden Tests for LinkedDocumentCollection Class ----------------------- //
    // =============================================================================================== //

    @DisplayName("0.5P | Hidden Tests for Method 'addToResultCollection()'")
    @HiddenTest
    void testAddToResultCollectionHidden() {
        LinkedDocumentCollection ldc = getStandardCollection();
        LinkedDocument ld = (LinkedDocument) newLinkedDocument.newInstance(
                "Herr der Ringe",
                "Böser Mann macht bösen Ring. Muss zerstören!",
                "Im Auenland aß man gerade das siebente Frühstück, da kam Gandalf vorbei und ...",
                null,
                null,
                "j.r.r.tolkien.uk/lordoftherings",
                3
        );

        testAddToResultCollection(ldc, ld, new String[]{
                "franz.kafka.de/prozess",
                "schiller.de/räuber",
                "nietzsche.de/zarathustra",
                "platon.gr/politeia"
        });

        LinkedDocumentCollection ldc2 = getStandardCollection2();
        LinkedDocument ld2 = (LinkedDocument) newLinkedDocument.newInstance(
                "Grundlagen der Informatik",
                "Alles, was man als Einsteiger der Informatik wissen muss",
                "Was weiß ich denn, was man als Einsteiger der Informatik wissen muss ...",
                null,
                null,
                "grundlagen-der-informatik.de/literatur",
                6
        );

        testAddToResultCollection(ldc2, ld2, new String[]{
                "pinguine.aq/buecher",
                "informatik-fuer-dummies.com/literatur",
                "grundlagen-der-mathematik.de/literatur"
        });

        LinkedDocumentCollection ldc3 = getStandardCollection2();
        LinkedDocument ld3 = (LinkedDocument) newLinkedDocument.newInstance(
                "Grundlagen der Informatik",
                "Alles, was man als Einsteiger der Informatik wissen muss",
                "Was weiß ich denn, was man als Einsteiger der Informatik wissen muss ...",
                null,
                null,
                "grundlagen-der-informatik.de/literatur",
                6
        );

        testAddToResultCollection(ldc3, ld3, new String[]{});
    }

    @DisplayName("0.5P | Hidden Tests for Method 'getNextUncrawledAddress()'")
    @HiddenTest
    void testGetNextUncrawledAddressHidden() {
        LinkedDocumentCollection ldc = getStandardCollection();

        testGetNextUncrawledAddress(ldc, Set.of(
                "goethe.de/faust", "schiller.de/räuber", "j.r.r.tolkien.uk/lordoftherings"
        ));

        LinkedDocumentCollection ldc2 = getStandardCollection2();

        testGetNextUncrawledAddress(ldc2, Set.of(
                "grundlagen-der-informatik.de/literatur", "informatik-fuer-dummies.com/literatur"
        ));
    }

    // ------------------------------------------- Helpers ------------------------------------------- //

    private LinkedDocumentCollection getStandardCollection() {
        LinkedDocumentCollection collection = (LinkedDocumentCollection) newLinkedDocumentCollection.newInstance(4);
        LinkedDocument prozess = (LinkedDocument) newLinkedDocument.newInstance(
                "Prozess",
                "Mann wird grundlos verhaftet",
                "Jemand musste Josef K. verleumdet haben",
                null,
                null,
                "franz.kafka.de/prozess",
                3
        );
        LinkedDocument got = (LinkedDocument) newLinkedDocument.newInstance(
                "A Game of Thrones",
                "Wie Herr der Ringe nur mit Intrigen",
                "Auf der Mauer, auf der Lauer ...",
                null,
                null,
                "george.r.r.martin.com/agameofthrones",
                2
        );
        DummyLinkedDocument faust = (DummyLinkedDocument) newDummyLinkedDocument.newInstance(
                "goethe.de/faust",
                2
        );
        DummyLinkedDocument raeuber = (DummyLinkedDocument) newDummyLinkedDocument.newInstance(
                "schiller.de/räuber",
                2
        );
        DummyLinkedDocument lotr = (DummyLinkedDocument) newDummyLinkedDocument.newInstance(
                "j.r.r.tolkien.uk/lordoftherings",
                2
        );

        // Incoming Link Collections
        LinkedDocumentCollection prozessIncoming = (LinkedDocumentCollection) incomingLinks.getOf(prozess);
        LinkedDocumentCollection faustIncoming = (LinkedDocumentCollection) incomingLinks.getOf(faust);
        LinkedDocumentCollection raeuberIncoming = (LinkedDocumentCollection) incomingLinks.getOf(raeuber);
        LinkedDocumentCollection lotrIncoming = (LinkedDocumentCollection) incomingLinks.getOf(lotr);

        // Outgoing Link Collections
        LinkedDocumentCollection prozessOutgoing = (LinkedDocumentCollection) outgoingLinks.getOf(prozess);
        LinkedDocumentCollection gotOutgoing = (LinkedDocumentCollection) outgoingLinks.getOf(got);

        // Adding all to main collection
        add.invokeOn(collection, prozess);
        add.invokeOn(collection, got);
        add.invokeOn(collection, faust);
        add.invokeOn(collection, raeuber);
        add.invokeOn(collection, lotr);

        // Add incoming links to respective incoming collections
        add.invokeOn(prozessIncoming, got);
        add.invokeOn(faustIncoming, prozess);
        add.invokeOn(faustIncoming, got);
        add.invokeOn(raeuberIncoming, prozess);
        add.invokeOn(lotrIncoming, got);

        // Add outgoing links to respective outgoing collections
        add.invokeOn(gotOutgoing, prozess);
        add.invokeOn(gotOutgoing, faust);
        add.invokeOn(gotOutgoing, lotr);
        add.invokeOn(prozessOutgoing, raeuber);
        add.invokeOn(prozessOutgoing, faust);

        return collection;
    }

    private LinkedDocumentCollection getStandardCollection2() {
        LinkedDocumentCollection collection = (LinkedDocumentCollection) newLinkedDocumentCollection.newInstance(4);
        LinkedDocument cleanCode = (LinkedDocument) newLinkedDocument.newInstance(
                "Clean Code",
                "Buch, mit dem man lernen kann, sauberen Code zu schreiben",
                "Lieber vierfach verschachtelte Schleifen vermeiden, nicht jede Zeile kommentieren, Variablen nicht 'var1', 'var2', 'var3' nennen usw.",
                null,
                null,
                "clean-code.com/books",
                2
        );
        LinkedDocument theo = (LinkedDocument) newLinkedDocument.newInstance(
                "Theoretische Informatik",
                "Einführung in die theoretische Informatik",
                "Wir definieren einen Automaten als 5-Tupel",
                null,
                null,
                "clean-code.com/buecher",
                2
        );
        LinkedDocument penguTheory = (LinkedDocument) newLinkedDocument.newInstance(
                "Einführung in die theoretische Pinguinistik",
                "Eine kurze Einführung in alles, was man über Pinguine wissen muss",
                "Wir definieren einen Pinguin als 5-Tupel ...",
                null,
                null,
                "pinguine.aq/buecher",
                5
        );
        DummyLinkedDocument basicsInfo = (DummyLinkedDocument) newDummyLinkedDocument.newInstance(
                "grundlagen-der-informatik.de/literatur",
                2
        );
        DummyLinkedDocument infoForDummies = (DummyLinkedDocument) newDummyLinkedDocument.newInstance(
                "informatik-fuer-dummies.com/literatur",
                7
        );

        // Incoming Link Collections
        LinkedDocumentCollection cleanCodeIncoming = (LinkedDocumentCollection) incomingLinks.getOf(cleanCode);
        LinkedDocumentCollection theoIncoming = (LinkedDocumentCollection) incomingLinks.getOf(theo);
        LinkedDocumentCollection basicsInfoIncoming = (LinkedDocumentCollection) incomingLinks.getOf(basicsInfo);
        LinkedDocumentCollection infoForDummiesIncoming = (LinkedDocumentCollection) incomingLinks.getOf(infoForDummies);

        // Outgoing Link Collections
        LinkedDocumentCollection cleanCodeOutgoing = (LinkedDocumentCollection) outgoingLinks.getOf(cleanCode);
        LinkedDocumentCollection theoOutgoing = (LinkedDocumentCollection) outgoingLinks.getOf(theo);
        LinkedDocumentCollection penguTheoryOutgoing = (LinkedDocumentCollection) outgoingLinks.getOf(penguTheory);

        // Adding all to main collection
        add.invokeOn(collection, cleanCode);
        add.invokeOn(collection, theo);
        add.invokeOn(collection, penguTheory);
        add.invokeOn(collection, basicsInfo);
        add.invokeOn(collection, infoForDummies);

        // Add incoming links to respective incoming collections
        add.invokeOn(cleanCodeIncoming, theo);
        add.invokeOn(theoIncoming, cleanCode);
        add.invokeOn(theoIncoming, penguTheory);
        add.invokeOn(basicsInfoIncoming, cleanCode);
        add.invokeOn(basicsInfoIncoming, theo);
        add.invokeOn(infoForDummiesIncoming, cleanCode);
        add.invokeOn(infoForDummiesIncoming, theo);
        add.invokeOn(infoForDummiesIncoming, penguTheory);

        // Add outgoing links to respective outgoing collections
        add.invokeOn(cleanCodeOutgoing, theo);
        add.invokeOn(cleanCodeOutgoing, basicsInfo);
        add.invokeOn(cleanCodeOutgoing, infoForDummies);
        add.invokeOn(theoOutgoing, cleanCode);
        add.invokeOn(theoOutgoing, basicsInfo);
        add.invokeOn(theoOutgoing, infoForDummies);
        add.invokeOn(penguTheoryOutgoing, theo);
        add.invokeOn(penguTheoryOutgoing, infoForDummies);

        return collection;
    }

}
