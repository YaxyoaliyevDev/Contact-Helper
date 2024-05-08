package com.example.contacthelper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.contacthelper.adapters.RvAdapter
import com.example.contacthelper.databinding.ActivityMainBinding
import com.example.contacthelper.helper.MyButton
import com.example.contacthelper.listener.MyButtonClickListener
import com.example.contacthelper.models.Contacts
import com.example.kotlinswipebuttonrcl.Helper.MySwipeHelper
import com.github.florent37.runtimepermission.kotlin.askPermission

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var adapter : RvAdapter
    lateinit var contactList:ArrayList<Contacts>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        contactList = ArrayList()

        object : MySwipeHelper(this, binding.rv, 120){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {

                buffer.add(
                    MyButton(this@MainActivity,
                        "Sms",
                        30,
                        R.drawable.ic_sms,
                        Color.parseColor("#FFDD2371"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                val intent = Intent(this@MainActivity, SMSActivity::class.java)
                                intent.putExtra("key", contactList[position])
                                startActivity(intent)
                            }
                        })
                )
                buffer.add(
                    MyButton(this@MainActivity,
                        "Call",
                        30,
                        R.drawable.ic_call,
                        Color.parseColor("#FFF8CA2A"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                Toast.makeText(this@MainActivity, "Update id $position", Toast.LENGTH_SHORT).show()
                                telefonQilish(position)
                            }
                        })
                )
            }

        }
        readContact()
    }

    private fun telefonQilish(position:Int) {
        askPermission(Manifest.permission.CALL_PHONE){
            val phoneNumber = contactList[position].number
            val intent = Intent(Intent(Intent.ACTION_CALL))
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    }
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
            if(e.hasForeverDenied()) {
                e.goToSettings()
            }
        }
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun readContact(){
        contactList = ArrayList()
        askPermission(Manifest.permission.READ_CONTACTS){
            val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null)
            while (contacts!!.moveToNext()){
                val contact = Contacts(
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )
                contactList.add(contact)
            }
            contacts.close()
            binding.rv.adapter = RvAdapter(contactList)
        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    }
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
            if(e.hasForeverDenied()) {
                e.goToSettings()
            }
        }


    }

}