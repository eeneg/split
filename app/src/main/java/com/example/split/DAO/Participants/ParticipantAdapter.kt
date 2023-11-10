package com.example.split.DAO.Participant
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.split.R

class ParticipantAdapter : ListAdapter<Participant, ParticipantAdapter.ParticipantViewHolder>(ParticipantComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.bib, current.name, current.eventName)
    }

    class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val writeBibList: TextView = itemView.findViewById(R.id.writeBibList)
        private val participantName: TextView = itemView.findViewById(R.id.participantName)

        @SuppressLint("SetTextI18n")
        fun bind(bib: String, name: String, eventName: String){
            writeBibList.text = bib
            participantName.text = name
        }

        companion object{
            fun create(parent: ViewGroup): ParticipantViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_bib_list, parent, false)
                return ParticipantViewHolder(view)
            }
        }
    }

    class ParticipantComparator : DiffUtil.ItemCallback<Participant>() {
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Participant, newItem: Participant): Boolean {
            return oldItem === newItem
        }

        override fun areItemsTheSame(oldItem: Participant, newItem: Participant): Boolean {
            return oldItem.bib == newItem.bib
        }
    }
}
