package sk.upjs.druhypokus

import android.app.Application
import sk.upjs.druhypokus.milniky.MilestonesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MemoraApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { MemoraDatabase.getDatabase(this, applicationScope) }
    val milestonesRepository by lazy { MilestonesRepository(database.milestonesDao()) }

}