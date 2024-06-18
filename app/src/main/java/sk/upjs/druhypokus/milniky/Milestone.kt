package sk.upjs.druhypokus.milniky

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "milestones")
data class Milestone(
    var typ: String?,
    var datum: String?,
    var fotka: String?,
    var zucastneni: String?
) : Serializable, Parcelable{

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {  uuid = UUID.fromString(parcel.readString())  }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(typ)
        parcel.writeString(datum)
        parcel.writeString(fotka)
        parcel.writeString(zucastneni)
        parcel.writeString(uuid.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Milestone> {
        override fun createFromParcel(parcel: Parcel): Milestone {
            return Milestone(parcel)
        }

        override fun newArray(size: Int): Array<Milestone?> {
            return arrayOfNulls(size)
        }
    }
}
