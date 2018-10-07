package com.thebali.allahhuakbarringtone

import android.content.ContentValues
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.media.RingtoneManager
import android.media.Ringtone
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media.getContentUriForPath
import java.io.File
import android.app.NotificationManager
import android.app.NotificationChannel
import android.media.AudioAttributes
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity(), AnkoLogger {

    //val TAG = "aha"
    // changing applicationContext does not work, nor the baseContext helps.
    // using the ActivityName as class with package name is a required.
    private val soundUri = Uri.parse("android.resource://"+ MainActivity::class.java.`package`.name +"/"+ R.raw.audio_allah)
    private val outPath = Environment.getExternalStorageDirectory().absolutePath

    private val log = AnkoLogger(this.javaClass)
    private val Tag = AnkoLogger("my_tag")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener { ringtoneApply() }
        img1.setOnClickListener { ringPhone() }

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






    private fun ringtoneApply(){


        val values = ContentValues()

        log.warn("$soundUri - is displayed.")


        val file1 = File(outPath, "allahakbar.mp3")
        values.put(MediaStore.MediaColumns.DATA, file1.absolutePath)
        values.put(MediaStore.MediaColumns.TITLE, R.string.allah1)
        values.put(MediaStore.MediaColumns.SIZE, file1.length())
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")   // assuming it's an mpeg, of course
        values.put(MediaStore.Audio.Media.ARTIST, R.string.artist_name)
// values.put(MediaStore.Audio.Media.DURATION, duration);  // doesn't appear to be necessary if you don't know
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)

        val uri1 = getContentUriForPath(file1.absolutePath)
        val newUri = contentResolver.insert(uri1, values)

        //val newUri = getContentResolver().insert(uri, values)
        val rMgr = RingtoneManager(applicationContext)

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
        r.play()

        // default notification tone.
        // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        //val resID = resources.getIdentifier("audio_allah.mp3", "raw", packageName)
//        val mp = MediaPlayer.create(applicationContext, uriID)
//        mp.start()
    }
}
