import org.junit.Test

class ApiTest {
    @Test
    fun test() {
        System.out.println("============ Slots ===============")
        System.out.println(API.getSlotsPOJO().toString())
        System.out.println("============ Sessions ============")
        System.out.println(API.getSessionsPOJO().toString())
    }
}