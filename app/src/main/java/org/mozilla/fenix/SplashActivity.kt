package org.mozilla.fenix

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import com.news.data.MyData
import com.news.data.utils.FileUtil
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import mozilla.components.feature.tab.collections.TabCollectionStorage
import org.mozilla.fenix.ext.settings
import org.mozilla.gecko.GeckoThread.launch
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
        val threadPolicy = StrictMode.ThreadPolicy.Builder()
            .permitDiskReads()
            .permitDiskWrites() // If you also want to ignore DiskWrites, Set this line too.
            .permitNetwork()
            .build();
        StrictMode.setThreadPolicy(threadPolicy);
        GlobalScope.launch(Dispatchers.IO) {
            StrictMode.setThreadPolicy(threadPolicy);
            //Start directly from the IO thread here
            if (!FileUtil.databaseFileExists(applicationContext, getString(R.string.dbname))) {
                MyData().syncTopSites(applicationContext)
                applicationContext.settings().defaultTopSitesAdded = true
                FileUtil.attachDbFromDropBox(
                    applicationContext,
                    getString(R.string.collection_key),
                    getString(R.string.collection_dbname)
                )
            }
            withContext(Dispatchers.Main) {
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