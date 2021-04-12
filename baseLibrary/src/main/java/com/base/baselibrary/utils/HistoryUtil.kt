package com.base.baselibrary.utils

import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.reflect.TypeToken


class HistoryUtil private constructor(private val key: String, private val maxCount: Int) {

    private var spUtils: SPUtils = SPUtils.getInstance("history")
    var history: MutableList<String>
        private set

    init {
        val spHistory = spUtils.getString(key)
        history = if (TextUtils.isEmpty(spHistory))
            mutableListOf()
        else
            GsonUtils.fromJson(spHistory, object : TypeToken<List<String>>() {}.type)


        keepMaxCount()
    }

    companion object {
        @Volatile
        private var instance: HistoryUtil? = null

        @JvmStatic
        @JvmOverloads
        fun getInstance(name: String = "history", maxCount: Int = 10): HistoryUtil =
                instance ?: synchronized(this) {
                    instance ?: HistoryUtil(name, maxCount).also { instance = it }
                }

        @JvmStatic
        fun getInstance(maxCount: Int = 10): HistoryUtil {
            return getInstance("history", maxCount)
        }
    }

    fun addHistory(newHistory: String) {
        if (history.contains(newHistory)) {
            history.remove(newHistory)
        }
        history.add(0, newHistory)
        keepMaxCount()
        putInSp()
    }

    fun clearHistory() {
        history.clear()
        spUtils.put(key, "")
    }

    /**
     * make sure history's max count
     */
    private fun keepMaxCount() {
        if (history.size > maxCount) {
            history = history.subList(0, maxCount)
        }
    }

    private fun putInSp() {
        spUtils.put(key, GsonUtils.toJson(history))
    }
}