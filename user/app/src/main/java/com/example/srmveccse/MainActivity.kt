package com.example.srmveccse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.srmveccse.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    val auth = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        if(auth!=null && intent!= null){

            createUI()
        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
            this.finish()
        }

    }

    override fun onResume() {
        super.onResume()
        if(auth!=null&& intent!= null){

            createUI()
        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
            this.finish()

        }
    }

    fun createUI(){

        val list = auth?.providerData
        var providerData:String = ""
        list?.let {
            for(provider in list){
                providerData = providerData+ " " +provider.providerId
            }
        }

        auth?.let {
            txtName.text = auth.displayName
            txtEmail.text = auth.email
            txtPhone.text = auth.phoneNumber
            txtProvider.text = providerData
            Glide
                .with(this)
                .load(auth.photoUrl)
                .fitCenter()
                .placeholder(R.drawable.login_profilepic)
                .into(profile_image)

        }

        val button = findViewById<Button>(R.id.btnnav)
        button.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }




}
