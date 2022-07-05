package com.zalo.proyectmeli.activities.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zalo.proyectmeli.activities.home.HomeActivity
import com.zalo.proyectmeli.databinding.ActivitySplashBinding
import com.zalo.proyectmeli.presenter.splash.SplashPresenter
import com.zalo.proyectmeli.presenter.splash.SplashView
import com.zalo.proyectmeli.utils.SPLASH_DELAY
import java.util.*

class SplashActivity : AppCompatActivity(), SplashView {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashPresenter = SplashPresenter(this)
        splashPresenter.initComponent()
    }

    override fun show() =
        Timer().schedule(object : TimerTask() {
            override fun run() {
                splashPresenter.navigateTo()
            }
        }, SPLASH_DELAY)

    override fun startHome() =
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java)).apply {
            finish()
        }
}

