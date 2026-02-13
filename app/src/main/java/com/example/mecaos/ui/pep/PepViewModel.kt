package com.example.mecaos.ui.pep

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class PepViewModel(application: Application) : AndroidViewModel(application) {
    private val _pdfBitmap = MutableStateFlow<Bitmap?>(null)
    val pdfBitmap: StateFlow<Bitmap?> = _pdfBitmap.asStateFlow()

    init {
        viewModelScope.launch {
            val file = File(getApplication<Application>().cacheDir, "pep.pdf")
            if (!file.exists()) {
                getApplication<Application>().assets.open("pep.pdf").use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }

            val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            val page = pdfRenderer.openPage(0)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            _pdfBitmap.value = bitmap
            page.close()
            pdfRenderer.close()
            parcelFileDescriptor.close()
        }
    }
}
