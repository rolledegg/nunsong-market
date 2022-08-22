package smu.app.nunsong_market.dto

data class Message(
    var message: String? = null,
    var senderId: String? = null,
    var time: String? = null

) {
    fun isDiffrentDate(new: Message): Boolean {
        if (this.time!!.slice(0..9).equals(new.time!!.slice(0..9))) return false
        else return true
    }
}