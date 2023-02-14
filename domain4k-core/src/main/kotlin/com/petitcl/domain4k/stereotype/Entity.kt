package com.petitcl.domain4k.stereotype


/**
 * Marker interface for a domain entity
 * @param Id the type of the entity's id (eg: String, Long, UUID, etc.)
 * @param T the type of the entity
 */
interface DomainEntity<Id, T> : Eq where T : DomainEntity<Id, T> {
    val id: Id
}
