package com.example.deslocations.core

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.deslocations.R
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ComposeFileProvider : FileProvider(
    R.xml.file_paths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()

            val timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val imageFileName = "JPEG_" + timestamp + "_"

            val file = File.createTempFile(
                imageFileName,
                ".jpg",
                directory
            )

            val authority = context.packageName + ".fileprovider"

            return getUriForFile(
                context,
                authority,
                file
            )
        }
    }
}