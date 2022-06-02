package lee.module.pdfview.pdf

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PdfView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = PreloadLayoutManager(context)
    }

    fun setPdfRenderer(renderer: PdfRenderer) {
        adapter = PdfViewAdapter(renderer)
    }

    fun setUri(uri: Uri?): Boolean {
        if (uri == null) {
            showErrorToast("invalid $uri")
            return false
        }

        val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r") ?: let {
            showErrorToast("invalid $uri")
            return false
        }

        adapter = PdfViewAdapter(PdfRenderer(fileDescriptor))
        return true
    }

    private fun showErrorToast(errorMessage: String) {
        Toast.makeText(context, "PdfView: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    fun clear() {
        (adapter as? PdfViewAdapter)?.clear()
    }
}
