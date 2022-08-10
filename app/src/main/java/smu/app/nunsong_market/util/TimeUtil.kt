package smu.app.nunsong_market.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    public  fun getTime(): String {
        var mNow = System.currentTimeMillis()
        var mDate = Date(mNow)
        val mFormat = SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss")

        return mFormat.format(mDate)
    }
}