package com.itangcent.easyapi.settings

/**
 * Format types for Markdown output.
 *
 * Controls the level of detail in generated Markdown tables.
 *
 * @param desc Description of the format type
 */
enum class MarkdownFormatType(val desc: String) {
    /** Simple format with name, type, and description columns */
    SIMPLE("基础列（名称、类型、描述）"),
    /** Ultimate format with additional required, default columns */
    ULTIMATE("完整列（名称、类型、必填、默认值、描述）");

    override fun toString(): String = desc
}
