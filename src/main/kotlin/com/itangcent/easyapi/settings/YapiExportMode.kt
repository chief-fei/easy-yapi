package com.itangcent.easyapi.settings

/**
 * Export modes for YAPI platforms.
 *
 * Determines how existing APIs are handled during export.
 *
 * @param desc Description of the export mode
 */
enum class YapiExportMode(val desc: String) {
    /** Always update existing APIs */
    ALWAYS_UPDATE("始终更新已有接口"),
    /** Never update existing APIs */
    NEVER_UPDATE("始终保留已有接口"),
    /** Ask user for each conflict */
    ALWAYS_ASK("每次冲突都弹窗确认"),
    /** Update only when content has changed */
    UPDATE_IF_CHANGED("仅内容变化时更新");

    override fun toString(): String = desc
}
