package pgdp.searchengine.testing;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;

import de.tum.in.test.api.jupiter.PublicTest;
import pgdp.searchengine.W10H01;

@W10H01
public class Testing {

    @DisplayName("3P | Reservierte Punkte für Tests des Studierenden")
    @PublicTest
    void testing() {
        fail("Dieser Test schlägt immer fehl. Deine eigenen Tests werden von einem Tutor angeschaut. Sind sie in Ordnung, bekommst du die drei Punkte, die für diesen Test reserviert wurden.");
    }

}
