package com.example.tic_tac_toe_online

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class InvitesRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<Invites> = ArrayList()
    private lateinit var deleteListener: OnDeleteClickListnerer
    private lateinit var playListener: OnPlayClickListnerer
    interface OnDeleteClickListnerer{
        fun onDeleteClick(position: Int)
    }

    fun setOnDeleteClickListnerer(listerer: OnDeleteClickListnerer) {
        deleteListener = listerer
    }

    interface OnPlayClickListnerer{
        fun onPlayClick(position: Int)
    }

    fun setOnPlayClickListnerer(listerer: OnPlayClickListnerer) {
        playListener = listerer
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InvitesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.invites_list, parent, false), deleteListener, playListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is InvitesViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun submitList(invitesList: List<Invites>){
       items = invitesList as ArrayList<Invites>
    }

    class InvitesViewHolder(itemView: View, private val deleteListener: OnDeleteClickListnerer, private val playListener: OnPlayClickListnerer): RecyclerView.ViewHolder(itemView){
        private val profilePic: ImageView = itemView.findViewById(R.id.invited_player_profile)
        private val displayName: TextView = itemView.findViewById(R.id.invited_player_displayName)
        private val playButton: Button = itemView.findViewById(R.id.invited_player_play)
        private val delete: ImageButton = itemView.findViewById(R.id.delete_player_invite)
        fun bind(invites: Invites){
            Picasso.get().load(invites.joinerPhotoURL).placeholder(R.mipmap.ic_launcher).into(profilePic)
            displayName.text = invites.joinerName
            playButton.setOnClickListener {
                playListener.onPlayClick(absoluteAdapterPosition)
            }
            delete.setOnClickListener {
                deleteListener.onDeleteClick(absoluteAdapterPosition)
            }
        }
    }


}