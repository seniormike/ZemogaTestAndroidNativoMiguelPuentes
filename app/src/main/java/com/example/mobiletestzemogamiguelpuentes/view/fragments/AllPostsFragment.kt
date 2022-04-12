package com.example.mobiletestzemogamiguelpuentes.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletestzemogamiguelpuentes.R
import com.example.mobiletestzemogamiguelpuentes.model.db.DataPost
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestAllPosts
import com.example.mobiletestzemogamiguelpuentes.tools.Constants.POST_ID
import com.example.mobiletestzemogamiguelpuentes.tools.Constants.USER_ID
import com.example.mobiletestzemogamiguelpuentes.tools.DialogFactory
import com.example.mobiletestzemogamiguelpuentes.view.activities.PostDetailActivity
import com.example.mobiletestzemogamiguelpuentes.view.adapters.AdapterPosts
import com.example.mobiletestzemogamiguelpuentes.viewmodel.PostsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AllPostsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllPostsFragment : Fragment() {

    private lateinit var postsViewModel: PostsViewModel

    // Components
    private lateinit var recyclerAllPosts: RecyclerView
    private lateinit var adapterPosts: AdapterPosts

    // Reload
    private lateinit var reload: LinearLayout
    private lateinit var deleteAll: LinearLayout
    private lateinit var progress: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_all_posts, container, false)
        // Inflate the layout for this fragment
        postsViewModel =
            ViewModelProvider(this@AllPostsFragment)[PostsViewModel::class.java]

        context?.let {
            progress = DialogFactory().setProgress(it)
            progress.show()
        }

        // Recycler de songs
        recyclerAllPosts = rootView.findViewById(R.id.recycler_all_posts)
        recyclerAllPosts.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerAllPosts.setHasFixedSize(true)

        // Reload
        reload = rootView.findViewById(R.id.click_reload_all_posts)
        deleteAll = rootView.findViewById(R.id.delete_all_posts)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePostsAdapter()

        postsViewModel.postsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                adapterPosts.setData(it)
                adapterPosts.notifyDataSetChanged()
            } else if (it.isNotEmpty()) {
                adapterPosts.setData(it)
                adapterPosts.notifyDataSetChanged()
            }
            progress.dismiss()
        }

        postsViewModel.dialogMsg.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                progress.dismiss()
                val dialog = DialogFactory().getDialog(it, requireContext())
                dialog.show()
            }
        }

        reload.setOnClickListener {
            progress.show()
            requestFetchStorePosts()
        }
        deleteAll.setOnClickListener {
            deleteAllPosts()
        }
        requestFetchStorePosts()
    }

    private fun initializePostsAdapter() {
        adapterPosts = AdapterPosts(
            requireContext(),
            ArrayList<DataPost>(), object : AdapterPosts.OnClickDetail {
                override fun onClickDetail(item: DataPost) {
                    val intent = Intent(context, PostDetailActivity::class.java).apply {
                        putExtra(POST_ID, item.postId)
                        putExtra(USER_ID, item.userId)
                    }
                    startActivity(intent)
                }

                override fun onClickDelete(item: DataPost) {
                    postsViewModel.deletePost(item.postId)
                }

            })
        recyclerAllPosts.adapter = adapterPosts
    }

    override fun onResume() {
        super.onResume()
        getCachePosts()
    }

    private fun requestFetchStorePosts() {
        val request = RequestAllPosts()
        postsViewModel.requestFetchStorePosts(request)
    }

    private fun deleteAllPosts() {
        postsViewModel.deleteAllPosts()
    }

    private fun getCachePosts() {
        postsViewModel.getCachePosts()
    }

}