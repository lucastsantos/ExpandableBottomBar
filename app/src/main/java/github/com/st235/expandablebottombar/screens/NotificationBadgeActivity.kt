package github.com.st235.expandablebottombar.screens

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import github.com.st235.expandablebottombar.R
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarNotification
import kotlinx.android.synthetic.main.activity_xml_declared.*

class NotificationBadgeActivity : AppCompatActivity() {

    private lateinit var bottomBar: ExpandableBottomBar

    private val notificationsLookup = mutableMapOf<Int, ExpandableBottomBarNotification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_badge)
        setSupportActionBar(toolbar)

        val color: View = findViewById(R.id.color)
        bottomBar = findViewById(R.id.expandable_bottom_bar)

        color.setBackgroundColor(ColorUtils.setAlphaComponent(Color.GRAY, 60))

        bottomBar.onItemSelectedListener = { v, i ->
            val anim = ViewAnimationUtils.createCircularReveal(color,
                    bottomBar.x.toInt() + v.x.toInt() + v.width / 2,
                    bottomBar.y.toInt() + v.y.toInt() + v.height / 2, 0F,
                    findViewById<View>(android.R.id.content).height.toFloat())
            color.setBackgroundColor(ColorUtils.setAlphaComponent(i.activeColor, 60))
            anim.duration = 420
            anim.start()
        }

        bottomBar.onItemReselectedListener = { v, i ->
            if (!notificationsLookup.containsKey(i.itemId)) {
                notificationsLookup[i.itemId] = bottomBar.getNotificationFor(i.itemId)
            }
            val notification = notificationsLookup.getValue(i.itemId)

            if (v.tag == null) {
                notification.show()
                v.tag = 0
            } else {
                val counter = (v.tag as Int) + 1
                notification.show(counter.toString())
                v.tag = counter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                for (notification in notificationsLookup.values) {
                    notification.clear()
                }
            }
        }

        return true
    }
}
