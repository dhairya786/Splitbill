package com.example.splitbill

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class ViewActivity : AppCompatActivity() {
    var lv : ListView? = null
    var friends : ArrayList<String> = ArrayList()


    val mAuth = FirebaseAuth.getInstance()
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menuu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==R.id.logout)
        {
            mAuth.signOut()

           val intent = Intent(this@ViewActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        else if(item?.itemId==R.id.add)
        {
            val intent = Intent(this,AddFriend::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        lv = findViewById(R.id.listView)
        val fab : FloatingActionButton = findViewById(R.id.floatingActionButton6)
        friends.add("No Friends in the list")
        val k = mAuth.currentUser?.email.toString()
        val m = k.substringBefore('@')

        val adapter = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,friends)
        lv?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("friends").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(friends[0].equals("No Friends in the list"))
                {
                    friends.clear()
                }
                friends.add(p0?.child("name").value.toString())
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = Intent(this@ViewActivity,AddFriend::class.java)
                startActivity(intent)
            }
        })
        lv?.setOnItemClickListener()
        { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->


            val intent = Intent(this@ViewActivity,FriendView::class.java)
            intent.putExtra("name",friends[i])
            startActivity(intent)
        }
    }
}
