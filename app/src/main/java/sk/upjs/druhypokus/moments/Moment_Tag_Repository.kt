package sk.upjs.druhypokus.moments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import sk.upjs.druhypokus.moments.Entity.Moment
import sk.upjs.druhypokus.moments.Entity.MomentTagCrossRef
import sk.upjs.druhypokus.moments.Entity.MomentWithTags
import sk.upjs.druhypokus.moments.Entity.Tag
import sk.upjs.druhypokus.moments.Entity.TagWithMoments

class MomentTagRepository(private val momentTagDao: Moment_Tag_Dao) {

    val allMoments = momentTagDao.getAllMoments()
    val allTags = momentTagDao.getAllTags()


    fun getTagSMomentami(tag: Tag): Flow<List<TagWithMoments>> {
        return momentTagDao.getTagSMomentami(tag.nazovTag.trim())
    }

    fun getMomentSTagmi(moment: Moment): Flow<List<MomentWithTags>>  {
        return momentTagDao.getMomentSTagmi(moment.idMoment)
    }

    suspend fun insertMoment(moment: Moment) : Long {
        return momentTagDao.insertMoment(moment)
    }

    suspend fun insertTag(tag: Tag) {
        momentTagDao.insertTag(tag)
    }

    suspend fun insertMomentTagCrossRef(crossRef: MomentTagCrossRef) {
        momentTagDao.insertMomentTagCrossRef(crossRef)
    }

    suspend fun deleteMoment(moment: Moment) = withContext(Dispatchers.IO) {
        // Delete cross-references first
        momentTagDao.deleteMomentTagCrossRefs(moment.idMoment)
        // Delete the moment
        momentTagDao.deleteMoment(moment)
    }

    suspend fun deleteTag(tag: Tag) = withContext(Dispatchers.IO) {
        // Delete cross-references first
        tag.nazovTag?.let { momentTagDao.deleteMomentTagCrossRefs(it) }
        // Delete the tag
        momentTagDao.deleteTag(tag)
    }

    suspend fun deleteMomentTagCrossRefs(tag: Tag, moment: Moment) = withContext(Dispatchers.IO){
        tag.nazovTag?.let { momentTagDao.deleteMomentTagCrossRefs(it, moment.idMoment) }
    }

    /*
    suspend fun deleteMomentTagCrossRefs(tag: Tag) = withContext(Dispatchers.IO) {
        tag.nazovTag?.let { momentTagDao.deleteMomentTagCrossRefs(it) }
    }

    suspend fun deleteMomentTagCrossRefs(moment: Moment) = withContext(Dispatchers.IO) {
        moment.idMoment.let { momentTagDao.deleteMomentTagCrossRefs(it) }
    }*/

}
