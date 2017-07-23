package com.putao.ptx.assistant.haicientity;

interface ISimplePreview {
    fun title(): MutableList<String>
    fun pronounce(): MutableList<String>
    fun basicExplain(): MutableList<String>
    fun example(): MutableList<String>

    fun titleStr(): String {
        val list = pronounce()
        val s = if (list.isNotEmpty()) list.joinToString(prefix = "[", postfix = "]") else ""
        return title()[0] + /*有一个空格*/" " + s
    }

    fun explainStr() = with(StringBuffer()) {
        append("基本释义:")
        append(basicExplain().joinToString(separator = ";"))
        append("\n\n")
        val bShowSentence = false
        if (bShowSentence) {
            val example = example().joinToString(separator = "\n")
            if (!example.isNullOrEmpty()) {
                append("例句：\n")
                append(example)
            }
        }
        this@with
    }.toString()

    fun isTitleExplainValid(): Boolean = titleStr().isNotBlank() || explainStr().isNotBlank()
}