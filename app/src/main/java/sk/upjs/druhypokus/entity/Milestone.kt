package sk.upjs.druhypokus.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date
import java.util.UUID

@Entity(tableName = "milestones")
data class Milestone(
    val typ: String,
    val datum: String,
    val nSuradnica: Float,
    val eSuradnica: Float,
    val fotka: String,
    val zucastneni: String
) : Serializable{

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()
}
