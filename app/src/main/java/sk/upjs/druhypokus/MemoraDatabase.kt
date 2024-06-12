package sk.upjs.druhypokus

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import sk.upjs.druhypokus.dao.MilestonesDao
import sk.upjs.druhypokus.entity.Milestone
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.UUID

@Database(entities = [Milestone::class], version = 1, exportSchema = false)
@TypeConverters(UuidConverter::class)
abstract class MemoraDatabase : RoomDatabase() {

    abstract fun  milestonesDao() : MilestonesDao

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