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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletestzemogamiguelpuentes.R
import com.example.mobiletestzemogamiguelpuentes.model.db.DataPost
import com.example.mobiletestzemogamiguelpuentes.repositories.PostsRepository
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestAllPosts
import com.example.mobiletestzemogamiguelpuentes.tools.Constants
import com.example.mobiletestzemogamiguelpuentes.view.activities.PostDetailActivity
import com.example.mobiletestzemogamiguelpuentes.view.adapters.AdapterPosts
import com.example.mobiletestzemogamiguelpuentes.viewmodel.PostsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private lateinit var postsViewModel: PostsViewModel

    // Components
    private lateinit var recyclerFavoritePosts: RecyclerView
    private lateinit var adapterPosts: AdapterPosts

    // Reload
    private lateinit var reload: LinearLayout
    private lateinit var deleteAllFavorites: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_favorites, container, false)

        // View model initialization
        postsViewModel =
            ViewModelProvider(this@FavoritesFragment)[PostsViewModel::class.java]

        // Recycler de songs
        recyclerFavoritePosts = rootView.findViewById(R.id.recycler_favorite_posts)
        recyclerFavoritePosts.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerFavoritePosts.setHasFixedSize(true)

        // Reload
        reload = rootView.findViewById(R.id.click_reload_favorite_posts)
        deleteAllFavorites = rootView.findViewById(R.id.delete_all_favorite_posts)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePostsAdapter()

        postsViewModel.favoritePostsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                adapterPosts.setData(it)
                adapterPosts.notifyDataSetChanged()
            } else if (it.isNotEmpty()) {
                adapterPosts.setData(it)
                adapterPosts.notifyDataSetChanged()
            }
        }

        reload.setOnClickListener {
            getFavoriteCachePosts()
        }
        deleteAllFavorites.setOnClickListener {
            deleteAllFavoritePosts()
        }
    }

    override fun onResume() {
        super.onResume()
        getFavoriteCachePosts()
    }
    private fun getFavoriteCachePosts() {
        postsViewModel.getFavoriteCachePosts()
    }

    private fun initializePostsAdapter() {
        adapterPosts = AdapterPosts(
            requireContext(),
            ArrayList<DataPost>(), object : AdapterPosts.OnClickDetail {
                override fun onClickDetail(item: DataPost) {
                    val intent = Intent(context, PostDetailActivity::class.java).apply {
                        putExtra(Constants.POST_ID, item.postId)
                        putExtra(Constants.USER_ID, item.userId)
                    }
                    startActivity(intent)
                }

                override fun onClickDelete(item: DataPost) {
                    postsViewModel.deletePost(item.postId)
                }
            })
        recyclerFavoritePosts.adapter = adapterPosts
    }

    private fun deleteAllFavoritePosts() {
        postsViewModel.deleteAllFavoritePosts()
    }
}