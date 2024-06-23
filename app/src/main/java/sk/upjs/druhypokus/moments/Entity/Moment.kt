package sk.upjs.druhypokus.moments.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "moments")
data class Moment(
    var nazov: String?,
    var text: String?,
    var fotka: String?,
    var datum: String?,
    var lat: Float?,
    var lng: Float?,
    @PrimaryKey (autoGenerate = true)
    var idMoment: Int
) : Serializable
