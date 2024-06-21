package sk.upjs.druhypokus.moments

// zdroj https://www.youtube.com/watch?v=AHn5JQVlJJM

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithMoments(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "nazovTag",
        entityColumn = "idMoment",
        associateBy = Junction(MomentTagCrossRef::class)
    )
    val moments : List<Moment>
)

