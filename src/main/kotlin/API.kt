import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import model.ScheduleSlotList
import model.SessionJson

object API {

    private fun getDatabase(): Firestore {
        if (FirebaseApp.getApps().isEmpty()) {
            val credentials = GoogleCredentials.getApplicationDefault()
            val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId("your-project-id")
                .build()
            FirebaseApp.initializeApp(options)
        }
        return FirestoreClient.getFirestore()
    }

    fun getSlotsPOJO(): ScheduleSlotList? {
        val slotsDocument = getDatabase().collection("schedule-app").document("slots").get().get()
        return slotsDocument.toObject(ScheduleSlotList::class.java)
    }

    fun getSessionsPOJO(): Map<String, SessionJson> {
        val sessions = mutableMapOf<String, SessionJson>()
        val sessionsDocuments = getDatabase().collection("sessions").get().get().documents
        sessionsDocuments.forEach { sessions[it.id] = it.toObject(SessionJson::class.java) }
        return sessions
    }

}