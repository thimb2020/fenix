package org.mozilla.fenix

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import com.smarttech.datalibrary.MyData
import com.smarttech.datalibrary.utils.FileUtil
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import mozilla.components.feature.tab.collections.TabCollectionStorage
import org.mozilla.fenix.ext.settings
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), CoroutineScope {
    val activityScope = CoroutineScope(Dispatchers.Main)

    //https://www.programmersought.com/article/37335883860/
    //job is used to control the coroutine
    private lateinit var job: Job

    //Inheriting CoroutineScope must initialize the coroutineContext variable
    // This is the standard way of writing, + is actually the plus method, which means job in the front, used to control the coroutine, and Dispatchers in the back, specifying the thread to start
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize job in onCreate
        job = Job()
        //hideSystemUI();
        setContentView(R.layout.activity_splash)
/*        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyDeath()
            .build());*/


        activityScope.launch(Dispatchers.IO) {
            //Start directly from the IO thread here
            launch(Dispatchers.IO) {
                if (!FileUtil.databaseFileExists(applicationContext, "sites.db")) {
                    MyData().syncTopSites(applicationContext)
                    applicationContext.settings().defaultTopSitesAdded = true
                    FileUtil.attachDbFromDropBox(
                        applicationContext,
                        "k6bzgww8mzvtclo",
                        "tab_collections"
                    )
                }
                var intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }


}