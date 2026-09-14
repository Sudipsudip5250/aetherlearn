package com.aetherlearn.app.data

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.aetherlearn.app.MainActivity

internal object GoalReminders {
    const val CHANNEL_ID = "aetherlearn-local-goals"
    const val EXTRA_GOAL_ID = "goal_id"
    const val EXTRA_GOAL_TITLE = "goal_title"

    fun canNotify(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 33) return true
        return context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    fun schedule(context: Context, goal: LearningGoal) {
        val remindAt = goal.remindAt ?: return
        if (remindAt <= System.currentTimeMillis()) return
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, remindAt, pending(context, goal))
    }

    fun cancel(context: Context, goalId: Long) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pending(context, LearningGoal(goalId, "", null, false, null, 0L)))
    }

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Local learning reminders",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Optional on-device reminders for goals you create. Nothing is sent to a server."
        }
        manager.createNotificationChannel(channel)
    }

    private fun pending(context: Context, goal: LearningGoal): PendingIntent {
        val intent = Intent(context, GoalReminderReceiver::class.java)
            .putExtra(EXTRA_GOAL_ID, goal.id)
            .putExtra(EXTRA_GOAL_TITLE, goal.title)
        return PendingIntent.getBroadcast(
            context,
            goal.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}

class GoalReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!GoalReminders.canNotify(context)) return
        GoalReminders.ensureChannel(context)
        val title = intent.getStringExtra(GoalReminders.EXTRA_GOAL_TITLE)?.ifBlank { null } ?: "Learning reminder"
        val open = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, GoalReminders.CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(context)
        }
        val notification = builder
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("AetherLearn")
            .setContentText(title)
            .setContentIntent(open)
            .setAutoCancel(true)
            .setLocalOnly(true)
            .build()
        context.getSystemService(NotificationManager::class.java)
            .notify(intent.getLongExtra(GoalReminders.EXTRA_GOAL_ID, 0L).toInt(), notification)
    }
}