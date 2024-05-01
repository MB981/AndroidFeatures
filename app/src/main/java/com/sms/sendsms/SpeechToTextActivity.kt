package com.sms.sendsms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sms.sendsms.databinding.ActivitySpeechToTextBinding
import java.util.Locale

class SpeechToTextActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySpeechToTextBinding

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_to_text)
        mBinding = ActivitySpeechToTextBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

//        Register here

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    val resultCode = result.resultCode
                    val data = result.data
                    if (resultCode == RESULT_OK && data != null) {
                        val speakResult: ArrayList<String> =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                                    as ArrayList<String>

                        mBinding.tvText.text = speakResult[0]
                    }
                })
        mBinding.imageButton.setOnClickListener {
            convertSpeech()

        }

    }

    fun convertSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, 1)
        activityResultLauncher.launch(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            val speakResult: ArrayList<String> =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        as ArrayList<String>

            mBinding.tvText.text = speakResult[0]

        }


    }
}