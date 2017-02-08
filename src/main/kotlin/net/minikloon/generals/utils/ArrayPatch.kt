package net.minikloon.generals.utils

fun patch(array: IntArray, diff: IntArray) : IntArray {
    val finalSize = surveyFinalSize(diff)
    val patched = IntArray(finalSize) { if(it < array.size) array[it] else 0 }
    patchInPlace(patched, diff)
    return patched
}

private fun surveyFinalSize(diff: IntArray) : Int {
    var finalSize = 0
    
    var diffIndex = 0
    while(diffIndex < diff.size) {
        val matching = diff[diffIndex++]
        finalSize += matching

        if(diffIndex >= diff.size) break
        val mismatching = diff[diffIndex++]
        finalSize += mismatching

        diffIndex += mismatching
    }
    return finalSize
}

fun patchInPlace(array: IntArray, diff: IntArray) {
    var arrayIndex = 0
    var diffIndex = 0
    while(diffIndex < diff.size) {
        val matching = diff[diffIndex++]
        if(diffIndex >= diff.size) break
        val mismatching = diff[diffIndex++]

        arrayIndex += matching
        (0 until mismatching).forEach {
            array[arrayIndex++] = diff[diffIndex++]
        }
    }
}