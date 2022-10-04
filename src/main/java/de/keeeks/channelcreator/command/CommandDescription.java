package de.keeeks.channelcreator.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandDescription {
    /**
     * Name of the command
     */
    String name() default "";

    /**
     * Description of the command
     */
    String description() default "";

    /**
     * Aliases of the command
     */
    String[] aliases() default {};
}