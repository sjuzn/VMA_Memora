package sk.upjs.druhypokus.main

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import sk.upjs.druhypokus.R
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.milniky.MilestonesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NewAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Create a ViewModel instance
        val application = context.applicationContext as MemoraApplication
        val milestonesRepository = application.milestonesRepository
        val factory = MilestonesViewModel.MilestoneViewModelFactory(milestonesRepository)
        val milestonesViewModel = ViewModelProvider(
            ViewModelStore(),
            factory
        ).get(MilestonesViewModel::class.java)

        // Fetch the latest milestones and update the widget
        milestonesViewModel.milestones.observeForever { milestones ->
            // There may be multiple widgets active, so update all of them
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(
                    context,
                    appWidgetManager,
                    appWidgetId,
                    najdiNajblizsiDatum(milestones)
                )
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun najdiNajblizsiDatum(milestoneList: List<Milestone>?): Milestone? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()

        var minDays: Int = Int.MAX_VALUE
        var savedMilestone: Milestone? = null

        if (milestoneList != null) {
            for (m in milestoneList) {
                val objDate = LocalDate.parse(m.datum, formatter)

                // Zmeníme rok na aktuálny rok, aby sme porovnávali len deň a mesiac
                val thisYearObjDate = objDate.withYear(currentDate.year)

                // Ak je dátum už prešiel tento rok, porovnávame s budúcim rokom
                val nextDate =
                    if (thisYearObjDate.isBefore(currentDate) || thisYearObjDate.isEqual(currentDate)) {
                        thisYearObjDate.plusYears(1)
                    } else {
                        thisYearObjDate
                    }

                val daysBetween = ChronoUnit.DAYS.between(currentDate, nextDate).toInt()

                if (daysBetween < minDays) {
                    minDays = daysBetween
                    savedMilestone = m
                }
            }
        }
        return savedMilestone
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    milestoneData: Milestone?
) {
    if (milestoneData != null) {
        Toast.makeText(context,milestoneData.typ, Toast.LENGTH_LONG).show()
    }
    val widgetText: String = if (milestoneData == null) {
        (context.getString(R.string.appwidget_text)) + (" - ")
    } else {
        calculateDaysUntilAnniversary(milestoneData.datum, context)
    }

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun calculateDaysUntilAnniversary(date: String?, context: Context): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val givenDate = LocalDate.parse(date, formatter)
    val now = LocalDate.now()

    // Get the month and day of the given date but for the current year
    var nextAnniversary = LocalDate.of(now.year, givenDate.month, givenDate.dayOfMonth)

    // If the anniversary has already happened this year, set it to next year
    if (nextAnniversary.isBefore(now) || nextAnniversary.isEqual(now)) {
        nextAnniversary = nextAnniversary.plusYears(1)
    }

    val daysUntilNextAnniversary = ChronoUnit.DAYS.between(now, nextAnniversary)

    return context.getString(R.string.appwidget_text) + " " + daysUntilNextAnniversary.toString() + " " + context.getString(R.string.days)
}