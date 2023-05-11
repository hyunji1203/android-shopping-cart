package woowacourse.shopping.view.cart

import woowacourse.shopping.model.ProductModel

interface CartContract {
    interface View {
        fun showProducts(cartProducts: List<ProductModel>)
        fun notifyRemoveItem(position: Int)
        fun showOtherPage(size: Int)
    }

    interface Presenter {
        fun fetchProducts()
        fun removeProduct(id: Int)
        fun fetchNextPage()
        fun fetchUndoPage()
    }
}
