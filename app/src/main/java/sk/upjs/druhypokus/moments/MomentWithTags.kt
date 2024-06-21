package sk.upjs.druhypokus.moments

// zdroj https://www.youtube.com/watch?v=AHn5JQVlJJM

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MomentWithTags(
    @Embedded val moment: Moment,
    @Relation(
        parentColumn = "idMoment",
        entityColumn = "nazovTag",
        associateBy = Junction(MomentTagCrossRef::class)
    )
    val tags : List<Tag>
)
