package com.luismakeapp.sindykato.ui.notifications

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.luismakeapp.sindykato.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private var totalTime: Int = 0
    private lateinit var mp: MediaPlayer


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })*/

        //---------------------------------------------------------------------------------

        //Objetos
        val volumeBar: SeekBar = root.findViewById(R.id.volumeBar)
        val positionBar: SeekBar = root.findViewById(R.id.positionBar)


        mp = MediaPlayer.create(context, R.raw.sound)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration

        // Volume Bar
        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        var volumeNum = progress / 100.0f
                        mp.setVolume(volumeNum, volumeNum)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Position Bar
        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )
        val elapsedTimeLabel: TextView = root.findViewById(R.id.elapsedTimeLabel)
        val remainingTimeLabel: TextView = root.findViewById(R.id.remainingTimeLabel)
        val playBtn: Button = root.findViewById(R.id.playBtn)

        @SuppressLint("HandlerLeak")
        var handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                var currentPosition = msg.what

                // Update positionBar
                positionBar.progress = currentPosition

                // Update Labels
                var elapsedTime = createTimeLabel(currentPosition)
                elapsedTimeLabel.text = elapsedTime

                var remainingTime = createTimeLabel(totalTime - currentPosition)
                remainingTimeLabel.text = "-$remainingTime"
            }
        }

        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()



        playBtn.setOnClickListener {
            if (mp.isPlaying) {
                // Stop
                mp.pause()
                playBtn.setBackgroundResource(R.drawable.play)

            } else {
                // Start
                mp.start()
                playBtn.setBackgroundResource(R.drawable.stop)
            }
        }

        //---------------------------------------------------------------------------------

        return root
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

}