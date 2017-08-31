package com.werb.eventbuskotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.werb.eventbus.EventBus
import com.werb.eventbus.Subscriber
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = getString(R.string.app_name)
        supportFragmentManager.beginTransaction().add(R.id.content_layout, MyFragment(), MyFragment::class.java.name).commitAllowingStateLoss()

        login.setOnClickListener {
            LoginDialogFragment.newInstance("login", "A Simple EventBus with Kotlin").show(supportFragmentManager, "LoginDialogFragment")
        }

        background.setOnClickListener {
            EventBus.post(RequestMeizhiEvent())
        }

        main.setOnClickListener {
            Thread{
                EventBus.post(MainEvent())
            }.start()
        }

    }

    override fun onStart() {
        super.onStart()
        // 注册 EventBus
        EventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        // 注销 EventBus
        EventBus.unRegister(this)
    }

    /** 符合 LoginEvent 且 tag = Default_TAG 时执行此方法，执行线程为 POST 线程*/
    @Subscriber
    private fun login(event: LoginEvent) {
        if (event.login) {
            login.text = "已登录"
            button_layout.visibility = View.GONE
            toolbar.inflateMenu(R.menu.toolbar)
            toolbar.setOnMenuItemClickListener { EventBus.post(LoginEvent(false)); true }
        } else {
            login.text = "模拟登录(发送普通 Event)"
            button_layout.visibility = View.VISIBLE
            toolbar.menu.clear()
        }
    }

    /** 符合 LoginEvent 且 tag = request load data 时执行此方法，执行线程为 POST 线程*/
    @Subscriber(tag = "request load data")
    private fun loadRequest(event: LoginEvent) {
        login.text = "已登录"
        button_layout.visibility = View.GONE
        toolbar.inflateMenu(R.menu.toolbar)
        toolbar.setOnMenuItemClickListener { EventBus.post(LoginEvent(false)); true }
    }

}
