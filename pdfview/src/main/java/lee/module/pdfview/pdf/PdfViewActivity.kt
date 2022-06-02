package lee.module.pdfview.pdf

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lee.module.pdfview.R

class PdfViewActivity : AppCompatActivity() {

    private val pdfView: PdfView
        get() = findViewById(R.id.pdfView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pv_pdf_view)
        intent.data?.lastPathSegment?.takeUnless { it.isNullOrBlank() }.let { title = it }
        initPdfView(intent)
    }

    private fun initPdfView(intent: Intent) {
        val validUri = pdfView.setUri(intent.data)
        if (validUri.not()) {
            finish()
            return
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        pdfView.clear()
    }
}
