package com.artgallery.ui.activity

import android.text.TextUtils
import android.view.KeyEvent
import com.artgallery.R
import com.artgallery.base.BaseActivity
import com.artgallery.databinding.ActivityPinCodeKtBinding
import com.artgallery.utils.SharedPreUtils
import com.blankj.utilcode.util.ToastUtils

class PinCodeKtActivity : BaseActivity<ActivityPinCodeKtBinding>() {
    var firstPsw: String? = null

    private var only_verify = true
    private var reset_passwd = false
    private var set_passwd = false
    private var start = false

    var psw: String? = null

    private fun pinCodeEntered(code: String) {

        if (only_verify) {
            if (code.equals(psw)) {
                //验证通过
                setResult(1)
                finish()
            } else {
                mDataBinding.pinCodeView.setTitle("密码错误")
                mDataBinding.pinCodeView.resetInput()
            }
        } else if (reset_passwd) {
            if (code.equals(psw) && !start) {
                mDataBinding.pinCodeView.setTitle("请输入新密码")
                mDataBinding.pinCodeView.resetInput()
                start = true
            } else if (start) {
                if (TextUtils.isEmpty(firstPsw)) {
                    firstPsw = code
                    mDataBinding.pinCodeView.setTitle("请再次输入密码")
                    mDataBinding.pinCodeView.resetInput()
                } else if (firstPsw.equals(code)) {
                    SharedPreUtils.setString(this, SharedPreUtils.KEY_PIN, code)
                    ToastUtils.showShort("密码设置完成")
                    finish()
                } else {
                    firstPsw = ""
                    mDataBinding.pinCodeView.setTitle("两次密码不一致，请重新输入")
                    mDataBinding.pinCodeView.resetInput()
                }
            } else {
                mDataBinding.pinCodeView.setTitle("密码错误")
                mDataBinding.pinCodeView.resetInput()
            }
        } else if (set_passwd) {//重设密码
            if (TextUtils.isEmpty(firstPsw)) {
                firstPsw = code
                mDataBinding.pinCodeView.setTitle("请再次输入密码")
                mDataBinding.pinCodeView.resetInput()
            } else if (firstPsw.equals(code)) {
                SharedPreUtils.setString(this, SharedPreUtils.KEY_PIN, code)
                SharedPreUtils.setString(this, SharedPreUtils.ACCOUNT_KEY_PIN, code)
                ToastUtils.showShort("密码设置完成")
                setResult(1)
                finish()
            } else {
                firstPsw = ""
                mDataBinding.pinCodeView.setTitle("两次密码不一致，请重新输入")
                mDataBinding.pinCodeView.resetInput()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pin_code_kt
    }

    override fun initPresenter() {
    }

    override fun initView() {
        mDataBinding.pinCodeView.setTitle("请输入密码")
        psw = SharedPreUtils.getString(this, SharedPreUtils.KEY_PIN)
        only_verify = intent.getBooleanExtra("ONLY_VERIFY", false)
        reset_passwd = intent.getBooleanExtra("RESET_PASSWORD", false)
        set_passwd = intent.getBooleanExtra("SET_PASSWORD", false)
        with(mDataBinding.pinCodeView) {
            pinCodeEnteredListener = { pinCodeEntered(it) }
        }
        mDataBinding.cancle.setOnClickListener { finish() }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (set_passwd)
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true
            }
        return super.onKeyDown(keyCode, event)
    }
}