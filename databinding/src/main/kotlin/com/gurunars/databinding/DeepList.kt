package com.gurunars.databinding

class DeepList<T>(var array: List<T>) {

    override fun equals(other: Any?): Boolean {
        return other != null &&
               other is List<*> &&
               array.size == other.size &&
               array.indices.none { array[it] != other[it] }
    }

    override fun hashCode() = array.hashCode()

}