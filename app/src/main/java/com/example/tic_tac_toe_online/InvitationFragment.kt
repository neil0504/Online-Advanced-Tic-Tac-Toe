 package com.example.tic_tac_toe_online

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

var itemSelected = false
lateinit var invitationFragment_InvitesAdapter: InvitationFragment_Invites_RecyclerViewAdapter
lateinit var invitationfragment_LobbyRecyclerviewAdapter: InvitationFragment_Lobby_RecyclerView_Adapter
lateinit var data_Lobby: ArrayList<InvitationFragment_Lobby>

lateinit var joinerID: String
lateinit var joinerName: String
lateinit var joinerEmail: String
lateinit var joinerPhotoURL: String
lateinit var joinerToken: String

//class InvitationFragment(sp: SharedPreferences?): Fragment(R.layout.fragment_invitation){
class InvitationFragment(cursor: Cursor?): Fragment(R.layout.fragment_invitation){
    private var cur: Cursor? = null
    private lateinit var recycler_view2: RecyclerView
    private lateinit var recycler_view1: RecyclerView

    private lateinit var thiscontxt: Context
    private lateinit var playBtn: Button


    private var idd: String? = null
    private var name: String? = null
    private var email: String? = null
    private var photo_URL: String? = null
    private var token: String? = null
    private var user_id: String? = null
    private lateinit var data_invites: ArrayList<InvitationFragment_Invites>

//    lateinit var acceptedMethod: AcceptedMethod
//    lateinit var obj: OnlineConnectionFragment
    init {
        cur = cursor
//        Toast.makeText(cc, "Fragment init method", Toast.LENGTH_SHORT).show()
//        acceptedMethod.accepted()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playBtn = view.findViewById(R.id.playButton)
        recycler_view2 = view.findViewById(R.id.rv2_invites)
        recycler_view1 = view.findViewById(R.id.rv1_lobby)
        thiscontxt = view.context
        Toast.makeText(thiscontxt, "Fragment Created", Toast.LENGTH_SHORT).show()
        initRecyclerView1()
        addDataset1()
        initRecyclerView2()
        addDataset2()

        playBtn.setOnClickListener {
            if(itemSelected){
                val set = ArrayList<String>()
                set.add(account!!.id)
                set.add(account!!.displayName!!)
                set.add(account!!.email!!)
                set.add(account!!.photoUrl!!.toString())
                set.add(myToken!!)
                FirebaseDatabase.getInstance().reference.child("Creator_cred").child(code!!).child(idd!!).setValue(set)
                FirebaseDatabase.getInstance().reference.child("Play with").child(code!!).child(idd!!).setValue(idd!!)
                joinerID = idd!!
                joinerName = name!!
                joinerEmail = email!!
                joinerPhotoURL = photo_URL!!.toString()
                joinerToken = token!!
                Log.d("ABCDEFGH", "Value of joinerID = $joinerID, Value of joinerName = $joinerName")
//                obj.accepted()
                acc()
            }else{
                Toast.makeText(thiscontxt, "Select a player to play with", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun initRecyclerView1() {
        recycler_view1.apply {
            layoutManager = LinearLayoutManager(thiscontxt)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            invitationfragment_LobbyRecyclerviewAdapter = InvitationFragment_Lobby_RecyclerView_Adapter()
            adapter = invitationfragment_LobbyRecyclerviewAdapter
//            invitationfragment_LobbyRecyclerviewAdapter.setOnClickListenerr(InvitationFragment_Lobby_RecyclerView_Adapter.OnRowSelect{

//            })
            invitationfragment_LobbyRecyclerviewAdapter.setOnItemListenerr(object : InvitationFragment_Lobby_RecyclerView_Adapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    Toast.makeText(thiscontxt, "Row clicked", Toast.LENGTH_SHORT).show()
                    itemSelected = true
                    val obj = data_Lobby[position]
                    idd = obj.id
                    name = obj.name
                    email = obj.email
                    photo_URL = obj.photo_URL
                    token = obj.token

                }

            })

        }
    }
    private fun initRecyclerView2(){
        recycler_view2.apply {
            layoutManager = LinearLayoutManager(thiscontxt)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            invitationFragment_InvitesAdapter = InvitationFragment_Invites_RecyclerViewAdapter()
            adapter = invitationFragment_InvitesAdapter
            invitationFragment_InvitesAdapter.setOnDeleteClickListnerer(object : InvitationFragment_Invites_RecyclerViewAdapter.OnDeleteClickListnerer{
                override fun onDeleteClick(position: Int) {
                    val obj = data_invites[position]
                    val ID = obj.id
                    Log.d("DATA_RETRIVAL", "Delete Button Clicked")
                    val db = MyDatabaseHelper(thiscontxt)
                    db.deleteRow(ID)
                    data_invites.removeAt(position)
                    invitationFragment_InvitesAdapter.notifyItemRemoved(position)
                }
            })
            invitationFragment_InvitesAdapter.setOnInviteClickListnerer(object : InvitationFragment_Invites_RecyclerViewAdapter.OnInviteClickListnerer{
                override fun onInviteClick(position: Int) {
                    Toast.makeText(thiscontxt, "Invite Button Clicked", Toast.LENGTH_SHORT).show()
                    val obj = data_invites[position]
                    val ID = obj.id
                    val token = obj.userToken
                    Log.d("TATA", "Invitee Token = $token")
                    // TODO: Do account not null check
                    val sendNotif = FcmNotificationsSender(token, "Invitation", "${account?.displayName} invites you to 2 Player Tic-Tac-Toe", thiscontxt, this@InvitationFragment.requireActivity())
                    sendNotif.SendNotifications()
                }

            })
        }
    }
    private fun addDataset2(){
        data_invites = DataSource_for_rv2_Invitation_Fragment.createDataSet(cur)
        invitationFragment_InvitesAdapter.submitList(data_invites)
    }
    private fun addDataset1(){
        data_Lobby = DataSource_for_rv1_Invitation_Fragment.createDataSet()
        invitationfragment_LobbyRecyclerviewAdapter.submitList(data_Lobby)
    }
    fun acc() {
        startActivity(Intent(cc, OnlinePlay::class.java))
    }





}