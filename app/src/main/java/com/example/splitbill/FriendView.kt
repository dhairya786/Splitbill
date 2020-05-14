package com.example.splitbill

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.abs
import kotlin.math.absoluteValue

class FriendView : AppCompatActivity() {
    var chats: ArrayList<String> = ArrayList()
    var lv: ListView? = null
    var twos: ArrayList<String> = ArrayList()
    var mAuth = FirebaseAuth.getInstance()
    var balance: Int? = 0
    var tv: TextView? = null
    var spinner: Spinner? = null
    var lol : String = "lent"
    var ib : ImageButton? = null
    var ui : String? = null
    var et: EditText? = null
    var ema: String? = null
    var lols: String? = null

    fun last(view : View)
    {
        if(lol.equals("lent"))
            lols = "borrow"
        else
            lols= "lent"

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("friends").child(ui!!).child("expenses").push().child(lol).setValue(et?.text.toString())
        FirebaseDatabase.getInstance().getReference().child("emailToUid").child(ema!!).addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                FirebaseDatabase.getInstance().getReference().child("friendToUid").child(p0.value.toString()).child(mAuth?.currentUser?.email.toString().replace('@',' ').replace('.',' ')).addListenerForSingleValueEvent(object : ValueEventListener
                {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(p1: DataSnapshot) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(p0.value.toString()).child("friends").child(p1.value.toString()).child("expenses").push().child(lols!!).setValue(et?.text.toString())
                    }
                })
            }

        })
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_view)
        lv = findViewById(R.id.listview)
        tv = findViewById(R.id.textView3)
        ib = findViewById(R.id.imageButton3)
        spinner = findViewById(R.id.spinner)
        et = findViewById(R.id.editText5)
        chats.add("No history")
        twos.add("lent")
        twos.add("borrow")
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, twos)
        spinner?.adapter = adapter1
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, chats)
        lv?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!)
            .child("friends").addChildEventListener(object :
                ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {


                    var name = p0?.child("name")?.value.toString()

                    var original = intent.getStringExtra("name")
                    if (original.equals(name)) {
                        var email =
                            p0?.child("email")?.value.toString().replace('@', ' ').replace('.', ' ')
                        ema = email
                        FirebaseDatabase.getInstance().getReference().child("friendToUid")
                            .child(mAuth.currentUser?.uid!!).child(email)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    var uid = p0.value.toString()
                                    ui = uid
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                        .child(mAuth.currentUser?.uid!!).child("friends").child(uid)
                                        .child("expenses")
                                        .addChildEventListener(object : ChildEventListener {
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

                                                if (p0?.child("lent").value.toString().equals("null")) {
                                                    balance =
                                                        balance!! - p0?.child("borrow").value.toString()
                                                            .toInt()
                                                    if (chats[0].equals("No history")) {
                                                        chats.clear()
                                                    }
                                                    chats.add("You borrowed :" + p0?.child("borrow").value.toString() + " (Balance " + balance.toString() + ")")


                                                } else {
                                                    balance =
                                                        balance!! + p0?.child("lent").value.toString()
                                                            .toInt()
                                                    if (chats[0].equals("No history")) {
                                                        chats.clear()
                                                    }
                                                    chats.add("You lent " + p0?.child("lent").value.toString() + " (Balance " + balance!!.toString() + ")")


                                                }
                                                if (balance!! > 0) {
                                                    tv?.setText(original + " owes you " + balance.toString())
                                                } else if (balance!! < 0) {
                                                    tv?.setText(
                                                        "You owe " + original + " " + balance.toString()
                                                            .substringAfter('-')
                                                    )
                                                } else {
                                                    tv?.setText("Settled up")
                                                }

                                                adapter.notifyDataSetChanged()


                                            }

                                            override fun onChildRemoved(p0: DataSnapshot) {
                                                TODO("Not yet implemented")
                                            }

                                        })
                                }

                            })
                    }


                }

                override fun onChildRemoved(p0: DataSnapshot) {

                }

            })

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 1)
                    lol = "borrow"
                else if(position ==0)
                    lol = "lent"

                Log.i("lol",lol)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}