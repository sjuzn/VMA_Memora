package sk.upjs.druhypokus.moments

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "moments")
data class Moment(
    var nazov: String?,
    var text: String?,
    var fotka: String?,
    var datum: String?,
    var nCoor: Float?,
    var eCoor: Float?,
    @PrimaryKey (autoGenerate = true)
    var idMoment: Int
) : Serializable {}
