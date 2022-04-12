package com.example.mobiletestzemogamiguelpuentes.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletestzemogamiguelpuentes.R
import com.example.mobiletestzemogamiguelpuentes.model.Comment
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestPostComments
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestPostDetail
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestUserDetail
import com.example.mobiletestzemogamiguelpuentes.tools.Constants.POST_ID
import com.example.mobiletestzemogamiguelpuentes.tools.Constants.USER_ID
import com.example.mobiletestzemogamiguelpuentes.tools.DialogFactory
import com.example.mobiletestzemogamiguelpuentes.view.adapters.AdapterComments
import com.example.mobiletestzemogamiguelpuentes.viewmodel.CommentsViewModel
import com.example.mobiletestzemogamiguelpuentes.viewmodel.PostsViewModel
import com.example.mobiletestzemogamiguelpuentes.viewmodel.UserViewModel

class PostDetailActivity : AppCompatActivity() {

    // View models
    private lateinit var postsViewModel: PostsViewModel
    private lateinit var commentsViewModel: CommentsViewModel
    private lateinit var userViewModel: UserViewModel

    private var userId: Int = 0
    private var postId: Int = 0

    /**
     * Components
     */
    // User detail
    private lateinit var nameUser: TextView
    private lateinit var emailUser: TextView
    private lateinit var phoneUser: TextView
    private lateinit var webUser: TextView

    // Post detail
    private lateinit var titlePost: TextView
    private lateinit var descriptionPost: TextView

    // Comments
    private lateinit var recyclerComments: RecyclerView
    private lateinit var adapterComments: AdapterComments

    // Buttons
    private lateinit var back: ImageView
    private lateinit var favorite: ImageView

    // Progress
    private lateinit var progress: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        nameUser = findViewById(R.id.txt_name_user)
        emailUser = findViewById(R.id.txt_email_user)
        phoneUser = findViewById(R.id.txt_phone_user)
        webUser = findViewById(R.id.txt_website_user)
        titlePost = findViewById(R.id.post_title_detail)
        descriptionPost = findViewById(R.id.post_description_detail)
        back = findViewById(R.id.back)
        favorite = findViewById(R.id.favorite_icon)

        this.let {
            progress = DialogFactory().setProgress(it)
            progress.show()
        }

        // Comments recycler
        recyclerComments = findViewById(R.id.recycler_comments)
        recyclerComments.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerComments.setHasFixedSize(true)

        // Comments Adapter initialization
        initializeSongsAdapter()

        // Extra values initialization
        userId = intent.getIntExtra(USER_ID, 0)
        postId = intent.getIntExtra(POST_ID, 0)


        // View models initialization
        postsViewModel =
            ViewModelProvider(this)[PostsViewModel::class.java]
        commentsViewModel =
            ViewModelProvider(this)[CommentsViewModel::class.java]
        userViewModel =
            ViewModelProvider(this)[UserViewModel::class.java]


        // Observers

        userViewModel.user.observe(this) {
            nameUser.text = it.name
            emailUser.text = it.email
            phoneUser.text = it.phone
            webUser.text = it.website
        }
        postsViewModel.dialogMsg.observe(this) {
            if (it.isNotBlank()) {
                progress.dismiss()
                val dialog = DialogFactory().getDialog(it, this)
                dialog.show()
            }
        }

        postsViewModel.post.observe(this) {
            titlePost.text = it.title
            descriptionPost.text = it.body
            if (it.favorite) {
                favorite.setImageDrawable(getDrawable(R.drawable.ic_favorite))
            } else {
                favorite.setImageDrawable(getDrawable(R.drawable.ic_empty_star))
            }
        }

        commentsViewModel.commentsList.observe(this) {
            if (it.isEmpty()) {
                adapterComments.setData(it)
                adapterComments.notifyDataSetChanged()
            } else if (it.isNotEmpty()) {

                adapterComments.setData(it)
                adapterComments.notifyDataSetChanged()
            }
            progress.dismiss()
        }

        // Click listeners
        back.setOnClickListener {
            finish()
        }

        // Click favorite
        favorite.setOnClickListener {
            postsViewModel.switchPostFavorite(postId)
        }

    }

    override fun onResume() {
        super.onResume()
        requestGetPostDetail()
        requestGetUserDetail()
        requestGetPostComments()
    }

    private fun initializeSongsAdapter() {
        adapterComments = AdapterComments(
            this,
            ArrayList<Comment>()
        )
        recyclerComments.adapter = adapterComments
    }

    private fun requestGetPostDetail() {
        val request = RequestPostDetail()
        postsViewModel.requestPostDetail(postId, request)
    }

    private fun requestGetUserDetail() {
        val request = RequestUserDetail()
        userViewModel.requestUserDetail(userId, request)
    }

    private fun requestGetPostComments() {
        val request = RequestPostComments()
        commentsViewModel.requestPostComments(postId, request)
    }

}