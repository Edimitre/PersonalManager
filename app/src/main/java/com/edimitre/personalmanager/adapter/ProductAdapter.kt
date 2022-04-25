package com.edimitre.personalmanager.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.model.Product


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    private var allProducts: List<Product>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_view, parent, false)

        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {


        val product: Product? = allProducts?.getOrNull(position)

        if (product != null) {
            holder.productName.text = product.name
            holder.productPrice.text = product.price.toString()
            holder.productItemBtn.setOnClickListener(View.OnClickListener {
                Log.e("Adapter Message : ", "clicked product :" + product.name)
            })

        }

    }

    override fun getItemCount(): Int {
        return allProducts?.size ?: 0
    }

    class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productName: TextView = itemView.findViewById(R.id.product_name_text)
        val productPrice: TextView = itemView.findViewById(R.id.product_price_text)
        val productItemBtn: Button = itemView.findViewById(R.id.productItemBtn)


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