package com.example.splitbill

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.substring
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    var username : EditText? = null
    var password : EditText? = null
    var mAuth = FirebaseAuth.getInstance()
    var loginmode = true
    var tv : TextView? = null
    var bt : Button? = null
    var name : EditText? = null
    fun login(view : View)
    {
        if(loginmode)
        {
            mAuth.signInWithEmailAndPassword(username?.text.toString(), password?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this,ViewActivity::class.java)
                        startActivity(intent)
                    } else {
                        val m = task.exception.toString()
                        val k = m.substringAfter(':')
                        Toast.makeText(this,k,Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(username?.text.toString(), password?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid!!).child("email").setValue(username?.text.toString())
                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid!!).child("name").setValue(name?.text.toString())
                        FirebaseDatabase.getInstance().getReference().child("emailToUid").child(username?.text.toString().replace('@',' ').replace('.',' ')).setValue(task.result?.user?.uid)
                        val intent = Intent(this,ViewActivity::class.java)
                        startActivity(intent)
                    } else {
                        val m = task.exception.toString()
                        val k = m.substringAfter(':')
                        Toast.makeText(this,k,Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    fun changemode(view : View)
    {
        if(loginmode)
        {
            tv?.setText("Or Login")
            loginmode=false
            bt?.setText("SIGN UP")

        }
        else
        {
            tv?.setText("Or Sign Up")
            loginmode=true
            bt?.setText("LOGIN")
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        tv = findViewById(R.id.textView)
        bt = findViewById(R.id.button)
        name = findViewById(R.id.name)
        if(mAuth.currentUser!=null)
        {
            val intent = Intent(this,ViewActivity::class.java)
            startActivity(intent)
        }

    }
}
