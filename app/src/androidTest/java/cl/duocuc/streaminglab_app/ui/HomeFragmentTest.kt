package cl.duocuc.streaminglab_app.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.streaminglab_app.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun muestraTextoInicialSinError() {
        // Lanza el fragment
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_StreamingLab
        )


        onView(withId(R.id.tvWelcome))
            .check(matches(withText("Bienvenido a StreamingLab")))
    }
}
