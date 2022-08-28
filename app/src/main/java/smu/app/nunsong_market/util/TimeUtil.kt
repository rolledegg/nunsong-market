package smu.app.nunsong_market.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    public fun getTime(): String {
        var mNow = System.currentTimeMillis()
        var mDate = Date(mNow)
        val mFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return mFormat.format(mDate)
    }

    fun formateTime(timeOfMsg: String): String {
        // 이미 오전 오후로 들어가있는 값들 처리
        var t = timeOfMsg.slice(0..1)
        if (t.equals("PM") || t.equals("AM")||t.equals("오전")||t.equals("오후")||t.equals(". ")){
            return timeOfMsg
        }

        var time: Int = t.toInt()
        var minute = timeOfMsg.slice(3..4)
        when (time) {
            0 -> return "오후 0" + time.toString() + ":" + minute
            in 1..9 -> return "오전 0" + time.toString() + ":" + minute
            10, 11 -> return "오전 " + time.toString() + ":" + minute
            12 -> return "오후 " + time.toString() + ":" + minute
            in 13..21 -> return "오후 0" + (time - 12).toString() + ":" + minute
            else -> return "오후 " + (time - 12).toString() + ":" + minute
        }
    }


}