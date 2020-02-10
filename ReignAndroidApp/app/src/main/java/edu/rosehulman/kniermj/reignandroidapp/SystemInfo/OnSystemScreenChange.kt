package edu.rosehulman.kniermj.reignandroidapp.SystemInfo

interface OnSystemScreenChange {
    fun onCommandQueueSelected(sysId: String)
    fun onCommandHistorySelected(sysId: String)
}