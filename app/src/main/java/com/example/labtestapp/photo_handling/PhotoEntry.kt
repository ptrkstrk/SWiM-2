package com.example.labtestapp.photo_handling



data class PhotoEntry(val url: String, val name: String,
                      val date: String, var tags: ArrayList<String> = ArrayList()) {

    fun evaluateSimilarity(comparedEntry: PhotoEntry): Int {
        var similarityVal = 0
        for(tag in comparedEntry.tags){
            if (tags.contains(tag))
                similarityVal++
        }
        return similarityVal
    }
}
