package loginapp

import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import ru.yandex.loginapp.LoginScreenState
import ru.yandex.loginapp.LoginViewModel

class LoginViewModelTest {

    private var loginVM: LoginViewModel? = null
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginVM = LoginViewModel()
    }

    @After
    fun tearDown() {
        loginVM = null
        Dispatchers.resetMain()
    }

    /**
     * Проверяет, что после введения пользователем пустых email и пароля
     * состояние экрана стало равным [LoginScreenState.EmptyFieldsError]
     */
    @Test
    fun testLoginEmptyFieldsReturnsEmptyFieldsError() {
        val email = ""
        val password = ""

        loginVM?.let {
            it.login(email, password)
            assert(it.state.value == LoginScreenState.EmptyFieldsError)
        }
    }

    /**
     * Проверяет состояние экрана на наличие ошибки
     * после введения некорректного email (просто случайная строка без @ и точек).
     * Ожидаемый результат - [LoginScreenState.EmailValidationError]
     */
    @Test
    fun testLoginIncorrectEmailReturnsEmailValidationError() {
        val email = "incorrect_email"
        val password = "Myp@ssw0rd"

        loginVM?.let {
            it.login(email, password)
            assert(it.state.value == LoginScreenState.EmailValidationError)
        }
    }

    /**
     * Проверяет, что при правильно введенных почте и пароле
     * состояние экрана переходит в загрузку [LoginScreenState.Loading]
     */
    @Test
    fun testLoginCorrectFieldsReturnLoading() = runTest {
        val email = "asya_vishnewskaya@yandex.com"
        val password = "Myp@ssw0rd"

        loginVM?.let {
            it.login(email, password)
            advanceTimeBy(1000)
            assert(it.state.value == LoginScreenState.Loading)
        }
    }

    /**
     * Проверяет, что после успешной загрузки
     * состояние экрана переходит в [LoginScreenState.Success]
     */
    @Test
    fun testLoginCorrectFieldsReturnLoadingThenSuccess() = runTest {
        val email = "asya_vishnewskaya@yandex.com"
        val password = "Myp@ssw0rd"

        loginVM?.let {
            it.login(email, password)
            advanceUntilIdle()
            assert(it.state.value == LoginScreenState.Success)
        }
    }

}