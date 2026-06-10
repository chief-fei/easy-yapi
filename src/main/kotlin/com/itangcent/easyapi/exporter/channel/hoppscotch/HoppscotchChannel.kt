package com.itangcent.easyapi.exporter.channel.hoppscotch

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.itangcent.easyapi.core.threading.background
import com.itangcent.easyapi.core.threading.swing
import com.itangcent.easyapi.exporter.channel.ApiChannel
import com.itangcent.easyapi.exporter.channel.ChannelConfig
import com.itangcent.easyapi.exporter.channel.ChannelOptionsPanel
import com.itangcent.easyapi.exporter.channel.FileExportSupport
import com.itangcent.easyapi.exporter.hoppscotch.HoppscotchExportMetadata
import com.itangcent.easyapi.exporter.hoppscotch.HoppscotchFormatter
import com.itangcent.easyapi.exporter.model.ExportContext
import com.itangcent.easyapi.exporter.model.ExportResult
import com.itangcent.easyapi.exporter.model.isHttp
import com.itangcent.easyapi.logging.IdeaLog
import com.itangcent.easyapi.settings.PostmanJson5FormatType
import com.itangcent.easyapi.settings.SettingBinder
import com.itangcent.easyapi.exporter.postman.PostmanFormatOptions
import kotlinx.coroutines.CancellationException
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class HoppscotchChannel : ApiChannel, IdeaLog {

    override val id: String = "hoppscotch"
    override val displayName: String = "Hoppscotch"
    override val exposeAsAction: Boolean = true
    override val actionText: String = "导出到 Hoppscotch"

    override fun createOptionsPanel(project: Project): ChannelOptionsPanel {
        return HoppscotchOptionsPanel(project)
    }

    override suspend fun export(context: ExportContext): ExportResult {
        val httpEndpoints = context.endpointsToExport.filter { it.isHttp }
        if (httpEndpoints.isEmpty()) {
            return ExportResult.Error("Hoppscotch 仅支持导出 HTTP 接口")
        }

        val settings = SettingBinder.getInstance(context.project).read()
        val formatter = HoppscotchFormatter(
            project = context.project,
            options = PostmanFormatOptions(
                buildExample = settings.postmanBuildExample,
                autoMergeScript = settings.autoMergeScript,
                wrapCollection = settings.wrapCollection,
                json5FormatType = runCatching {
                    PostmanJson5FormatType.valueOf(settings.postmanJson5FormatType)
                }.getOrDefault(PostmanJson5FormatType.EXAMPLE_ONLY),
                appendTimestamp = false
            )
        )
        val content = formatter.format(httpEndpoints, context.project.name)
        return ExportResult.Success(
            count = httpEndpoints.size,
            target = "Hoppscotch",
            metadata = HoppscotchExportMetadata(content)
        )
    }

    override suspend fun handleResult(
        project: Project,
        result: ExportResult.Success,
        config: ChannelConfig
    ): Boolean {
        val metadata = result.metadata as? HoppscotchExportMetadata ?: return false
        val targetFile = FileExportSupport.resolveTargetFile(
            project = project,
            fileConfig = config as? ChannelConfig.FileConfig,
            defaultFileName = "hoppscotch_collection.json",
            extension = "json",
            dialogTitle = "保存 Hoppscotch 集合",
            dialogDescription = "选择 Hoppscotch 导出文件的保存位置"
        ) ?: throw CancellationException("User cancelled file selection")

        background {
            targetFile.writeText(metadata.content)
        }
        LOG.info("Hoppscotch collection exported to ${targetFile.absolutePath}")

        swing {
            Messages.showInfoMessage(
                project,
                "已成功导出 ${result.count} 个接口到 ${targetFile.absolutePath}",
                "导出 API"
            )
        }
        return true
    }
}

private class HoppscotchOptionsPanel(private val project: Project) : ChannelOptionsPanel {

    private val outputDirField = com.intellij.openapi.ui.TextFieldWithBrowseButton().apply {
        text = project.basePath ?: ""
        addBrowseFolderListener(
            project,
            com.intellij.openapi.fileChooser.FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withTitle("选择输出目录")
                .withDescription("选择 Hoppscotch 导出文件的输出目录")
        )
    }

    private val fileNameField = com.intellij.ui.components.JBTextField().apply {
        text = "hoppscotch_export"
        columns = 30
    }

    override val component: JComponent = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JPanel(BorderLayout()).apply {
            add(JLabel("输出目录："), BorderLayout.WEST)
            add(outputDirField, BorderLayout.CENTER)
        })
        add(Box.createVerticalStrut(5))
        add(JPanel(BorderLayout()).apply {
            add(JLabel("文件名（不含扩展名）："), BorderLayout.WEST)
            add(fileNameField, BorderLayout.CENTER)
        })
    }

    override fun buildConfig(): ChannelConfig.FileConfig {
        return ChannelConfig.FileConfig(
            outputDir = outputDirField.text.takeIf { it.isNotBlank() },
            fileName = fileNameField.text.takeIf { it.isNotBlank() }
        )
    }
}
