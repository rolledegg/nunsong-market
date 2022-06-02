package smu.app.nunsong_market.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.model.Product
import smu.app.nunsong_market.R
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var adapter: ProductAdapter

    //데이터 배열
    var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"HomeFragment - onCreate() called")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com")
//            .baseUrl("https://fakestoreapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        productApi.getProducts()
            .enqueue(object: Callback<List<Product>>{
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    if(response.isSuccessful.not()){
                        //예외처리
                        Log.d(TAG,"NOT SUCCESS")
                        return
                    }

                    response.body()?.let{
                        it.forEach{product->
                            Log.d(TAG, product.toString())
                            productList.add(product)
                        }
                        adapter.submitList(productList)
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }

            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG,"HomeFragment - onAttach() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"HomeFragment - onCreateView() called")
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        initBookRecyclerView()

        return binding.root // return layout
    }

    fun initBookRecyclerView(){
        adapter = ProductAdapter(clickListener= {
            Log.d(TAG,"recyclerview clicked")
        })

        adapter.submitList(this.productList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = adapter
    }

    companion object {
        const val TAG: String = "HomeFragment"

        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }
}