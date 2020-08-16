package com.example.organize.helper

import java.text.SimpleDateFormat

class DateUtil {

    companion object{
        fun currentDate():String {
            var date:Long = System.currentTimeMillis()
            var dateFormat:SimpleDateFormat = SimpleDateFormat("MM/dd/yyyy -- hh:mm:ss" )
            var stringData:String = dateFormat.format(date)

            return stringData
        }

        fun dateSelected(date:String): String {
            var returnData:List<String> = date.split("/" , "--")
            var mounthYear:String = returnData[0] + returnData[2]

            return mounthYear
        }
    }

}