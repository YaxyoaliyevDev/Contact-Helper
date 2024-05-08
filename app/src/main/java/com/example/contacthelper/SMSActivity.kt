package com.example.contacthelper

import android.Manifest
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.contacthelper.databinding.ActivitySmsactivityBinding
import com.example.contacthelper.models.Contacts
import com.github.florent37.runtimepermission.kotlin.askPermission

class SMSActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySmsactivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val contact = intent.getSerializableExtra("key") as Contacts
        binding.txtNameSms.text = contact.name
        binding.txtNumberSms.text = contact.number

        binding.btnSend.setOnClickListener {
            askPermission(Manifest.permission.SEND_SMS){
                //all permissions already granted or just granted

                val matn = binding.edtMatn.text.toString()
                var obj = SmsManager.getDefault()
                obj.sendTextMessage(contact.number,
                    null,  matn,
                    null, null)
                Toast.makeText(this, "Send message", Toast.LENGTH_SHORT).show()

            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(this)
                        .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if(e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }

        binding.cardOrtga.setOnClickListener {
            finish()
        }
    }
}