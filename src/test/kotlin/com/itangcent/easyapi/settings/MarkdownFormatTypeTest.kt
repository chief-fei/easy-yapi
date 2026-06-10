package com.itangcent.easyapi.settings

import org.junit.Assert.*
import org.junit.Test

class MarkdownFormatTypeTest {

    @Test
    fun testValues() {
        val values = MarkdownFormatType.values()
        assertEquals(2, values.size)
        assertTrue(values.contains(MarkdownFormatType.SIMPLE))
        assertTrue(values.contains(MarkdownFormatType.ULTIMATE))
    }

    @Test
    fun testDesc() {
        assertEquals("基础列（名称、类型、描述）", MarkdownFormatType.SIMPLE.desc)
        assertEquals("完整列（名称、类型、必填、默认值、描述）", MarkdownFormatType.ULTIMATE.desc)
    }

    @Test
    fun testName() {
        assertEquals("SIMPLE", MarkdownFormatType.SIMPLE.name)
        assertEquals("ULTIMATE", MarkdownFormatType.ULTIMATE.name)
    }

    @Test
    fun testValueOf() {
        assertEquals(MarkdownFormatType.SIMPLE, MarkdownFormatType.valueOf("SIMPLE"))
        assertEquals(MarkdownFormatType.ULTIMATE, MarkdownFormatType.valueOf("ULTIMATE"))
    }

    @Test
    fun testToStringUsesChineseDescription() {
        assertEquals("基础列（名称、类型、描述）", MarkdownFormatType.SIMPLE.toString())
        assertEquals("完整列（名称、类型、必填、默认值、描述）", MarkdownFormatType.ULTIMATE.toString())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testValueOf_invalid() {
        MarkdownFormatType.valueOf("INVALID")
    }
}
