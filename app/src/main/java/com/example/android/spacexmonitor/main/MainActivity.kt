package com.example.android.spacexmonitor.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.spacexmonitor.R
import com.example.android.spacexmonitor.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

// если хотябы один дочерний фрагмент нужно использовать с Hilt (использовать его predefined компонент),
// то и для активити нужно задействовать её компонент с помощью @AndroidEntryPoint иначе
// граф зависимостей не построится
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return NavigationUI.navigateUp(navController, null)
    }
}

//@AndroidEntryPoint привязывает данную активити к созданному в сиситеме Hilt компоненту "ActivityComponent"
//в отличие от даггер компоненты в Hilt уже созданы для типовых применений
//причем все компоненты по умолчанию Unscoped, это означает что каждый раз когда потребуется ссылка на
//зависимость - то будет сконструирован новый объект
//это поведение по умолчанию переопределяется с помощью Scope
//By default, all bindings in Dagger are “unscoped”. This means that each time the binding is requested,
//Dagger will create a new instance of the binding.
//However, Dagger also allows a binding to be “scoped” to a particular component (see the scope annotations
//in the table above). A scoped binding will only be created once per instance of the component it’s scoped to,
//and all requests for that binding will share the same instance.
