package sk.upjs.druhypokus.repository


import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.first
import sk.upjs.druhypokus.dao.MilestonesDao
import sk.upjs.druhypokus.entity.Milestone
import java.util.UUID

class MilestonesRepository(private val milestonesDao: MilestonesDao) {

    val milestones = milestonesDao.getAllMilestones()
    lateinit var milestone : Milestone

    suspend fun getByUuid(uuid: UUID) {
        milestone = milestonesDao.getByUuid(uuid).first()
    }

    suspend fun delete(milestone: Milestone) {
        milestonesDao.delete(milestone)
    }

}