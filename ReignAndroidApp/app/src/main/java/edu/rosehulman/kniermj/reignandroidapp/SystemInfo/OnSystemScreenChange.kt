package edu.rosehulman.kniermj.reignandroidapp.SystemInfo

interface OnSystemScreenChange {
    fun onCommandQueueSelected(sysId: String)
    fun onCommandHistorySelected(sysId: String)
    fun onProcessesSelected(sysId: String)
    fun onHistoryGraphPressed(sysId: String)
}