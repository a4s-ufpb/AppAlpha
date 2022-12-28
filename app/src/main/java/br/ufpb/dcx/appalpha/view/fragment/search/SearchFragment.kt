package br.ufpb.dcx.appalpha.view.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufpb.dcx.appalpha.R
import br.ufpb.dcx.appalpha.control.util.SearchEngineClientBing


class SearchFragment(query : String,tag :Int)  : Fragment(){
    private var query:String = query
    private lateinit var rv : RecyclerView
    private lateinit var laymanager : GridLayoutManager
    private lateinit var searchEngine : SearchEngineClientBing
    private var TAG :Int = tag

    private  var  resultado :ArrayList<HashMap<String, Any>> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_nav_search,container,false)

        searchEngine = SearchEngineClientBing(context)
        rv = root.findViewById(R.id.search_rv)

        laymanager = GridLayoutManager(context,2,RecyclerView.VERTICAL,false)

        try {
            activity?.runOnUiThread {

                activity?.findViewById<View>(R.id.loadingframeLayout)?.setVisibility(View.VISIBLE)

                searchEngine.search(query, object :
                    SearchEngineClientBing.SearchEngineClientBingCompletionHandler {
                    override fun success(resultado: List<HashMap<String?, Any?>?>) {
                        activity?.runOnUiThread {
                            activity?.findViewById<View>(R.id.loadingframeLayout)?.setVisibility(View.GONE)

                            fillRecycleView(resultado)
                        }
                    }

                    override fun failed(reason: String) {
                        activity?.runOnUiThread {
                            activity?.findViewById<View>(R.id.loadingframeLayout)?.setVisibility(View.GONE)

                            Toast.makeText(activity, reason, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
            }
        }catch (e : Exception) {
            e.printStackTrace();
        }

        return root
    }

    private fun fillRecycleView(resultado: List<HashMap<String?, Any?>?>){
        rv.layoutManager = laymanager
        rv.adapter = context?.let { SearchListAdapter(resultado, it,this,TAG) }
    }

}