package com.gurunars.box

/**
 * A link between 2 or more entities that manifests the fact
 * that at least one of them is being observed by the others.
 */
interface Bond {
    /**
     * Removes the bond between the entities
     */
    fun drop()
}