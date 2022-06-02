package lee.module.pdfview.pdf

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PreloadLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return 5 * getScreenHeight()
    }
}
