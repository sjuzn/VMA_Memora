package sk.upjs.druhypokus.moments

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sk.upjs.druhypokus.moments.Entity.Moment
import sk.upjs.druhypokus.moments.Entity.MomentTagCrossRef
import sk.upjs.druhypokus.moments.Entity.MomentWithTags
import sk.upjs.druhypokus.moments.Entity.Tag
import sk.upjs.druhypokus.moments.Entity.TagWithMoments

@Dao
interface Moment_Tag_Dao {

    @Query("SELECT * FROM moments")
    fun getAllMoments(): Flow<List<Moment>>

    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<Tag>>

    @Query("SELECT * FROM MomentTagCrossRef WHERE nazovTag = :nazovTagu")
    fun getTagSMomentami(nazovTagu: String): Flow<List<TagWithMoments>>

    @Query("SELECT * FROM MomentTagCrossRef WHERE idMoment = :idMoment")
    fun getMomentSTagmi(idMoment: Int): Flow<List<MomentWithTags>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoment(moment: Moment) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMomentTagCrossRef(crossRef: MomentTagCrossRef)

    @Delete
    suspend fun deleteMoment(moment: Moment)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("DELETE FROM momenttagcrossref WHERE idMoment = :momentId")
    suspend fun deleteMomentTagCrossRefs(momentId: Int)

    @Query("DELETE FROM momenttagcrossref WHERE nazovTag = :tagId")
    suspend fun deleteMomentTagCrossRefs(tagId: String)

    @Query("DELETE FROM momenttagcrossref WHERE nazovTag = :tagId AND idMoment = :momentId")
    suspend fun deleteMomentTagCrossRefs(tagId: String, momentId: Int)

}
