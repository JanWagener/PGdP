package pgdp.searchengine;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Public
@UseLocale("")
@MirrorOutput
@StrictTimeout(30)
@Deadline("2022-02-07 05:30 Europe/Berlin")
@ActivateHiddenBefore("2022-01-28 17:30 Europe/Berlin")
@WhitelistPath(value = "../pgdp2021*w13h01**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface W13H01 {

}
