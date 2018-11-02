package com.thebali.allahhuakbarringtone

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import kotlinx.android.synthetic.main.activity_main.*
import android.media.RingtoneManager
import android.os.Environment
import org.jetbrains.anko.*
import permissions.dispatcher.*
import android.content.Intent
import android.provider.Settings
import java.io.*
import java.lang.Error


@RuntimePermissions
class MainActivity : AppCompatActivity(), AnkoLogger {

    // changing applicationContext does not work, nor the baseContext helps.
    // using the ActivityName as class with package name is a required.
    private val soundUri = Uri.parse("android.resource://" + MainActivity::class.java.`package`!!.name + "/" + R.raw.audio_allah)
    //private val outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).absolutePath

    private val logger1 = AnkoLogger(this.javaClass)

    //String PERM1 = Manifest.permission.WRITE_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener {
            ringtoneApplyWithPermissionCheck()

            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                toast(Environment.getExternalStorageState().toString())
            }
        }
        btn2.setOnClickListener { saRequestPerms() }
        img1.setOnClickListener { ringPhone() }

    }

    /*
    *
    * *
    * *
    * Separate notification material
    *
    * *
    * *
    private fun ringtoneApply(){
        //val soundUri = Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.audio_allah)

        log.warn("$soundUri is liveee.")

        val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_ALARM)
        .build()

        // Creating Channel
        val channel = NotificationChannel("id1",
        "allah_hu_akbar_app",
        NotificationManager.IMPORTANCE_HIGH)
        channel.setSound(soundUri, audioAttributes)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .createNotificationChannel(channel)

    }

    */


    /**
     * the anko and permissionsDispatcher are working great
     *
     *
     *
     *
     * */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }


    /**
     *
     * Fuck this version of the ringtoneApply()
     *
     * we need to make new fresh version according to our new requirements.
     *
     *

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun ringtoneApply(){

    val values = ContentValues()

    log.warn("$soundUri - sound Uri is displayed.")


    //toast("this the "+values.toString())

    val file1 = File(outPath, "allahakbar.mp3")


    //        val os = FileOutputStream(file1)
    //        //val data = "This is the content of my file"
    //        os.write(R.raw.audio_allah)
    //        os.close()


    // verification, if file exists,


    // read the file from the resources
    val f = baseContext.resources.openRawResource(R.raw.audio_allah)
    val size = f.available()

    val fsf = f.readBytes()
    f.close()

    // write the file to the location outPath
    val save = FileOutputStream(file1)
    save.write(fsf)
    save.flush()
    save.close()

    // the verification of the file
    val file2 = File(outPath, "allahakbar.mp3")
    if (file2.exists()){
    longToast("the file exists..")
    log.warn { "the file exists" }
    }
    else{
    log.warn(" te does nit exists")
    }


    values.put(MediaStore.MediaColumns.DATA, R.raw.audio_allah)
    values.put(MediaStore.MediaColumns.TITLE, R.string.allah1)
    values.put(MediaStore.MediaColumns.SIZE, file1.length())
    values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")   // assuming it's an mpeg, of course
    values.put(MediaStore.Audio.Media.ARTIST, R.string.artist_name)
    // values.put(MediaStore.Audio.Media.DURATION, duration);  // doesn't appear to be necessary if you don't know
    values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true)


    val uri1 = getContentUriForPath(file1.absolutePath)
    val newUri = contentResolver.insert(uri1, values)

    toast("this si the toast"+ outPath!!.toString())

    //val newUri = getContentResolver().insert(uri, values)
    //val rMgr = RingtoneManager(applicationContext)

    try {
    RingtoneManager.setActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_NOTIFICATION, newUri)
    }
    catch (e: Exception){
    log.error(e)
    }
    }

     *
     */

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun ringtoneApply() {
        // Create a path where we will place our private file on external
        // storage.

        val file = File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), "allah_akbar.mp3")

        if (!file.exists()) {
            try {
                // Very simple code to copy a picture from the application's
                // resource into the external file.  Note that this code does
                // no error checking, and assumes the picture is small (does not
                // try to copy it in chunks).  Note that if external storage is
                // not currently mounted this will silently fail.
                val in1 = resources.openRawResource(R.raw.audio_allah)
                val out1 = FileOutputStream(file)

                //val data = ByteArray(in1.available())  // kotlin equivalent of the java byte
                val data = in1.readBytes() // kotlin.io designed functions for this task.

                in1.read(data)
                out1.write(data)
                in1.close()
                out1.close()
            } catch (e: IOException) {
                // Unable to create file, likely because external storage is
                // not currently mounted.
                logger1.warn("ExternalStorage Error writing " + file.toString())
            }
        }
        else {
            logger1.warn("message:: file already exists, trying to apply the ringtone.")
        }

        try {
            val fileURI = Uri.fromFile(file)
            RingtoneManager.setActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_NOTIFICATION, fileURI)
            toast("Ringtone is applied...")
        } catch (e: Error) {
            logger1.error { e }
            toast("Error Occurred: See log for details.")
        }




    }


    private fun ringPhone() {

        //best technique to resources files path
        val r = RingtoneManager.getRingtone(applicationContext, soundUri)
        //r.streamType = 10
        r.play()

    }



    private fun saRequestPerms() {
        // Check whether has the write settings permission or not.
        val settingsCanWrite = Settings.System.canWrite(applicationContext)

        if (!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivity(intent)
        } else {
            // If has permission then show an alert dialog with message.
            toast("WRITE SETTINGS Permission has been granted already.")
            //alert("you have permissions now.").show()
        }
    }


//
//    private fun hasFile(): Boolean {
//        // Get path for the file on external storage.  If external
//        // storage is not currently mounted this will fail.
//        //val file = File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), "allah_akbar.mp3")
//        return file.exists()
//    }


}

