package com.calcx.calculator.domain.programmer

enum class NumberBase(val radix: Int, val prefix: String) {
    HEX(16, "0x"),
    DEC(10, ""),
    OCT(8, "0o"),
    BIN(2, "0b")
}

enum class BitWidth(val bits: Int, val mask: Long) {
    QWORD(64, -1L),
    DWORD(32, 0xFFFFFFFFL),
    WORD(16, 0xFFFFL),
    BYTE(8, 0xFFL)
}

data class ProgrammerState(
    val rawValue: Long = 0L,
    val activeBase: NumberBase = NumberBase.DEC,
    val bitWidth: BitWidth = BitWidth.QWORD,
    val isSigned: Boolean = true
) {
    fun maskedValue(): Long {
        return when (bitWidth) {
            BitWidth.QWORD -> rawValue
            BitWidth.DWORD -> rawValue and bitWidth.mask
            BitWidth.WORD -> rawValue and bitWidth.mask
            BitWidth.BYTE -> rawValue and bitWidth.mask
        }
    }

    fun toFormattedString(base: NumberBase): String {
        val v = maskedValue()
        return when (base) {
            NumberBase.HEX -> java.lang.Long.toHexString(v).uppercase()
            NumberBase.DEC -> if (isSigned) signedValue().toString() else java.lang.Long.toUnsignedString(v)
            NumberBase.OCT -> java.lang.Long.toOctalString(v)
            NumberBase.BIN -> {
                val bin = java.lang.Long.toBinaryString(v)
                // Group binary in 4-bit nibbles
                bin.padStart(bitWidth.bits, '0').chunked(4).joinToString(" ")
            }
        }
    }

    fun signedValue(): Long {
        val v = maskedValue()
        return when (bitWidth) {
            BitWidth.QWORD -> v
            BitWidth.DWORD -> v.toInt().toLong()
            BitWidth.WORD -> v.toShort().toLong()
            BitWidth.BYTE -> v.toByte().toLong()
        }
    }
}

object ProgrammerEngine {

    fun bitwiseAnd(a: Long, b: Long, width: BitWidth): Long {
        return (a and b) and width.mask
    }

    fun bitwiseOr(a: Long, b: Long, width: BitWidth): Long {
        return (a or b) and width.mask
    }

    fun bitwiseXor(a: Long, b: Long, width: BitWidth): Long {
        return (a xor b) and width.mask
    }

    fun bitwiseNot(a: Long, width: BitWidth): Long {
        return (a.inv()) and width.mask
    }

    fun shiftLeft(a: Long, shift: Int, width: BitWidth): Long {
        return (a shl shift) and width.mask
    }

    fun shiftRight(a: Long, shift: Int, width: BitWidth): Long {
        return (a ushr shift) and width.mask
    }

    fun parseInput(text: String, base: NumberBase, width: BitWidth): Long {
        val clean = text.replace(" ", "").trim()
        if (clean.isEmpty()) return 0L
        val parsed = java.lang.Long.parseUnsignedLong(clean, base.radix)
        return parsed and width.mask
    }
}
