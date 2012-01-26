/**
 * <p>
 *     JCActor is a final class which supports one type of request message, Include, which adds a Component to the
 *     actor. Each Component provides a list of other components that are to be included as well, unless they have
 *     already been included of course. The open method of a component is called after it, and all the components it
 *     identifies for inclusion, have been included. Typically when open is called on a component it calls the
 *     Internals.bind method, providing the actor with the means to process additional types of request messages.
 * </p>
 */
package org.agilewiki.jactor.components;