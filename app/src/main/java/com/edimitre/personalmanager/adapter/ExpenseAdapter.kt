package com.edimitre.personalmanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.model.Expense
import com.edimitre.personalmanager.data.utils.TimeUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ExpenseAdapter(val expenseClickListener: OnExpenseClickListener) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder>() {

    private var allExpenses: List<Expense>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item_view, parent, false)

        return ExpenseHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {

        val expense: Expense? = allExpenses?.getOrNull(position)

        if (expense != null) {

            holder.bind(expense, expenseClickListener)

        }

    }

    override fun getItemCount(): Int {
        return allExpenses?.size ?: 0
    }

    class ExpenseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val descriptionName: TextView = itemView.findViewById(R.id.description_name_text);
        private val valueText: TextView = itemView.findViewById(R.id.expense_value_text);
        private val dateText: TextView = itemView.findViewById(R.id.expense_date_text);
        private val hourText: TextView = itemView.findViewById(R.id.expense_hour_text);

        fun bind(expense: Expense, expenseClickListener: OnExpenseClickListener) {

            @SuppressLint("SimpleDateFormat")
            val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")

            @SuppressLint("SimpleDateFormat")
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")


            val date = Date(
                TimeUtils().getTimeInMilliSeconds(
                    expense.year,
                    expense.month,
                    expense.date,
                    expense.hour,
                    expense.minute
                )
            )

            val expDate = dateFormat.format(date)

            val expTime = timeFormat.format(date)
            descriptionName.text = expense.description.name
            valueText.text = expense.spentValue.toString()
            dateText.text = expDate
            hourText.text = expTime

            itemView.setOnClickListener {
                expenseClickListener.onExpenseClicked(expense)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun putExpenses(expenseList: List<Expense>) {
        allExpenses = expenseList
        notifyDataSetChanged()
    }

    fun getExpenseByPos(pos: Int): Expense? {
        return this.allExpenses?.get(pos)
    }

    interface OnExpenseClickListener {
        fun onExpenseClicked(expense: Expense)
    }
}
