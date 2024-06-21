package sk.upjs.druhypokus.main

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import sk.upjs.druhypokus.bucketList.BList
import sk.upjs.druhypokus.bucketList.BListDao
import sk.upjs.druhypokus.milniky.MilestonesDao
import sk.upjs.druhypokus.milniky.Milestone
import sk.upjs.druhypokus.moments.Moment
import sk.upjs.druhypokus.moments.MomentTagCrossRef
import sk.upjs.druhypokus.moments.Moment_Tag_Dao
import sk.upjs.druhypokus.moments.Tag
import java.util.UUID

@Database(
    entities = [Milestone::class, BList::class, Moment::class, Tag::class, MomentTagCrossRef::class], version = 1, exportSchema = false
)
@TypeConverters(UuidConverter::class)
abstract class MemoraDatabase : RoomDatabase() {

    abstract fun milestonesDao(): MilestonesDao
    abstract fun bListDao(): BListDao
    abstract fun moment_Tag_Dao(): Moment_Tag_Dao

    companion object {
        @Volatile
        private var INSTANCE: MemoraDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MemoraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoraDatabase::class.java,
                    "memora_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class UuidConverter {
    @TypeConverter
    fun uuidToString(uuid: UUID) = uuid.toString()

    @TypeConverter
    fun stringToUuid(string: String) = UUID.fromString(string)
}