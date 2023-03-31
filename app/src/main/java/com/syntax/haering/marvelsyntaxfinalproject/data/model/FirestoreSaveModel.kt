package com.syntax.haering.marvelsyntaxfinalproject.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FirestoreSaveModel(
    @DocumentId
    var docID: String = "",
    var marvelID: String = "",
    @ServerTimestamp
    var timestamp: Date = Date()
)