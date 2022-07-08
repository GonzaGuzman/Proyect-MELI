package com.zalo.proyectmeli

import com.nhaarman.mockito_kotlin.verify
import com.zalo.proyectmeli.presenter.splash.SplashPresenter
import com.zalo.proyectmeli.presenter.splash.SplashView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashPresenterTest {
    @Mock
    private lateinit var splashView: SplashView

    private lateinit var splashPresenter: SplashPresenter

    @Before
    fun setup(){
        splashPresenter = SplashPresenter(splashView)
    }

    @Test
    fun `initComponent call`(){
        //GIVEN
        //WHEN
        splashPresenter.initComponent()
        //THEN
        verify(splashView).show()
    }

    @Test
    fun `navigateTo call`(){
        //GIVEN
        //WHEN
        splashPresenter.navigateTo()
        //THEN
        verify(splashView).startHome()
    }
}