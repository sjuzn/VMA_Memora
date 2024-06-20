package sk.upjs.druhypokus.bucketList

class BListRepository(private val bListDao: BListDao) {

    val bListAll = bListDao.getAllBList()

    suspend fun insert(bList: BList){
        bListDao.insert(bList)
    }

    suspend fun update(bList: BList) {
        bListDao.update(bList)
    }

    suspend fun delete(bList: BList) {
        bListDao.delete(bList)
    }
}