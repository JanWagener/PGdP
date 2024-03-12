package pgdp.encapsulation;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.text.*;
import java.util.*;

import static de.tum.in.test.api.util.ReflectionTestUtils.*;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@Public
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
class NoTests {

    @Public
    @Test
    public void tellNoTests() {
        fail("Für diese Aufgabe gibt es keine automatischen Tests, da kein neues Verhalten zu implementieren ist." +
                " Stattdessen sollst du herausfinden, ob du die 'geometry'-Bibliothek so abändern kannst," +
                " wie in der Aufgabenstellung gefordert, unter der Randbedingung, dass das 'geometryuser'-Package kompiliert.");
    }
}
