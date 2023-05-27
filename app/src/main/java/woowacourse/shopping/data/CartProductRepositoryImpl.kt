package woowacourse.shopping.data

import woowacourse.shopping.data.db.CartDBHelper
import woowacourse.shopping.domain.CartProduct
import woowacourse.shopping.domain.CartProductRepository

class CartProductRepositoryImpl(
    private val dbHelper: CartDBHelper,
) : CartProductRepository {
    override fun findAll(): List<CartProduct> {
        return dbHelper.findAll()
    }

    private fun find(id: Int): CartProduct? {
        return dbHelper.findWhereById(id)
    }

    override fun findRange(mark: Int, rangeSize: Int): List<CartProduct> {
        return dbHelper.findRange(mark, rangeSize)
    }

    override fun findCheckedItem(): List<CartProduct> {
        return dbHelper.findByChecked()
    }

    override fun add(id: Int, count: Int, check: Boolean) {
        val cardProduct = find(id)
        if (cardProduct != null) {
            dbHelper.update(id, count + cardProduct.count)
            return
        }
        dbHelper.insert(id, count, check)
    }

    override fun updatePlus(id: Int) {
        dbHelper.plusCount(id)
    }

    override fun UpdateSub(id: Int) {
        dbHelper.subCount(id)
    }

    override fun updateCheckState(id: Int, checked: Boolean) {
        dbHelper.updateCheck(id, checked)
    }

    override fun remove(id: Int) {
        dbHelper.remove(id)
    }

    override fun isExistByMark(mark: Int): Boolean {
        return dbHelper.getSize(mark)
    }
}
