package com.example.labtestapp.photo_handling



data class PhotoEntry(val url: String, val name: String,
                      val date: String, var tags: ArrayList<String> = ArrayList()) {

    fun evaluateSimilarity(comparedEntry: PhotoEntry): Int {
        var similarityVal = 0
        if(comparedEntry.tags != null && tags!= null)
        for (photoTag in comparedEntry.tags) {
            if (tags.contains(photoTag))
                similarityVal++
        }
        return similarityVal
    }
}
