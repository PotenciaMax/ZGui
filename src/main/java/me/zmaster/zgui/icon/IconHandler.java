package me.zmaster.zgui.icon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as an icon handler in a menu class.
 * The annotated method will be called to provide an Icon for the given key.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IconHandler {

    /**
     * The icon key this method handles.
     * This key corresponds to the icon's section name in the menu's configuration file (.yml).
     *
     * @return the icon key string from the menu config
     */
    String value();

    /**
     * The priority of this icon handler.
     * Determines the order in which handlers are applied,
     * with higher priority handlers executed first.
     *
     * @return the priority (default is MEDIUM)
     */

    IconPriority priority() default IconPriority.MEDIUM;
}
