package com.example.chattraductor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chattraductor.data.repository.local.PopulateLocalDataBase
import com.example.chattraductor.data.repository.local.PopulateLocalDataBaseFactory
import com.example.chattraductor.data.repository.local.chat.RoomChatDataSource
import com.example.chattraductor.data.repository.local.message.RoomMessageDataSource
import com.example.chattraductor.data.repository.local.user.RoomUserDataSource
import com.example.chattraductor.data.repository.remote.RemoteMessageDataSource
import com.example.chattraductor.data.repository.remote.RemoteUserDataSource
import com.example.chattraductor.databinding.ActivityMainBinding
import com.example.chattraductor.ui.message.MessageService
import com.example.chattraductor.utils.MyApp
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val loggedUser = MyApp.userPreferences.getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        if (loggedUser != null) {
            binding.appBarMain.fab.visibility = View.VISIBLE
        } else {
            binding.appBarMain.fab.visibility = View.GONE
        }
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            (application as MyApp).logoutUser()
            val navController = this.findNavController(R.id.nav_host_fragment_content_main)
            navController?.navigate(R.id.nav_chat)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_chat, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}