package sk.upjs.druhypokus.milniky

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date
import java.util.UUID

@Entity(tableName = "milestones")
data class Milestone(
    var typ: String,
    var datum: String,
    var fotka: String,
    var zucastneni: String
) : Serializable{

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()
}
