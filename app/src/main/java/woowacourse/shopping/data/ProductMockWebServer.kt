package woowacourse.shopping.data

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.json.JSONObject
import woowacourse.shopping.data.db.firstPageProducts
import woowacourse.shopping.domain.Price
import woowacourse.shopping.domain.Product
import java.io.IOException
import java.lang.Exception

class ProductMockWebServer {
    private lateinit var mockWebServer: MockWebServer
    private val okHttpClient = OkHttpClient()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/products" -> {
                    MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(200)
                        .setBody(firstPageProducts)
                }

//                "/products?page=2" -> {
//                    MockResponse()
//                        .setHeader("Content-Type", "application/json")
//                        .setResponseCode(200)
//                        .setBody(secondPageProducts)
//                }
//
//                "/products?page=3" -> {
//                    MockResponse()
//                        .setHeader("Content-Type", "application/json")
//                        .setResponseCode(200)
//                        .setBody(thirdPageProducts)
//                }

                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    fun request(
        // page: () -> Unit = 1,
        onSuccess: (List<Product>) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        synchronized(this) {
            mockWebServer = MockWebServer()
            mockWebServer.dispatcher = dispatcher
            mockWebServer.url("/")

            val baseUrl = String.format("http://localhost:%s", mockWebServer.port)
            val url = "$baseUrl/products"
            val request = Request.Builder().url(url).build()

            okHttpClient.newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        onFailure(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        response.close()

                        val result = responseBody?.let {
                            parseJsonToProducts(responseBody)
                        } ?: emptyList()

                        onSuccess(result)
                    }
                },
            )
        }
    }

    private fun parseJsonToProducts(json: String): List<Product> {
        val products = mutableListOf<Product>()
        val jsonProducts = JSONObject(json).getJSONObject("data").getJSONArray("products")

        for (i in 0 until jsonProducts.length()) {
            val jsonProduct = jsonProducts.getJSONObject(i)

            val id = jsonProduct.getInt("id")
            val name = jsonProduct.getString("name")
            val imageUrl = jsonProduct.getString("imageUrl")
            val price = Price(jsonProduct.getInt("price"))

            val product = Product(id, name, imageUrl, price)
            products.add(product)
        }

        return products
    }
}