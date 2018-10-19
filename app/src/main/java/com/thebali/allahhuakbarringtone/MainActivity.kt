package com.thebali.allahhuakbarringtone

import android.Manifest
import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import kotlinx.android.synthetic.main.activity_main.*
import android.media.RingtoneManager
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media.getContentUriForPath
import java.io.File
import org.jetbrains.anko.*
import permissions.dispatcher.*
import android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.System.canWrite



@RuntimePermissions
class MainActivity : AppCompatActivity(), AnkoLogger {

    val TAG = "ahaaaaa"
    // changing applicationContext does not work, nor the baseContext helps.
    // using the ActivityName as class with package name is a required.
    private val soundUri = Uri.parse("android.resource://"+ MainActivity::class.java.`package`!!.name +"/"+ R.raw.audio_allah)
    private val outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).absolutePath

    private val log = AnkoLogger(this.javaClass)

    //String PERM1 = Manifest.permission.WRITE_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener {
            saRequestPerms()
            ringtoneApplyWithPermissionCheck()
        }
        img1.setOnClickListener { ringPhone() }

        // check permissions for the storage
        //checkRequestPermission()

    }

    //its fucking too soon to call the views
//    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?) {
//        super.onCreateView(parent, name, context, attrs)
//    }



    /*
    *
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }


    fun saRequestPerms(){
        val context = applicationContext

        // Check whether has the write settings permission or not.
        val settingsCanWrite = Settings.System.canWrite(context)

        if (!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivity(intent)
        } else {
            // If has permission then show an alert dialog with message.
            alert("you have permissions now.").show()
        }
    }




    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun ringtoneApply(){

        val values = ContentValues()

        log.warn("$soundUri - sound Uri is displayed.")


        //toast("this the "+values.toString())

        val file1 = File.createTempFile(outPath, "allahakbar.mp3")
        values.put(MediaStore.MediaColumns.DATA, file1.absolutePath)
        values.put(MediaStore.MediaColumns.TITLE, R.string.allah1)
        values.put(MediaStore.MediaColumns.SIZE, file1.length())
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")   // assuming it's an mpeg, of course
        values.put(MediaStore.Audio.Media.ARTIST, R.string.artist_name)
        // values.put(MediaStore.Audio.Media.DURATION, duration);  // doesn't appear to be necessary if you don't know
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)

        

        val uri1 = getContentUriForPath(file1.absolutePath)
        val newUri = contentResolver.insert(uri1, values)

        toast("this si the toast"+ outPath!!.toString())

        //val newUri = getContentResolver().insert(uri, values)
        //val rMgr = RingtoneManager(applicationContext)

        try {
            RingtoneManager.setActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_RINGTONE, newUri)
        }
        catch (e: Exception){
            log.error(e)
        }
    }


    private fun ringPhone(){

        //best technique to resources files path
        val r = RingtoneManager.getRingtone(applicationContext, soundUri)
        //r.streamType = 10
        r.play()

        // default notification tone.
        // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        //val resID = resources.getIdentifier("audio_allah.mp3", "raw", packageName)
//        val mp = MediaPlayer.create(applicationContext, uriID)
//        mp.start()
    }
}
