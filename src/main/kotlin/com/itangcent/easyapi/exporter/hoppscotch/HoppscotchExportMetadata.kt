package com.itangcent.easyapi.exporter.hoppscotch

import com.itangcent.easyapi.exporter.model.ExportMetadata

data class HoppscotchExportMetadata(val content: String) : ExportMetadata {
    override fun formatDisplay(): String? = "导出的 JSON 可直接导入 Hoppscotch"
}
