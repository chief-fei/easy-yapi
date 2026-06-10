package com.itangcent.easyapi.exporter.hoppscotch

import com.intellij.openapi.project.Project
import com.itangcent.easyapi.exporter.model.ApiEndpoint
import com.itangcent.easyapi.exporter.postman.PostmanFormatOptions
import com.itangcent.easyapi.exporter.postman.PostmanFormatter
import com.itangcent.easyapi.exporter.postman.model.postmanGson

class HoppscotchFormatter(
    private val project: Project,
    private val options: PostmanFormatOptions = PostmanFormatOptions(appendTimestamp = false)
) {

    suspend fun format(endpoints: List<ApiEndpoint>, collectionName: String): String {
        val collection = PostmanFormatter(
            project = project,
            options = options.copy(appendTimestamp = false)
        ).format(endpoints, collectionName)
        return postmanGson().toJson(collection)
    }
}
