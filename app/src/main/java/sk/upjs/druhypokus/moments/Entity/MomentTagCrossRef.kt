package sk.upjs.druhypokus.moments.Entity

import androidx.room.Entity

@Entity(primaryKeys = ["idMoment", "nazovTag"])
data class MomentTagCrossRef (
    var idMoment: Int,
    var nazovTag: String
)