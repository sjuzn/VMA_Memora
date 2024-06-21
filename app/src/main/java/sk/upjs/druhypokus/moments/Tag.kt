package sk.upjs.druhypokus.moments

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = false)
    var nazovTag: String
) : Serializable
