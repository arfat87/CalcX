package com.calcx.calculator.domain.converter

enum class UnitCategory(val displayName: String) {
    LENGTH("Length"),
    WEIGHT("Weight / Mass"),
    TEMPERATURE("Temperature"),
    AREA("Area"),
    VOLUME("Volume"),
    SPEED("Speed"),
    TIME("Time"),
    PRESSURE("Pressure"),
    ENERGY("Energy"),
    POWER("Power"),
    DATA("Data Storage"),
    ANGLE("Angle")
}

data class ConversionUnit(
    val id: String,
    val name: String,
    val symbol: String,
    val factorToBase: Double // Multiplier to convert 1 of this unit into base unit
)

object UnitConverterEngine {

    val categories: Map<UnitCategory, List<ConversionUnit>> = mapOf(
        UnitCategory.LENGTH to listOf(
            ConversionUnit("m", "Meter", "m", 1.0),
            ConversionUnit("km", "Kilometer", "km", 1000.0),
            ConversionUnit("cm", "Centimeter", "cm", 0.01),
            ConversionUnit("mm", "Millimeter", "mm", 0.001),
            ConversionUnit("um", "Micrometer", "µm", 1e-6),
            ConversionUnit("nm", "Nanometer", "nm", 1e-9),
            ConversionUnit("mi", "Mile", "mi", 1609.344),
            ConversionUnit("yd", "Yard", "yd", 0.9144),
            ConversionUnit("ft", "Foot", "ft", 0.3048),
            ConversionUnit("in", "Inch", "in", 0.0254),
            ConversionUnit("nmi", "Nautical Mile", "nmi", 1852.0)
        ),
        UnitCategory.WEIGHT to listOf(
            ConversionUnit("kg", "Kilogram", "kg", 1.0),
            ConversionUnit("g", "Gram", "g", 0.001),
            ConversionUnit("mg", "Milligram", "mg", 1e-6),
            ConversionUnit("t", "Metric Ton", "t", 1000.0),
            ConversionUnit("lb", "Pound", "lb", 0.45359237),
            ConversionUnit("oz", "Ounce", "oz", 0.028349523125),
            ConversionUnit("st", "Stone", "st", 6.35029318)
        ),
        UnitCategory.TEMPERATURE to listOf(
            ConversionUnit("c", "Celsius", "°C", 1.0),
            ConversionUnit("f", "Fahrenheit", "°F", 1.0),
            ConversionUnit("k", "Kelvin", "K", 1.0)
        ),
        UnitCategory.AREA to listOf(
            ConversionUnit("sq_m", "Square Meter", "m²", 1.0),
            ConversionUnit("sq_km", "Square Kilometer", "km²", 1e6),
            ConversionUnit("sq_cm", "Square Centimeter", "cm²", 0.0001),
            ConversionUnit("sq_mm", "Square Millimeter", "mm²", 1e-6),
            ConversionUnit("ha", "Hectare", "ha", 10000.0),
            ConversionUnit("ac", "Acre", "ac", 4046.8564224),
            ConversionUnit("sq_mi", "Square Mile", "mi²", 2589988.110336),
            ConversionUnit("sq_yd", "Square Yard", "yd²", 0.83612736),
            ConversionUnit("sq_ft", "Square Foot", "ft²", 0.09290304),
            ConversionUnit("sq_in", "Square Inch", "in²", 0.00064516)
        ),
        UnitCategory.VOLUME to listOf(
            ConversionUnit("l", "Liter", "L", 1.0),
            ConversionUnit("ml", "Milliliter", "mL", 0.001),
            ConversionUnit("cu_m", "Cubic Meter", "m³", 1000.0),
            ConversionUnit("cu_cm", "Cubic Centimeter", "cm³", 0.001),
            ConversionUnit("gal_us", "US Gallon", "gal", 3.785411784),
            ConversionUnit("qt_us", "US Quart", "qt", 0.946352946),
            ConversionUnit("pt_us", "US Pint", "pt", 0.473176473),
            ConversionUnit("cup_us", "US Cup", "cup", 0.2365882365),
            ConversionUnit("fl_oz_us", "US Fluid Ounce", "fl oz", 0.0295735295625),
            ConversionUnit("gal_uk", "Imperial Gallon", "gal (UK)", 4.54609)
        ),
        UnitCategory.SPEED to listOf(
            ConversionUnit("mps", "Meter per Second", "m/s", 1.0),
            ConversionUnit("kph", "Kilometer per Hour", "km/h", 0.2777777777777778),
            ConversionUnit("mph", "Mile per Hour", "mph", 0.44704),
            ConversionUnit("knot", "Knot", "kn", 0.5144444444444445),
            ConversionUnit("fps", "Foot per Second", "ft/s", 0.3048)
        ),
        UnitCategory.TIME to listOf(
            ConversionUnit("s", "Second", "s", 1.0),
            ConversionUnit("ms", "Millisecond", "ms", 0.001),
            ConversionUnit("us", "Microsecond", "µs", 1e-6),
            ConversionUnit("min", "Minute", "min", 60.0),
            ConversionUnit("h", "Hour", "h", 3600.0),
            ConversionUnit("d", "Day", "d", 86400.0),
            ConversionUnit("wk", "Week", "wk", 604800.0),
            ConversionUnit("mo", "Month (Avg)", "mo", 2629800.0),
            ConversionUnit("yr", "Year (365.25d)", "yr", 31557600.0)
        ),
        UnitCategory.PRESSURE to listOf(
            ConversionUnit("pa", "Pascal", "Pa", 1.0),
            ConversionUnit("kpa", "Kilopascal", "kPa", 1000.0),
            ConversionUnit("bar", "Bar", "bar", 100000.0),
            ConversionUnit("psi", "Pounds per Sq Inch", "psi", 6894.757293168),
            ConversionUnit("atm", "Standard Atmosphere", "atm", 101325.0),
            ConversionUnit("torr", "Torr / mmHg", "Torr", 133.322368421)
        ),
        UnitCategory.ENERGY to listOf(
            ConversionUnit("j", "Joule", "J", 1.0),
            ConversionUnit("kj", "Kilojoule", "kJ", 1000.0),
            ConversionUnit("cal", "Calorie", "cal", 4.184),
            ConversionUnit("kcal", "Kilocalorie", "kcal", 4184.0),
            ConversionUnit("wh", "Watt-hour", "Wh", 3600.0),
            ConversionUnit("kwh", "Kilowatt-hour", "kWh", 3.6e6),
            ConversionUnit("ev", "Electronvolt", "eV", 1.602176634e-19),
            ConversionUnit("btu", "BTU", "BTU", 1055.05585262)
        ),
        UnitCategory.POWER to listOf(
            ConversionUnit("w", "Watt", "W", 1.0),
            ConversionUnit("kw", "Kilowatt", "kW", 1000.0),
            ConversionUnit("mw", "Megawatt", "MW", 1e6),
            ConversionUnit("hp", "Horsepower (Imperial)", "hp", 745.69987158227)
        ),
        UnitCategory.DATA to listOf(
            ConversionUnit("b", "Byte", "B", 1.0),
            ConversionUnit("kb", "Kilobyte (KB)", "KB", 1024.0),
            ConversionUnit("mb", "Megabyte (MB)", "MB", 1024.0 * 1024.0),
            ConversionUnit("gb", "Gigabyte (GB)", "GB", 1024.0 * 1024.0 * 1024.0),
            ConversionUnit("tb", "Terabyte (TB)", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0),
            ConversionUnit("bit", "Bit", "bit", 0.125),
            ConversionUnit("kbit", "Kilobit", "kbit", 128.0),
            ConversionUnit("mbit", "Megabit", "Mbit", 131072.0),
            ConversionUnit("gbit", "Gigabit", "Gbit", 134217728.0)
        ),
        UnitCategory.ANGLE to listOf(
            ConversionUnit("deg", "Degree", "°", 1.0),
            ConversionUnit("rad", "Radian", "rad", 57.29577951308232),
            ConversionUnit("grad", "Gradian", "grad", 0.9),
            ConversionUnit("arcmin", "Arcminute", "arcmin", 1.0 / 60.0),
            ConversionUnit("arcsec", "Arcsecond", "arcsec", 1.0 / 3600.0)
        )
    )

    fun convert(
        category: UnitCategory,
        fromUnitId: String,
        toUnitId: String,
        value: Double
    ): Double {
        if (fromUnitId == toUnitId) return value

        if (category == UnitCategory.TEMPERATURE) {
            return convertTemperature(fromUnitId, toUnitId, value)
        }

        val units = categories[category] ?: throw IllegalArgumentException("Unknown category: $category")
        val fromUnit = units.find { it.id == fromUnitId } ?: throw IllegalArgumentException("Unknown from unit: $fromUnitId")
        val toUnit = units.find { it.id == toUnitId } ?: throw IllegalArgumentException("Unknown to unit: $toUnitId")

        val baseValue = value * fromUnit.factorToBase
        return baseValue / toUnit.factorToBase
    }

    private fun convertTemperature(fromId: String, toId: String, value: Double): Double {
        // First convert to Celsius
        val celsius = when (fromId) {
            "c" -> value
            "f" -> (value - 32.0) * 5.0 / 9.0
            "k" -> value - 273.15
            else -> throw IllegalArgumentException("Unknown temperature unit: $fromId")
        }

        // Convert Celsius to target
        return when (toId) {
            "c" -> celsius
            "f" -> (celsius * 9.0 / 5.0) + 32.0
            "k" -> celsius + 273.15
            else -> throw IllegalArgumentException("Unknown temperature unit: $toId")
        }
    }
}
