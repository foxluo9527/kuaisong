package com.ps.wb.ui.view

import android.annotation.SuppressLint
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeProvider : LinkageProvider {
        override fun firstLevelVisible(): Boolean {
            return true
        }

        override fun thirdLevelVisible(): Boolean {
            return false
        }

        override fun provideFirstData(): MutableList<String> {
            val days = arrayOf("今天", "明天", "后天")
            return days.toMutableList()
        }

        @SuppressLint("SimpleDateFormat")
        override fun linkageSecondData(firstIndex: Int): MutableList<String> {
            val list = ArrayList<String>()
            if (firstIndex == 0) {
                var h = SimpleDateFormat("HH").format(Date()).toInt()
                if (h < 24) {
                    list.add("现在")
                    h++
                    for (hour in h..24) {
                        list.add("$hour:00")
                    }
                }
            } else {
                for (hour in 0..24) {
                    list.add("$hour:00")
                }
            }
            return list
        }

        override fun linkageThirdData(firstIndex: Int, secondIndex: Int): MutableList<*> {
            return ArrayList<String>()
        }

        override fun findFirstIndex(firstValue: Any?): Int {
            return when (firstValue) {
                "今天" -> 0
                "明天" -> 1
                "后天" -> 2
                else -> -1
            }
        }

        override fun findSecondIndex(firstIndex: Int, secondValue: Any?): Int {
            if (secondValue==null)
                return -1
            val list = linkageSecondData(firstIndex)
            return list.indexOf(secondValue)
        }

        override fun findThirdIndex(firstIndex: Int, secondIndex: Int, thirdValue: Any?): Int {
            return 0
        }
    }