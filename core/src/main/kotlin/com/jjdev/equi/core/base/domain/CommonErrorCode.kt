package com.jjdev.equi.core.base.domain

@Suppress("MagicNumber")
enum class CommonErrorCode(val code: Int) {
    NullPointerException(1),
    IllegalStateException(2),
    IllegalArgumentException(3),
    ArrayIndexOutOfBoundsException(4),
    SecurityCheck(99),
    Unknown(0);

    companion object {
        fun from(code: Int) = values().find { it.code == code } ?: Unknown
    }
}