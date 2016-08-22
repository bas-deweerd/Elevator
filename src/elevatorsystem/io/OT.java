/*
 *  Copyright Pieter van den Hombergh 2010/.
 *  Fontys Hogeschool voor Techniek en logistiek Venlo Netherlands.
 *  Software Engineering. Website: http://www.fontysvenlo.org
 *  This file may be used distributed under GPL License V2.
 */

package elevatorsystem.io;

/**
 * Output types.
 *
 * @author hom
 */
public enum OT {
    /** Where is the elevatorsystem.elevator. */
    FLOORINDICATOR,
    /** Motor up bit. */
    MOTORUP,
    /** Motor down bit. */
    MOTORDOWN,
    /** Up led on floor panel. */
    UPLED,
    /** Down led on floor panel. */
    DOWNLED,
    /** Door open command bit. */
    DOORMOTOROPEN,
    /** Door close command bit. */
    DOORMOTORCLOSE,
    /** Test light. */
    BUZZER
}
