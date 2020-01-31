package com.shallowinggg.narep.core.env;

/**
 * Interface indicating a component that contains and exposes an {@link Environment} reference.
 *
 * @author Chris Beams
 * @see Environment
 */
public interface EnvironmentCapable {

    /**
     * Return the {@link Environment} associated with this component.
     */
    Environment getEnvironment();
}
