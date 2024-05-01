package com.sms.sendsms

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sms.sendsms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private var message: String = ""
    private var number: String = ""
    private val REQUEST_SEND_SMS_PERMISSION = 123
    private val REQUEST_CALL_PHONE_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.etMessage.visibility = View.VISIBLE
        mBinding.etPhoneNumber.visibility = View.VISIBLE
        mBinding.btnSend.visibility = View.VISIBLE

        mBinding.etAddress.visibility = View.GONE
        mBinding.etSubject.visibility = View.GONE
        mBinding.etEmailBody.visibility = View.GONE

        onClick()
    }


    private fun onClick() {
        mBinding.btnSMSScreen.setOnClickListener {
            mBinding.etMessage.visibility = View.VISIBLE
            mBinding.etPhoneNumber.visibility = View.VISIBLE
            mBinding.btnSend.visibility = View.VISIBLE

            mBinding.etAddress.visibility = View.GONE
            mBinding.etSubject.visibility = View.GONE
            mBinding.etEmailBody.visibility = View.GONE
            mBinding.etCallNumber.visibility = View.GONE
            mBinding.btnCall.visibility = View.GONE
            mBinding.btnEmail.visibility = View.GONE

        }
        mBinding.btnEmailScreen.setOnClickListener {

            mBinding.etAddress.visibility = View.VISIBLE
            mBinding.etSubject.visibility = View.VISIBLE
            mBinding.etEmailBody.visibility = View.VISIBLE
            mBinding.btnEmail.visibility = View.VISIBLE


            mBinding.etMessage.visibility = View.GONE
            mBinding.etPhoneNumber.visibility = View.GONE
            mBinding.btnSend.visibility = View.GONE
            mBinding.etCallNumber.visibility = View.GONE
            mBinding.btnCall.visibility = View.GONE

        }

        mBinding.btnCallScreen.setOnClickListener {
            mBinding.etCallNumber.visibility = View.VISIBLE
            mBinding.btnCall.visibility = View.VISIBLE


            mBinding.etAddress.visibility = View.GONE
            mBinding.etSubject.visibility = View.GONE
            mBinding.etEmailBody.visibility = View.GONE
            mBinding.btnEmail.visibility = View.GONE


            mBinding.etMessage.visibility = View.GONE
            mBinding.etPhoneNumber.visibility = View.GONE
            mBinding.btnSend.visibility = View.GONE


        }

        mBinding.btnSpeech.setOnClickListener {
            val intent = Intent(this@MainActivity, SpeechToTextActivity::class.java)
            startActivity(intent)
        }





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


        mBinding.btnEmail.setOnClickListener {
            val userAddress = mBinding.etAddress.text.toString()
            val userSubject = mBinding.etSubject.text.toString()
            val userEmail = mBinding.etEmailBody.text.toString()
            sendEmail(userAddress, userSubject, userEmail)
        }


        mBinding.btnCall.setOnClickListener {
            val phoneNumber = mBinding.etCallNumber.text.toString()
            // Check if the app has the CALL_PHONE permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, start the call
                startCall(phoneNumber)
            } else {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PHONE_PERMISSION
                )
            }
        }
    }


    private fun startCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendEmail(userAddress: String, userSubject: String, userEmail: String) {
        val emailAddresses = arrayOf(userAddress)
        val emailIntent = Intent(Intent.ACTION_SENDTO)
// The only email app open
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddresses)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, userSubject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, userEmail)

        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, "Choose an app"))
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
        when (requestCode) {
            REQUEST_SEND_SMS_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted for SMS, send the SMS
                    val phoneNumber = mBinding.etPhoneNumber.text.toString()
                    val message = mBinding.etMessage.text.toString()
                    sendSMS(message, phoneNumber)
                } else {
                    // Permission denied for SMS, inform the user.
                    Toast.makeText(
                        applicationContext,
                        "Permission denied for SMS. Cannot send SMS.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            REQUEST_CALL_PHONE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted for call, start the call
                    val phoneNumber = mBinding.etPhoneNumber.text.toString()
                    startCall(phoneNumber)
                } else {
                    // Permission denied for call, inform the user.
                    Toast.makeText(
                        applicationContext,
                        "Permission denied for call. Cannot make a call.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}