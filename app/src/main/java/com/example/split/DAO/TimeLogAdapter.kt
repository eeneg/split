package com.example.split.DAO
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.split.R
import java.sql.Time

class TimeLogAdapter : ListAdapter<TimeLog, TimeLogAdapter.TimeLogViewHolder>(BibComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLogViewHolder {
        return TimeLogViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TimeLogViewHolder, position: Int) {
        val current = getItem(position)
        current.bib?.let { current.time?.let { it1 -> holder.bind(it, it1) } }
    }

    class TimeLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val bibItemView: TextView = itemView.findViewById(R.id.bib)
        private val timeItemView: TextView = itemView.findViewById(R.id.time)

        fun bind(bib: String, time: Time){
            bibItemView.text = bib
            timeItemView.text = time.toString()
        }

        companion object{
            fun create(parent: ViewGroup): TimeLogViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list, parent, false)
                return TimeLogViewHolder(view)
            }
        }
    }

    class BibComparator : DiffUtil.ItemCallback<TimeLog>() {
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TimeLog, newItem: TimeLog): Boolean {
            return oldItem === newItem
        }

        override fun areItemsTheSame(oldItem: TimeLog, newItem: TimeLog): Boolean {
            return oldItem.bib == newItem.bib
        }
    }
}
