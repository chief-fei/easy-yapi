package com.itangcent.easyapi.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class ExportModeDisplayTextTest {

    @Test
    fun testPostmanExportModeUsesChineseDescription() {
        assertEquals("始终创建新集合", PostmanExportMode.CREATE_NEW.toString())
        assertEquals("优先更新已有集合", PostmanExportMode.UPDATE_EXISTING.toString())
    }

    @Test
    fun testPostmanJson5FormatTypeUsesChineseDescription() {
        assertEquals("不使用 JSON5", PostmanJson5FormatType.NONE.toString())
        assertEquals("全部使用 JSON5", PostmanJson5FormatType.ALL.toString())
    }

    @Test
    fun testYapiExportModeUsesChineseDescription() {
        assertEquals("始终更新已有接口", YapiExportMode.ALWAYS_UPDATE.toString())
        assertEquals("每次冲突都弹窗确认", YapiExportMode.ALWAYS_ASK.toString())
    }
}
