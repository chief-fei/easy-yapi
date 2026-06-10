package com.itangcent.easyapi.exporter.channel

import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileWrapper
import com.itangcent.easyapi.core.threading.swing
import java.io.File

object FileExportSupport {

    suspend fun resolveTargetFile(
        project: Project,
        fileConfig: ChannelConfig.FileConfig?,
        defaultFileName: String,
        extension: String,
        dialogTitle: String,
        dialogDescription: String
    ): File? {
        val outputDir = fileConfig?.outputDir
        val fileName = fileConfig?.fileName
        if (!outputDir.isNullOrBlank()) {
            val dir = File(outputDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return File(dir, normalizeFileName(fileName, defaultFileName, extension))
        }
        return selectTargetFile(project, defaultFileName, dialogTitle, dialogDescription)
    }

    private suspend fun selectTargetFile(
        project: Project,
        defaultFileName: String,
        dialogTitle: String,
        dialogDescription: String
    ): File? {
        return swing {
            val descriptor = FileSaverDescriptor(dialogTitle, dialogDescription)
            val saver = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, project)
            val wrapper: VirtualFileWrapper? = saver.save(null as VirtualFile?, defaultFileName)
            wrapper?.file
        }
    }

    private fun normalizeFileName(fileName: String?, defaultFileName: String, extension: String): String {
        if (fileName.isNullOrBlank()) {
            return defaultFileName
        }
        val suffix = ".$extension"
        return if (fileName.endsWith(suffix, ignoreCase = true)) fileName else "$fileName$suffix"
    }
}
