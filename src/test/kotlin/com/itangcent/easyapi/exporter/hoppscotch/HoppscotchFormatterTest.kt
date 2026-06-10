package com.itangcent.easyapi.exporter.hoppscotch

import com.google.gson.JsonParser
import com.itangcent.easyapi.exporter.model.ApiParameter
import com.itangcent.easyapi.exporter.model.HttpMethod
import com.itangcent.easyapi.exporter.model.ParameterBinding
import com.itangcent.easyapi.exporter.model.httpMetadata
import com.itangcent.easyapi.exporter.model.ApiEndpoint
import com.itangcent.easyapi.testFramework.EasyApiLightCodeInsightFixtureTestCase
import com.itangcent.easyapi.testFramework.TestConfigReader
import kotlinx.coroutines.runBlocking

class HoppscotchFormatterTest : EasyApiLightCodeInsightFixtureTestCase() {

    override fun createConfigReader() = TestConfigReader.empty(project)

    fun testFormatProducesHoppscotchImportableCollection(): Unit = runBlocking {
        val formatter = HoppscotchFormatter(project)
        val endpoint = ApiEndpoint(
            name = "查询用户",
            metadata = httpMetadata(
                path = "/api/users/{id}",
                method = HttpMethod.GET,
                parameters = listOf(
                    ApiParameter(name = "id", binding = ParameterBinding.Path, example = "1001"),
                    ApiParameter(name = "keyword", binding = ParameterBinding.Query, example = "chief-fei")
                )
            )
        )

        val json = formatter.format(listOf(endpoint), "用户接口")
        val root = JsonParser.parseString(json).asJsonObject
        val info = root.getAsJsonObject("info")

        assertEquals("用户接口", info.get("name").asString)
        assertTrue(root.getAsJsonArray("item").size() > 0)
        assertTrue(json.contains("/api/users/:id"))
        assertTrue(json.contains("chief-fei"))
    }
}
