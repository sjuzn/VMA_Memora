package sk.upjs.druhypokus.milniky

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID

class MilestonesRepository(private val milestonesDao: MilestonesDao) {

    val milestones = milestonesDao.getAllMilestones()
    lateinit var milestone : Milestone

    suspend fun getByUuid(uuid: UUID) {
        milestone = milestonesDao.getByUuid(uuid).first()
    }

    suspend fun insert(milestone : Milestone){
        milestonesDao.insert(milestone)
    }

    suspend fun replaceAll(milestones: List<Milestone>) = withContext(Dispatchers.IO) {
        deleteAll()
        milestonesDao.insert(milestones)
    }

    suspend fun delete(milestone: Milestone) {
        milestonesDao.delete(milestone)
    }

    suspend fun deleteAll() {
        milestonesDao.deleteAll()
    }

    suspend fun update(milestone: Milestone) {
        milestonesDao.update(milestone)
    }

}