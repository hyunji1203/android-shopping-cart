package woowacourse.shopping.view.cart

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.CartProductModel

class CartAdapter(
    private val products: List<CartProductModel>,
    private val onItemClick: OnItemClick,
    private val isExistUndo: Boolean,
    private val isExistNext: Boolean,
    private val count: String,
) : RecyclerView.Adapter<CartItemViewHolder>() {

    interface OnItemClick {
        fun onRemoveClick(id: Int)
        fun onNextClick()
        fun onUndoClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        Log.d("test", "onCreateViewHolder 진입")
        return CartItemViewHolder.of(parent, CartViewType.values()[viewType], onItemClick)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == products.size) {
            CartViewType.PAGINATION_ITEM.ordinal
        } else {
            CartViewType.CART_PRODUCT_ITEM.ordinal
        }
    }

    override fun getItemCount(): Int = products.size + 1

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        when (holder) {
            is CartItemViewHolder.CartProductViewHolder -> holder.bind(
                products[position],
                onItemClick,
            )

            is CartItemViewHolder.CartPaginationViewHolder -> holder.bind(
                count,
                isExistUndo,
                isExistNext,
            )
        }
    }

    fun updateCartItems(size: Int) {
        notifyItemRangeChanged(0, size + 1)
    }

    fun removeCartItems(position: Int) {
        notifyItemRemoved(position)
    }
}
