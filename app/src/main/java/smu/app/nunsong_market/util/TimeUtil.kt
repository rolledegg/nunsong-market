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

    /***
    1:1  -> 오전 01:01
    13:1 -> 오후 01:01
    ***/
    fun formateTime(timeOfMsg: String): String {
        // 이미 오전 오후로 들어가있는 값들 처리
        var t = timeOfMsg.split(":")[0]
        if (t.equals("PM") || t.equals("AM") || t.equals("오전") || t.equals("오후") || t.equals(". ")) {
            return timeOfMsg
        }

        var time: Int = t.toInt()
        var minute = timeOfMsg.split(":")[1]
        if (minute.length == 1) {
            minute = "0" + minute
        }
        when (time) {
            0 -> return "오후 0" + time.toString() + ":" + minute
            in 1..9 -> return "오전 0" + time.toString() + ":" + minute
            10, 11 -> return "오전 " + time.toString() + ":" + minute
            12 -> return "오후 " + time.toString() + ":" + minute
            in 13..21 -> return "오후 0" + (time - 12).toString() + ":" + minute
            else -> return "오후 " + (time - 12).toString() + ":" + minute
        }
    }


    /***
    2022-09-03T03:13 -> 9월 3일 오전 03:13
     ***/
    fun formateDateTime(timeOfProm: String):String{
        var date= timeOfProm.split("T")[0]
        var dateArr= date.split("-")
        var time= formateTime(timeOfProm.split("T")[1])
        return dateArr[1].toInt().toString() +"월 " + dateArr[2].toInt().toString() +"일 " +time
    }


}