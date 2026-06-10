package com.itangcent.easyapi.settings

/**
 * Format types for JSON5 output in Postman exports.
 *
 * JSON5 is an extension of JSON that supports comments.
 * This enum controls where JSON5 format is used.
 *
 * ## Values
 * - NONE - Never use JSON5
 * - REQUEST_ONLY - Use JSON5 for request bodies
 * - RESPONSE_ONLY - Use JSON5 for response bodies
 * - EXAMPLE_ONLY - Use JSON5 for examples only
 * - ALL - Always use JSON5
 */
enum class PostmanJson5FormatType(val desc: String, private val types: Int) {
    NONE("不使用 JSON5", 0),
    REQUEST_ONLY("仅请求体使用 JSON5", 1 or 4),
    RESPONSE_ONLY("仅响应体使用 JSON5", 8),
    EXAMPLE_ONLY("仅示例使用 JSON5", 4 or 8),
    ALL("全部使用 JSON5", 1 or 4 or 8);

    /**
     * Checks if JSON5 should be used for the given type.
     *
     * @param type The type to check (1=request, 4=example, 8=response)
     * @return true if JSON5 should be used
     */
    fun needUseJson5(type: Int): Boolean {
        return (this.types and type) != 0
    }

    override fun toString(): String = desc
}
