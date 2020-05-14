package com.example.splitbill

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AddFriend : AppCompatActivity() {
    var friendname : EditText? = null
    var friendemail : EditText? = null
    var mAuth = FirebaseAuth.getInstance()
    val m = mAuth.currentUser?.email.toString()
    var emails : ArrayList<String> = ArrayList()
    var name : String? = null
    fun next(view : View)
    {
        val friendMap : Map<String,String> = mapOf("email" to friendemail?.text.toString() , "name" to  friendname?.text.toString())
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("friends").push().setValue(friendMap)
        if(emails.contains(friendemail?.text.toString()))
        {
            Toast.makeText(this,"user exists",Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().getReference().child("emailToUid").child(friendemail?.text.toString().replace('@',' ').replace('.',' ')).addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val uid = p0.value.toString()
                    Log.i("uid",uid)
                    val OurMap : Map<String,String> = mapOf("email" to mAuth.currentUser?.email.toString() , "name" to  name!!)
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("friends").push().setValue(OurMap)
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("friends").addChildEventListener(object : ChildEventListener
                    {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                            FirebaseDatabase.getInstance().getReference().child("friendToUid").child(uid).child(mAuth.currentUser?.email.toString().replace('@',' ').replace('.',' ')).setValue(p0.key)
                        }

                        override fun onChildRemoved(p0: DataSnapshot) {
                            TODO("Not yet implemented")
                        }

                    })

                }

            })

        }
        else
        {
            Toast.makeText(this,"user does not exist",Toast.LENGTH_SHORT).show()
        }
        val intent = Intent(this,ViewActivity::class.java)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        friendname = findViewById(R.id.editText)
        friendemail = findViewById(R.id.editText2)
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object : ChildEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val email = p0?.child("email")?.value.toString()
                emails.add(email)


            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("name").addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                name = p0.value.toString()
            }

        })
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("friends").addChildEventListener(object: ChildEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.i("lol","ok")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                FirebaseDatabase.getInstance().getReference().child("friendToUid").child(mAuth.currentUser?.uid!!).child(p0.child("email").value.toString().replace('@',' ').replace('.',' ')).setValue(p0.key)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }
}
