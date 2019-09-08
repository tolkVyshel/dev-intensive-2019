package ru.skillbranch.devintensive.ui.main


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    //private lateinit var chatAdapter: RecyclerView.Adapter<*>?
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initViews()
        initViewModel()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
    private fun initToolbar() {
        //  val toolbar = findViewById<Toolbar>(R.layout.activity_main)
        setActionBar(toolbar)

    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it)  })
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {
            Snackbar.make(rv_chat_list, "Click on ${it.title}and he is ${if (it.isOnline) "online" else "not online"}", Snackbar.LENGTH_LONG).show()
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            val chatId = it.id
            viewModel.addToArchive(chatId)
            Snackbar
                .make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_LONG)
                .setAction("отмена"){viewModel.restoreFromArchive(chatId)}
                .show()

        }
        val touchHelper = ItemTouchHelper(touchCallback)


        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list){
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
            addItemDecoration(divider)
        }

        fab.setOnClickListener{
       val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }


}
