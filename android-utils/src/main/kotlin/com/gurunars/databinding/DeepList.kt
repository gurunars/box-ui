package com.gurunars.databinding

class DeepList<T>(var array: List<T>) {

    override fun equals(other: Any?): Boolean {
        if (!(other != null && other is List<*>)) return false

        if (array.size != other.size) {
            return false
        }

        for (i in array.indices) {
            if (array[i] != other[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode() = array.hashCode()

}