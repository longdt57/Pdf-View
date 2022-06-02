package lee.module.sample

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

const val FILE_NAME = "file_example.pdf"

class MainActivity : AppCompatActivity() {

    private lateinit var getPdf: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.tvPdfView).setOnClickListener {
            val file = File(filesDir, FILE_NAME)
            val uri = FileProvider
                .getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file)
            startPdfActivity(uri)
        }

        getPdf = registerForActivityResult(ActivityResultContracts.GetContent()) {
            startPdfActivity(it)
        }

        findViewById<View>(R.id.btnOpenLocalFile).setOnClickListener {
            getPdf.launch("application/pdf")
        }

        lifecycleScope.launch(Dispatchers.IO) {
            readAssetAndMakeCopy()
        }
    }

    private fun readAssetAndMakeCopy() {
        val assetManager = assets
        var `in`: InputStream? = null
        var out: OutputStream? = null
        val file = File(filesDir, FILE_NAME)
        try {
            `in` = assetManager.open(FILE_NAME)
            out = openFileOutput(file.name, Context.MODE_PRIVATE)
            copyFile(`in`, out)
            `in`.close()
            `in` = null
            out.flush()
            out.close()
            out = null
        } catch (e: Exception) {
            Log.e("tag", e.message.orEmpty())
        }
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }

    private fun startPdfActivity(uri: Uri) {
        Intent(Intent.ACTION_VIEW).apply {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
            setDataAndType(uri, type)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(this)
        }
    }
}
