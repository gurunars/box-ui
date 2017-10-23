package com.gurunars.crud_item_list

data class Status(
    val type: Type,
    val message: String
) {
    enum class Type(val isBlocking: Boolean) {
        INFO(false), WARNING(false), ERROR(true)
    }

    companion object {
        fun ok() = info("")
        fun error(msg: String) = Status(Type.ERROR, msg)
        fun warning(msg: String) = Status(Type.WARNING, msg)
        fun info(msg: String) = Status(Type.INFO, msg)
    }
}