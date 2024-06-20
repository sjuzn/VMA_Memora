package sk.upjs.druhypokus.bucketList

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "blist")
data class BList(
    var task: String?,
    var hotovo: Boolean
): Serializable, Parcelable {

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readBoolean(),
    ) {  uuid = UUID.fromString(parcel.readString())  }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(task)
        parcel.writeBoolean(hotovo)
        parcel.writeString(uuid.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BList> {
        override fun createFromParcel(parcel: Parcel): BList {
            return BList(parcel)
        }

        override fun newArray(size: Int): Array<BList?> {
            return arrayOfNulls(size)
        }
    }
}