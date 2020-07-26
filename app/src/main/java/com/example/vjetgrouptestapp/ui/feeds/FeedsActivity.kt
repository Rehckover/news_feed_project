package com.example.vjetgrouptestapp.ui.feeds

import com.example.vjetgrouptestapp.R
import com.example.vjetgrouptestapp.base.arch.BaseActivity
import com.example.vjetgrouptestapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsActivity : BaseActivity<FeedActivityViewModel, ActivityMainBinding>() {

    override fun layoutResId(): Int = R.layout.activity_main

    override fun viewModelClass(): Class<FeedActivityViewModel> = FeedActivityViewModel::class.java
}