package sk.upjs.druhypokus.bucketList

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.util.UUID
import kotlinx.coroutines.flow.Flow

@Dao
interface BListDao {

    @Query("SELECT * FROM blist")
    fun getAllBList(): Flow<List<BList>>

    @Query("SELECT * FROM blist WHERE uuid=:uuid")
    fun getByUuid(uuid: UUID): Flow<BList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blist: BList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blist: List<BList>)

    @Delete
    suspend fun delete(blist: BList)
    @Query("DELETE FROM blist WHERE 1=1")
    suspend fun deleteAll()

    @Query("DELETE FROM blist WHERE uuid=:uuid")
    suspend fun delete(uuid: UUID)

    @Update
    suspend fun update(blist: BList)
}