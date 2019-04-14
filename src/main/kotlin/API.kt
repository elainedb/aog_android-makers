import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.ThreadManager
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.internal.NonNull
import model.ScheduleSlotList
import model.SessionJson
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.*

object API {

    private fun getDatabase(): Firestore {
        val file = File("WEB-INF/serviceAccountKey.json")
//        val file = File("src/main/webapp/WEB-INF/serviceAccountKey.json") // local
        val serviceAccount = FileInputStream(file)

        if (FirebaseApp.getApps().isEmpty()) {
            val credentials = GoogleCredentials.fromStream(serviceAccount)
            val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId("your-project-id")
                .setThreadManager(BasicScalingThreadManager())
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

    class BasicScalingThreadManager : ThreadManager() {

        override fun getThreadFactory(): ThreadFactory {
            // This thread factory will be used by all database-related code.
            return Executors.defaultThreadFactory()
        }

        override fun getExecutor(@NonNull firebaseApp: FirebaseApp): ExecutorService {
            // Use a single-threaded executor which keeps threads alive for 1 minute.
            return ThreadPoolExecutor(
                0, 1,
                60L, TimeUnit.SECONDS, SynchronousQueue(), threadFactory
            )
        }

        override fun releaseExecutor(@NonNull firebaseApp: FirebaseApp, @NonNull executorService: ExecutorService) {
            executorService.shutdownNow()
        }
    }

}