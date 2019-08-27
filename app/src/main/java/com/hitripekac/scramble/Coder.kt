package com.hitripekac.scramble

class Coder {

    private var encodingMap: Array<String> = arrayOf(
            "0357198624",
            "0789123456",
            "0987645321",
            "0465798321",
            "0852962147",
            "0864897213",
            "0258147397",
            "0645978312",
            "3078945612",
            "0145678923"
    )
    private var decodingMap: Array<String> = arrayOf(
            "0357198624",
            "0456789123",
            "0987564321",
            "0987192465",
            "7368259140",
            "0879312645",
            "041752 338",
            "0897231564",
            "3890567234",
            "0189234567"
    )


    fun encodeCoordinate(coordinate: String, cipher: Int): String {
        return transform(coordinate, encodingMap[cipher])
    }

    fun decodeCoordinate(coordinate: String, cipher: Int): String {
        return transform(coordinate, decodingMap[cipher])
    }

    private fun transform(coordinates: String, cipher: String): String = coordinates
            .map { single -> single - '0' }
            .map { position ->
                if (position in 0..9) {
                    cipher[position]
                } else {
                    '0' + position
                }

            }.joinToString("")
}
