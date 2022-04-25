package com.edimitre.personalmanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.model.Product


class ProductInExpenseAdapter : RecyclerView.Adapter<ProductInExpenseAdapter.ProductHolder>() {

    private var allProducts: List<Product>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_in_expense_item_view, parent, false)

        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {


        val product: Product? = allProducts?.getOrNull(position)

        if (product != null) {
            holder.productName.text = product.name
            holder.productPrice.text = product.price.toString()
            holder.qty.text = product.quantity.toString()

            val valueOfSelectedProduct = product.quantity * product.price
            holder.prdValue.text = valueOfSelectedProduct.toString()

        }

    }

    override fun getItemCount(): Int {
        return allProducts?.size ?: 0
    }

    class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productName: TextView = itemView.findViewById(R.id.eproduct_name_text)
        val productPrice: TextView = itemView.findViewById(R.id.eproduct_price_text)
        val qty: TextView = itemView.findViewById(R.id.e_product_quantity_text)
        val prdValue: TextView = itemView.findViewById(R.id.spent_value_text)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun putProducts(productList: List<Product>) {
        allProducts = productList
        notifyDataSetChanged()
    }

    fun getProductByPos(pos: Int): Product? {
        return this.allProducts?.get(pos)
    }
}