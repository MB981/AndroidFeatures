package com.sms.sendsms

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sms.sendsms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private var message: String = ""
    private var number: String = ""
    private val REQUEST_SEND_SMS_PERMISSION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        onClick()
    }


    private fun onClick() {
        mBinding.btnSend.setOnClickListener {
            message = mBinding.etMessage.text.toString()
            number = mBinding.etPhoneNumber.text.toString()
            // Check if the app has the SEND_SMS permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, send the SMS
                sendSMS(message, number)
            } else {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.SEND_SMS),
                    REQUEST_SEND_SMS_PERMISSION
                )
            }
        }
    }

    private fun sendSMS(userMessage: String, userNumber: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(userNumber, null, userMessage, null, null)
            Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Failed to send message: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_SEND_SMS_PERMISSION) {
            // Check if the permission is granted.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS
                sendSMS(message, number)
            } else {
                // Permission denied, inform the user.
                Toast.makeText(
                    applicationContext,
                    "Permission denied. Cannot send SMS.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



}