package sk.upjs.druhypokus.moments.Entity

// zdroj https://www.youtube.com/watch?v=AHn5JQVlJJM

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import sk.upjs.druhypokus.moments.Entity.Moment
import sk.upjs.druhypokus.moments.Entity.MomentTagCrossRef
import sk.upjs.druhypokus.moments.Entity.Tag

data class TagWithMoments(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "nazovTag",
        entityColumn = "idMoment",
        associateBy = Junction(MomentTagCrossRef::class)
    )
    val moments : List<Moment>
)

