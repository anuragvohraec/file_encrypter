package np.com.sarbagyastha.file_encrypter

import android.util.Base64
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class FileEncrypterPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var result: Result
    private val algorithm = "AES"
    private val transformation = "AES/CBC/PKCS5Padding"

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "file_encrypter")
        channel.setMethodCallHandler(this)
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "file_encrypter")
            channel.setMethodCallHandler(FileEncrypterPlugin())
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        this.result = result
        CoroutineScope(IO).launch {
            when (call.method) {
                "encrypt" -> encrypt(call.argument("inFileName"), call.argument("outFileName"))
                "decrypt" -> decrypt(call.argument("key"), call.argument("inFileName"), call.argument("outFileName"))
                else -> withContext(Main) {
                    result.notImplemented()
                }
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private suspend fun successResult(data: String?) {
        withContext(Main) {
            result.success(data)
        }
    }

    private suspend fun errorResult(message: String?) {
        withContext(Main) {
            result.error("", message, message)
        }
    }

    private suspend fun encrypt(inFileName: String?, outFileName: String?) {
        val cipher = Cipher.getInstance(transformation)
        val secretKey = KeyGenerator.getInstance("AES").generateKey()

        try {
            FileOutputStream(outFileName!!).use { fileOut ->
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                CipherOutputStream(fileOut, cipher).use { cipherOut ->
                    fileOut.write(cipher.iv)
                    val buffer = ByteArray(8192)
                    FileInputStream(inFileName!!).use { fileIn ->
                        var byteCount = fileIn.read(buffer)
                        while (byteCount != -1) {
                            cipherOut.write(buffer, 0, byteCount)
                            byteCount = fileIn.read(buffer)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorResult(e.message)
            return
        }

        val keyString = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
        successResult(keyString)
    }

    private suspend fun decrypt(key: String?, inFileName: String?, outFileName: String?) {
        val cipher = Cipher.getInstance(transformation)
        val encodedKey = Base64.decode(key, Base64.DEFAULT)
        val secretKey = SecretKeySpec(encodedKey, 0, encodedKey.size, algorithm)

        try {
            FileInputStream(inFileName!!).use { fileIn ->
                val fileIv = ByteArray(16)
                fileIn.read(fileIv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(fileIv))
                CipherInputStream(fileIn, cipher).use { cipherIn ->
                    val buffer = ByteArray(8192)
                    FileOutputStream(outFileName!!).use { fileOut ->
                        var byteCount = cipherIn.read(buffer)
                        while (byteCount != -1) {
                            fileOut.write(buffer, 0, byteCount)
                            byteCount = cipherIn.read(buffer)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorResult(e.message)
            return
        }
        successResult(null)
    }
}
