package sk.upjs.druhypokus.milniky

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import sk.upjs.druhypokus.milniky.Milestone
import java.util.UUID
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestonesDao {

    @Query("SELECT * FROM milestones")
    fun getAllMilestones(): Flow<List<Milestone>>

    @Query("SELECT * FROM milestones WHERE uuid=:uuid")
    fun getByUuid(uuid: UUID): Flow<Milestone>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(milestone: Milestone)

    @Delete
    suspend fun delete(milestone: Milestone)

    @Query("DELETE FROM milestones WHERE uuid=:uuid")
    suspend fun delete(uuid: UUID)

    @Update
    suspend fun update(milestone: Milestone)
}