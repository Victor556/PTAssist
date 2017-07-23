package com.putao.ptx.assistant

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.putao.ptx.assistant.utils.Constance
import com.putao.ptx.assistant.utils.MsgDialog

open class BaseActivity : AppCompatActivity() {

    open override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyStoragePermissions(this)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*do request function(into apk)*/
                    onNewIntent(intent)
                } else {

                    Toast.makeText(applicationContext,
                            "没有读写文件权限，无法启动学习助手！",
                            Toast.LENGTH_LONG).show()

                    /*do request function(exitapk)*/
                    finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    companion object {

        val ACTION_EXIT = "com.putao.assist.DETAIL"
        private val TAG = "BaseActivity"
        private val FILE_SELECT_CODE = 10001
        val TIME_SCAN = 1000


        /***
         * group:android.permission-group.STORAGE
         * permission:android.permission.READ_EXTERNAL_STORAGE
         * permission:android.permission.WRITE_EXTERNAL_STORAGE
         */
        private val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun getPath(context: Context, uri: Uri): String? {
            var res: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, proj, null, null, null)
            if (cursor!!.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(column_index)
            }
            cursor.close()
            return res
        }
    }


    fun verifyStoragePermissions(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < 23)
        /*******below android 6.0 */
        {
            return true
        }

        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE)
            return false
        } else {
            return true
        }
    }

    fun showFileChooser() {
        val intent = Intent(Intent./*ACTION_GET_CONTENT*/ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //intent.setType("*/*");
        //intent.setType("image/jpeg");//选择图片
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            val chooser = Intent.createChooser(intent, "Select a File to Upload")
            startActivityForResult(/*chooser*/intent, FILE_SELECT_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            Log.e(TAG, "showFileChooser: ", ex)
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
        }

    }

    var mSelectedPath = ""//Constance.OCR_PATH

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data ?: return
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                val uri = data.data
                val path = getPath(this, uri)//uri.getPath();
                if (path != null && (path.endsWith(".png") || path.endsWith(".jpg"))) {
                    mSelectedPath = path
                    //btnDetect.performClick();

                    val it = Intent(this, OcrResultActivity::class.java)
                    it.putExtra(Constance.KEY_IMG, mSelectedPath)
                    it.putExtra(Constance.KEY_SELECT, mSelectedPath)
                    startActivity(it)
                } else {
                    Toast.makeText(this, "请选择.jpg文件！", Toast.LENGTH_LONG).show()
                }
                Log.d(TAG, "onActivityResult: mSelectedPath:" + path!!)


                //                    Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                //                    String[] proj = {MediaStore.Images.Media.DATA};
                //                    Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                //                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //                    actualimagecursor.moveToFirst();
                //                    String img_path = actualimagecursor.getString(actual_image_column_index);
                //                    File file = new File(img_path);
                //                    Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();


            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
