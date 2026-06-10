package com.itangcent.easyapi.settings

/**
 * Export modes for Postman collections.
 *
 * Determines how collections are handled during export.
 *
 * @param desc Description of the export mode
 */
enum class PostmanExportMode(val desc: String) {
    /** Always create a new collection */
    CREATE_NEW("始终创建新集合"),
    /** Update existing collections by module */
    UPDATE_EXISTING("优先更新已有集合");

    override fun toString(): String = desc
}
