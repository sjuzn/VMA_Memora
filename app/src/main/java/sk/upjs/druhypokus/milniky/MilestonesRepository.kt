package sk.upjs.druhypokus.milniky


import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import sk.upjs.druhypokus.milniky.MilestonesDao
import sk.upjs.druhypokus.milniky.Milestone
import java.util.UUID
import java.util.stream.Collectors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MilestonesRepository(private val milestonesDao: MilestonesDao) {

    val milestones = milestonesDao.getAllMilestones()
    lateinit var milestone : Milestone
    lateinit var milestoneList: ArrayList<Milestone>

    suspend fun getByUuid(uuid: UUID) {
        milestone = milestonesDao.getByUuid(uuid).first()
    }

    suspend fun insert(milestone : Milestone){
        milestonesDao.insert(milestone)
    }

    suspend fun delete(milestone: Milestone) {
        milestonesDao.delete(milestone)
    }

    suspend fun update(milestone: Milestone) {
        milestonesDao.update(milestone)
    }

}