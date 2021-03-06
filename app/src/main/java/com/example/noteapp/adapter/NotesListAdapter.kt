package com.example.noteapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.localdatabase.model.UsersNoteEntity
import com.example.noteapp.BR
import com.example.noteapp.R
import com.example.noteapp.databinding.ItemRowAdapterBinding

class NotesListAdapter
    (
    val context: Context,
    private val itemList: List<UsersNoteEntity>,
    private val adapterClickEvent: AdapterClickEventInterface
) : RecyclerView.Adapter<NotesListAdapter.Myhandler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myhandler {
        val itemRowAdapterBinding =
            ItemRowAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return Myhandler(itemRowAdapterBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Myhandler, position: Int) {

        holder.bind(itemList.get(position))
        holder.iv_cancel.setOnClickListener {
            adapterClickEvent.deleteNote(itemList.get(position))
        }
        holder.cv_parent.setOnClickListener {
            adapterClickEvent.viewDetailorEdit(position)
        }
    }

    class Myhandler(val binding: ItemRowAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val iv_cancel = binding.root.findViewById<AppCompatImageView>(R.id.iv_cancel)
        val cv_parent = binding.root.findViewById<CardView>(R.id.cv_parent)

        fun bind(entity: UsersNoteEntity) {
            binding.adapterItemRow = entity
            binding.setVariable(BR.adapterItemRow, entity)
            binding.executePendingBindings()
        }
    }

    interface AdapterClickEventInterface {
        fun deleteNote(entity: UsersNoteEntity)
        fun viewDetailorEdit(position: Int)
    }
}