package lee.module.pdfview.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lee.module.pdfview.databinding.PvPdfItemBinding

class PdfViewAdapter(
    private val pdfRenderer: PdfRenderer
) : RecyclerView.Adapter<PdfViewAdapter.PdfViewHolder>() {

    private val cachePages: MutableMap<Int, Bitmap> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val binding =
            PvPdfItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PdfViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pdfRenderer.pageCount
    }

    private fun getBitmapAtPage(position: Int): Bitmap {
        return cachePages[position] ?: getBitmapFromPdfFile(position).apply {
            cachePages[position] = this
        }
    }

    private fun getBitmapFromPdfFile(position: Int): Bitmap {
        val currentPage = pdfRenderer.openPage(position)
        val bitmap = Bitmap.createBitmap(
            currentPage.width,
            currentPage.height,
            Bitmap.Config.ARGB_8888
        )
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        currentPage.close()
        return bitmap
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.bind(getBitmapAtPage(position))
    }

    fun clear() {
        cachePages.clear()
    }

    class PdfViewHolder(private val binding: PvPdfItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap) {
            binding.ivContent.setImageBitmap(bitmap)
        }
    }
}
