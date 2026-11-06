package com.furrymeow.launcher

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.furrymeow.launcher.databinding.ActivityMainBinding

/**
 * Главный экран furryMeowOS.
 * Показывает сетку всех запускаемых приложений.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appGrid.layoutManager = GridLayoutManager(this, 4)
        binding.appGrid.adapter = AppAdapter(loadApps()) { app ->
            launchApp(app)
        }
    }

    /** Нажатие «Назад» не закрывает лаунчер: мы и так на домашнем экране. */
    override fun onBackPressed() {
        // Ничего не делаем.
    }

    /** Собирает список приложений, которые можно запустить. */
    private fun loadApps(): List<AppItem> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

        return pm.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != packageName }
            .map { info ->
                AppItem(
                    label = info.loadLabel(pm).toString(),
                    packageName = info.activityInfo.packageName,
                    icon = info.loadIcon(pm),
                    isSystem = (info.activityInfo.applicationInfo.flags and
                            ApplicationInfo.FLAG_SYSTEM) != 0
                )
            }
            .sortedBy { it.label.lowercase() }
    }

    /** Запускает приложение по имени пакета. */
    private fun launchApp(app: AppItem) {
        val intent = packageManager.getLaunchIntentForPackage(app.packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
